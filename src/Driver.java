import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputListener;

public class Driver {

	public static void main(String[] args) throws InterruptedException {

		boolean[][] board = new boolean[100][100];

		int screenWidth = 800;
		int screenHeight = 800;

		int tickRateMilliseconds;
		int cellSizePixels = (screenWidth < screenHeight ? screenWidth : screenHeight)
				/ (board.length < board[0].length ? board.length : board[0].length);

		board[5][5] = true;
		board[6][6] = true;
		board[4][7] = true;
		board[5][7] = true;
		board[6][7] = true;

		GameOfLife g = new GameOfLife(board);

		// Graphics stuff
		JFrame jf = new JFrame("Conway's Game of Life");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Screen screen = new Screen(g, cellSizePixels);

		PauseButton pb = new PauseButton();
		StepButton sb = new StepButton(pb);
		ClearButton cb = new ClearButton(pb, screen, g);

		JButton step = sb.button;
		JButton pause = pb.button;
		JButton clear = cb.button;

		JPanel centralPanel = new JPanel(new BorderLayout());
		centralPanel.add(screen, BorderLayout.CENTER);
		centralPanel.setBackground(Color.WHITE);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		buttonPanel.add(pause);
		buttonPanel.add(step);
		buttonPanel.add(clear);

		centralPanel.add(buttonPanel, BorderLayout.SOUTH);
		centralPanel.setSize(centralPanel.getPreferredSize());

		jf.add(centralPanel, BorderLayout.CENTER);
		jf.setSize(screenWidth + 20, screenHeight + 82);

		jf.setVisible(true);

		tickRateMilliseconds = 100;

		while (true) {
			Thread.sleep(tickRateMilliseconds);
			if (!pb.isPaused) {
				g.step();
				screen.repaint();

			}
			if (sb.canStep) {
				g.step();
				screen.repaint();
				sb.canStep = false;
			}
			// jf.setTitle("Conway's Game of Life " + jf.getWidth() + " x " +
			// jf.getHeight());
		}

	}

}

class Screen extends JPanel implements MouseInputListener {

	GameOfLife game;
	final int scaleFactor;
	final int width;
	final int height;

	boolean isMouseDepressed = false;

	public Screen(GameOfLife game, int scaleFactor) {
		this.game = game;
		this.scaleFactor = scaleFactor;
		this.width = game.board.length * scaleFactor + 24;
		this.height = game.board[0].length * scaleFactor + 48;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void paint(Graphics g) {

		for (int i = 0; i < game.board.length; i++) {
			for (int j = 0; j < game.board[0].length; j++) {
				if (game.board[i][j]) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.WHITE);
				}

				g.fillRect(i * scaleFactor, j * scaleFactor, scaleFactor, scaleFactor);
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		try {
			if (SwingUtilities.isLeftMouseButton(e)) {
				game.board[e.getX() / scaleFactor][e.getY() / scaleFactor] = true;
				repaint();
			} else if (SwingUtilities.isRightMouseButton(e) || e.isShiftDown()) {
				game.board[e.getX() / scaleFactor][e.getY() / scaleFactor] = false;
				repaint();
			}
		} catch (Exception exception) {

		}

		// System.out.println(e.isShiftDown());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseDepressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseDepressed = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

		try {
			if (SwingUtilities.isLeftMouseButton(e)) {
				game.board[e.getX() / scaleFactor][e.getY() / scaleFactor] = true;
				repaint();
			}
			if (SwingUtilities.isRightMouseButton(e) || e.isShiftDown()) {
				game.board[e.getX() / scaleFactor][e.getY() / scaleFactor] = false;
				repaint();
			}
		} catch (Exception exception) {

		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

}

class PauseButton implements ActionListener {

	public boolean isPaused = true;
	JButton button;

	public PauseButton() {
		button = new JButton("Start");
		button.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		isPaused = !isPaused;
		button.setText(isPaused ? "Unpause" : "Pause");

		System.out.println(isPaused ? "Is Paused" : "Unpaused");

	}

}

class StepButton implements ActionListener {

	boolean canStep = false;

	JButton button;
	PauseButton pb;

	public StepButton(PauseButton pb) {
		button = new JButton("Step");
		button.addActionListener(this);
		this.pb = pb;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (pb.isPaused) {
			canStep = true;
		}
	}

}

class ClearButton implements ActionListener {

	PauseButton pb;
	JButton button;

	Screen screen;

	GameOfLife game;

	public ClearButton(PauseButton pb, Screen screen, GameOfLife game) {
		button = new JButton("Clear");
		button.addActionListener(this);
		this.pb = pb;
		this.screen = screen;
		this.game = game;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (!pb.isPaused) {
			pb.actionPerformed(e);

		}
		pb.button.setText("Start");
		game.board = new boolean[game.board.length][game.board[0].length];
		screen.repaint();
	}
}
