package uet.oop.bomberman;

import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.input.Keyboard;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JButton;


/**
 * Tạo vòng lặp cho game, lưu trữ một vài tham số cấu hình toàn cục,
 * Gọi phương thức render(), update() cho tất cả các entity
 */
public class Game extends Canvas {

	public static final int TILES_SIZE = 16,
			WIDTH = TILES_SIZE * (31 / 2),
			HEIGHT = 13 * TILES_SIZE;

	public static int SCALE = 3;

	public static final String TITLE = "BombermanGame";
	public static final int TICKS_PER_SECOND = 60;

	public static final int BOMBRATE = 1;
	public static final int BOMBRADIUS = 1;
	public static final double BOMBERSPEED = 1.0;// toc do bomber
	private static int itemTime;

	public static final int TIME = 200;
	public static final int ITEM_TIME = 20;
	public static final int POINTS = 0;

	protected static int SCREENDELAY = 3;

	protected static int bombRate = BOMBRATE;
	protected static int bombRadius = BOMBRADIUS;
	protected static double bomberSpeed = BOMBERSPEED;

	protected int _screenDelay = SCREENDELAY;

	private Keyboard _input;
	private boolean _running = false;
	private boolean _paused = true;
	private static Board _board;
	private Screen screen;
	private Frame _frame;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game(Frame frame) {
		_frame = frame;
		_frame.setTitle(TITLE);

		screen = new Screen(WIDTH, HEIGHT);
		_input = new Keyboard();

		_board = new Board(this, _input, screen);
		addKeyListener(_input);
	
	}

	private void renderGame() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();

		_board.render(screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen._pixels[i];
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		_board.renderMessages(g);

		g.dispose();
		bs.show();
	}

	private void renderScreen() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();

		Graphics g = bs.getDrawGraphics();

		_board.drawScreen(g);

		g.dispose();
		bs.show();
	}

	private void update() {
		_input.update();
		_board.update();
		if (_input.pause) { // Kiểm tra nếu phím "p" được nhấn
			_board.setShow(3); // Hiển thị màn hình tạm dừng
			_paused = true; // Đặt trạng thái game là tạm dừng
			return;
	}
	}
	public void start() {
		_running = true;
		long  lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / TICKS_PER_SECOND; // nanosecond, 60 frames per second
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (_running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}

			if (_paused) {
				if (_screenDelay <= 0) {
					_board.setShow(-1);
					_paused = false;
				}

				renderScreen();
			} else {
				renderGame();
			}

			if (_input.resume) {
				_paused = false;
				_board.setShow(-1);
				}
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				_frame.setTime(_board.subtractTime());
				_frame.setPoints(_board.getPoints());
				_frame.setItemTime(_board.getItemTime());
				timer += 1000;
				_frame.setTitle(TITLE + " | " + updates + " rate, " + frames + " fps");
				updates = 0;
				frames = 0;

				if (_board.getShow() == 2)
					--_screenDelay;
			}
		}
	}

	public static int getItemTime() {
		return itemTime;
	}

	public static double getBomberSpeed() {
		return bomberSpeed;
	}

	public static int getBombRate() {
		return bombRate;
	}

	public static int getBombRadius() {
		return bombRadius;
	}

	public static void addBomberSpeed(double i) {
		bomberSpeed += i;
	}

	public static void addBombRadius(int i) {
		bombRadius += i;
	}

	public static void addBombRate(int i) {
		bombRate += i;
	}

	public void resetScreenDelay() {
		_screenDelay = SCREENDELAY;
	}
	public static Board getBoard() {
		return _board;
	}
	public boolean isPaused() {
		return _paused;
	}

	public void pause() {
		_paused = !_paused;
	}

	public static void setBombRate(int bombRate) {
		Game.bombRate = bombRate;
	}

	public static void setBombRadius(int bombRadius) {
		Game.bombRadius = bombRadius;
	}

	public static void setBomberSpeed(double bomberSpeed) {
		Game.bomberSpeed = bomberSpeed;
	}

}
