package uet.oop.bomberman.agent.rl.dtypes;

import java.util.Arrays;
import java.util.Random;

import ai.djl.ndarray.NDManager;

public final class Memory {
    private final Random random;
    private final int capacity;
    private final Transition[] memory;

    private float[] state_prev;
    private int action;
    private float reward;
    private boolean mask;
    private int stage;
    private int head;
    private int size;

    public Memory(int capacity) {
        this(capacity, 0);
    }

    public Memory(int capacity, int seed) {
        this.capacity = capacity;
        this.memory = new Transition[capacity];
        this.random = new Random(seed);

        reset();
    }

    public void setState(float[] state) {
        assertStage(0);
        if (state_prev != null) {
            add(new Transition(state_prev, state, action, reward, mask));
        }
        state_prev = state;

    }

    public void setAction(int action) {
        assertStage(1);
        this.action = action;
    }

    public void setRewardAndMask(float reward, boolean mask) {
        assertStage(2);
        this.reward = reward;
        this.mask = mask;

        if (mask) {
            add(new Transition(state_prev, null, action, reward, mask));
            state_prev = null;
            action = -1;
        }

    }

    public Transition[] sample(int sample_size) {
        Transition[] chunk = new Transition[sample_size];
        for (int i = 0; i < sample_size; i++) {
            chunk[i] = memory[random.nextInt(size)];
        }

        return chunk;
    }

    public MemoryBatch sampleBatch(int sample_size, NDManager manager) {
        return getBatch(sample(sample_size), manager, sample_size);
    }

    public MemoryBatch getOrderedBatch(NDManager manager) {
        return getBatch(memory, manager, size());
    }

    public Transition get(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound " + index);
        }
        return memory[index];
    }

    public int size() {
        return size;
    }

    public void reset() {
        state_prev = null;
        action = -1;
        reward = 0.0F;
        mask = false;
        stage = 0;
        head = -1;
        size = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(memory);
    }

    private void add(Transition transition) {
        head += 1;
        if (head >= capacity) {
            head = 0;
        }

        memory[head] = transition;
        if (size < capacity) {
            size++;
        }
    }

    private void assertStage(int i) {
        if (i != stage) {
            String info_name;
            switch (stage) {
            case 0:
                info_name = "State";
                break;
            case 1:
                info_name = "Action";
                break;
            case 2:
                info_name = "Reward and Mask";
                break;
            default:
                info_name = null;
            }
            throw new IllegalStateException("Expected information: " + info_name);
        } else {
            stage++;
            if (stage > 2) {
                stage = 0;
            }
        }
    }

    private MemoryBatch getBatch(Transition[] transitions, NDManager manager, int batch_size) {

        float[][] states = new float[batch_size][];
        float[][] next_states = new float[batch_size][];
        int[] actions = new int[batch_size];
        float[] rewards = new float[batch_size];
        int[] masks = new int[batch_size];

        int index = head;
        for (int i = 0; i < batch_size; i++) {
            index++;
            if (index >= batch_size) {
                index = 0;
            }
            states[i] = transitions[index].getState();
            float[] next_state = transitions[index].getNextState();
            next_states[i] = next_state != null ? next_state : new float[states[i].length];
            actions[i] = transitions[index].getAction();
            rewards[i] = transitions[index].getReward();
            masks[i] = transitions[index].isMasked() ? 1 : 0;
        }

        return new MemoryBatch(manager.create(states), manager.create(next_states), manager.create(actions),
                manager.create(rewards), manager.create(masks));
    }

}
