package uet.oop.bomberman.input;

import java.awt.event.KeyEvent;

public class Keyboard1 extends Keyboard {
    private boolean[] keys = new boolean[120]; // 120 is enough to this game

    @Override
    public void update() {
        super.update(); // Cập nhật các phím điều khiển chung từ class Keyboard gốc

        // Cập nhật các phím điều khiển riêng cho Bomber 1
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        space = keys[KeyEvent.VK_X];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
