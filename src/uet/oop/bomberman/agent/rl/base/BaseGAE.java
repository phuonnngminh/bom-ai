package uet.oop.bomberman.agent.rl.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import uet.oop.bomberman.agent.rl.dtypes.Memory;
import uet.oop.bomberman.agent.rl.model.DistributionValueModel;
import uet.oop.bomberman.agent.rl.utils.ActionSampler;

public abstract class BaseGAE extends BaseAgent {
    protected final Random random = new Random();
    protected final Memory memory = new Memory(1024);
    protected final Optimizer optimizer;

    public NDManager manager = NDManager.newBaseManager();
    protected Model model;
    public Model getModel() {
        return model;
    }

    protected Predictor<NDList, NDList> predictor;

    private final float gae_lambda;
    private final float gamma;
    protected final int num_of_action;
    protected final int dim_of_state_space;
    private final int hidden_size;

    public BaseGAE(int dim_of_state_space, int num_of_action, int hidden_size, float gamma, float gae_lambda,
            float learning_rate) {
        this.gae_lambda = gae_lambda;
        this.gamma = gamma;
        this.dim_of_state_space = dim_of_state_space;
        this.num_of_action = num_of_action;
        this.hidden_size = hidden_size;
        this.optimizer = Optimizer.adam()
            .optLearningRateTracker(Tracker.fixed(learning_rate))
            .optClipGrad(1.0f)
            .build();
        this.reset();
    }

    protected Supplier<Boolean[]> getActionMask;
    public void setActionMaskGetter(Supplier<Boolean[]> getActionMask) {
        this.getActionMask = getActionMask;
    }

    @Override
    public int react(float[] state) {
        try (NDManager submanager = manager.newSubManager()) {
            if (!isEval()) {
                memory.setState(state);
            }
            
            final float EXPLORE_RATE = 0.05f;
            int action;
            if (random.nextFloat() < EXPLORE_RATE) {
                Boolean[] actionMask = getActionMask.get();
                List<Integer> validActions = new ArrayList<>();
                for (int i = 0; i < num_of_action; i++) {
                    if (actionMask[i]) {
                        validActions.add(i);
                    }
                }
                action = validActions.get(random.nextInt(validActions.size()));
                // System.out.println("Random action: " + action);
            } else {
                NDArray prob = predictor.predict(new NDList(submanager.create(state))).get(0);
                action = ActionSampler.sampleMultinomial(prob, random);
                System.out.println(" action: " + action);
            }

            if (!isEval()) {
                memory.setAction(action);
            }

            return action;

        } catch (TranslateException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void collect(float reward, boolean done) {
        if (!isEval()) {
            memory.setRewardAndMask(reward, done);
            if (done) {
                try (NDManager submanager = manager.newSubManager()) {
                    updateModel(submanager);
                } catch (TranslateException e) {
                    throw new IllegalStateException(e);
                }
                memory.reset();
            }
        }
    }

    @Override
    public void reset() {
        if (manager != null) {
            manager.close();
        }
        manager = NDManager.newBaseManager();
        model = DistributionValueModel.newModel(manager, dim_of_state_space, hidden_size, num_of_action);
        predictor = model.newPredictor(new NoopTranslator());
    }

    public void load() {
        try {
            Path dir = Paths.get("models");
            File file = new File("models/PPO.params");
            Files.createDirectories(dir);
            DataInputStream is = new DataInputStream(Files.newInputStream(file.toPath()));
            model.getBlock().loadParameters(manager, is);
        } catch (NoSuchFileException e) {
            System.out.println("No pre-trained model found");
        } catch (MalformedModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            File file = new File("models/PPO.params");

            DataOutputStream os = new DataOutputStream(Files.newOutputStream(file.toPath()));
            model.getBlock().saveParameters(os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected NDList estimateAdvantage(NDArray values, NDArray rewards) {
        NDArray expected_returns = rewards.duplicate();
        NDArray advantages = rewards.sub(values.squeeze());
        for (long i = expected_returns.getShape().get(0) - 2; i >= 0; i--) {
            NDIndex index = new NDIndex(i);
            expected_returns.set(index, expected_returns.get(i).add(expected_returns.get(i + 1).mul(gamma)));
            advantages.set(index,
                    advantages.get(i).add(values.get(i + 1).add(advantages.get(i + 1).mul(gae_lambda)).mul(gamma)));
        }

        return new NDList(expected_returns, advantages);
    }

    protected abstract void updateModel(NDManager submanager) throws TranslateException;

}
