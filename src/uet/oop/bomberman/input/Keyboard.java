package uet.oop.bomberman.input;

import uet.oop.bomberman.utils.EGameControl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

/**
 * Tiếp nhận và xử lý các sự kiện nhập từ bàn phím
 */
public class Keyboard implements KeyListener {

	public interface KeyboardInputCallback {
		void onKeyPressed(EGameControl gameControl);
	}

	private boolean[] keys = new boolean[200]; //120 is enough to this game
	public boolean up, down, left, right, space,x;
	public Optional<KeyboardInputCallback> keyboardInputCallback;

	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE];
		x = keys[KeyEvent.VK_X];

	}

	private EGameControl keyToGameControl(int keyCode) {
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			return EGameControl.UP;
		}

		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			return EGameControl.DOWN;
		}

		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			return EGameControl.LEFT;
		}

		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			return EGameControl.RIGHT;
		}

		if (keyCode == KeyEvent.VK_SPACE) {
			return EGameControl.SPACE;
		}

		if (keyCode == KeyEvent.VK_ENTER) {
			return EGameControl.ENTER;
		}
		if (keyCode == KeyEvent.VK_X) {
			return EGameControl.X;
		}

		return EGameControl.NONE;
	}
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (keyboardInputCallback.isPresent()) {
            KeyboardInputCallback callback = keyboardInputCallback.get();
            callback.onKeyPressed(keyToGameControl(e.getKeyCode()));
        }
    }

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

}