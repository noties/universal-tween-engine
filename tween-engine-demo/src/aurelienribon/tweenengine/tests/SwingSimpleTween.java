package aurelienribon.tweenengine.tests;

import aurelienribon.tweenaccessors.swing.ComponentAccessor;
import aurelienribon.utils.swing.DrawingCanvas;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Cubic;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SwingSimpleTween {
	public static void start() {
		new SwingSimpleTween();
	}

	private final JFrame window;
	private final JLabel label;
	private final JButton button;
	private final TweenManager tweenManager;

	public SwingSimpleTween() {
		int w = 800;
		int h = 600;

		label = new JLabel("Universal Tween Engine");
		label.setSize(label.getPreferredSize());
		label.setLocation(10, 10);

		button = new JButton("Push me!");
		button.setSize(button.getPreferredSize());
		button.setLocation(w/2 - button.getWidth()/2, h/2 - button.getHeight()/2);
		button.addMouseListener(buttonMouseListener);
		button.addActionListener(buttonActionListener);

		window = new JFrame("TweenEngine Swing test");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(w, h);
		window.setLayout(null);
		window.getContentPane().add(label);
		window.getContentPane().add(button);
		window.setVisible(true);

		window.addWindowListener(new WindowAdapter() {
			@Override public void windowOpened(WindowEvent e) {
				new DrawingCanvas() {
					@Override protected void update(int elapsedMillis) {
						tweenManager.update(elapsedMillis);
					}
				}.start();
			}
		});

		Tween.registerAccessor(Component.class, new ComponentAccessor());
		tweenManager = new TweenManager();

		Tween.to(label, ComponentAccessor.POSITION, 1000)
			.target(window.getContentPane().getWidth() - label.getWidth() - 10, 10)
			.ease(Cubic.INOUT)
			.repeatYoyo(-1, 200)
			.delay(500)
			.start(tweenManager);
	}

	private final MouseAdapter buttonMouseListener = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			Random rand = new Random();
			int tx = rand.nextInt(window.getContentPane().getWidth() - button.getWidth());
			int ty = rand.nextInt(window.getContentPane().getHeight() - 30 - button.getHeight()) + 30;

			tweenManager.killTarget(button);
			Tween.to(button, ComponentAccessor.POSITION, 500)
				.target(tx, ty)
				.ease(Back.OUT)
				.start(tweenManager);
		}
	};

	private final ActionListener buttonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(window, "Congratulations! I guess you had some luck, or resized the window :)");
		}
	};
}