package uet.oop.bomberman.input;

import java.awt.event.KeyEvent;

public class Keyboard2 extends Keyboard {
    private boolean[] keys = new boolean[120]; // 120 is enough to this game

    @Override
    public void update() {
        super.update();

        // Cập nhật các phím điều khiển riêng cho Bomber 1
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        space = keys[KeyEvent.VK_M];
        // System.out.println("hello");
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