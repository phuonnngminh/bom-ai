package uet.oop.bomberman.input;

import java.awt.event.KeyEvent;

public class Keyboard1 extends Keyboard {
    public boolean up1, down1, left1, right1; // Phím điều khiển riêng cho Bomber 1
    private boolean[] keys = new boolean[120]; // 120 is enough to this game

    @Override
    public void update() {
        super.update(); // Cập nhật các phím điều khiển chung từ class Keyboard gốc

        // Cập nhật các phím điều khiển riêng cho Bomber 1
        up1 = keys[KeyEvent.VK_W];
        down1 = keys[KeyEvent.VK_S];
        left1 = keys[KeyEvent.VK_A];
        right1 = keys[KeyEvent.VK_D];
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
