
//Author Name: Daniel McGee
//Date: 6/18/2021
//Program Name: FibonacciCalculatorMcGee
//Purpose: Implement 2 different approaches to calculating the Fibonacci series and compare the efficiencies. 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FibonacciCalculatorMcGee extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1952010163219627404L;

	/**
	 * A simple worker thread that calculates each value of the Fibonacci sequence up to an arbitrary point, using one of 2 different methods, times the execution
	 * of each method, and updates the chart with the time of each value. Once told to stop, it will no longer interact with the rest of the program and can be ignore,
	 * even if it is in the middle of executing a function.
	 * @author Daniel McGee
	 *
	 */
	public class FibWorker implements Runnable {
		private final int max;
		private boolean stop;
		private final boolean iterative;

		public FibWorker(boolean iterative, int max) {
			super();
			this.max = max;
			this.iterative = iterative;
		}

		public synchronized void stop() {
			this.stop = true;
		}

		@Override
		public void run() {

			for (int i = 0; i < max; i++) {
				long time = System.nanoTime();
				if (iterative) {
					fibIterative(i);
				} else {
					fibRecursive(i);
				}
				time = System.nanoTime() - time;
				//because this entire block is synchronized with the stop function, there will never be a situation where
				//stop returns and then this thread adds data to the chart.
				synchronized (this) {
					if (stop) {
						break;
					}
					if (iterative) {
						chart.addIterativeStep(time);
					} else {
						chart.addRecursiveStep(time);
					}
				}
			}

		}

	}

	private final ChartMcGee chart = new ChartMcGee();
	private FibWorker iterativeThread;
	private FibWorker recursiveThread;

	/**
	 * Simple UI for the test. Allows the user to specify the max sequence via a JSpinner, and launch the program with a JButton.
	 * Displays the current state of the program at the top. 
	 */
	public FibonacciCalculatorMcGee() {

		this.setPreferredSize(new Dimension(500, 400));
		this.setMinimumSize(new Dimension(250, 200));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(getPreferredSize());

		JPanel panel = new JPanel();

		getContentPane().add(panel, BorderLayout.SOUTH);

		JLabel lbl = new JLabel("Max Size: ");
		panel.add(lbl);

		JSpinner spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(60, 20));
		spinner.setMinimumSize(new Dimension(60, 20));
		spinner.setModel(new SpinnerNumberModel(15, 2, null, 1));
		panel.add(spinner);

		
		JButton btn = new JButton("Go");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iterativeThread != null) {
					iterativeThread.stop();
				}
				if (recursiveThread != null) {
					recursiveThread.stop();
				}
				chart.reset();
				new Thread(iterativeThread = new FibWorker(true, (Integer) spinner.getValue())).start();
				new Thread(recursiveThread = new FibWorker(false, (Integer) spinner.getValue())).start();
			}
		});

		panel.add(btn);
		getContentPane().add(chart, BorderLayout.CENTER);
		
		JPanel panelTop = new JPanel();
		getContentPane().add(panelTop, BorderLayout.NORTH);
		
		JLabel lblTitle = new JLabel("Fibonacci Sequence function efficiency.");
		panelTop.add(lblTitle);

	}

	/**
	 * Main method, launches the JFrame.
	 * @param args
	 */
	public static void main(String[] args) {
		new FibonacciCalculatorMcGee().setVisible(true);
	}

	/**
	 * Recursive Fibonacci sequence. 
	 * @param sequence
	 * @return
	 */
	public static int fibRecursive(int sequence) {
		if (sequence == 0) {
			return 0;
		}
		if (sequence == 1) {
			return 1;
		}
		return fibRecursive(sequence - 1) + fibRecursive(sequence - 2);
	}

	/**
	 * Iterative Fibonacci sequence. 
	 * @param sequence
	 * @return
	 */
	public static int fibIterative(int iteration) {
		int nsub2 = 0;
		int nsub1 = 1;
		for (int i = 1; i < iteration; i++) {
			int val = nsub2 + nsub1;
			nsub2 = nsub1;
			nsub1 = val;
		}
		if (iteration == 0) {
			return 0;
		}
		return nsub1;
	}

}
