package lib;

//Author Name: Daniel McGee
//Date: 6/18/2021
//Program Name: ChartMcGee
//Purpose: Low Latency rendering of runtime efficiency data.

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ChartMcGee extends JComponent {

	private static final long serialVersionUID = 5220811827758029625L;
	private final ArrayList<Long> timesIterative = new ArrayList<>();
	private final ArrayList<Long> timesRecursive = new ArrayList<>();

	private final ArrayList<Long> iterativeQueue = new ArrayList<>();
	private final ArrayList<Long> recursiveQueue = new ArrayList<>();

	private long largestTime = 0;

	private Timer t;
	
	
	ChartMcGee() {
		//Timer periodically checks for updated data.
		t = new Timer(100, (e) -> {
			this.checkQueues();
			t.restart();
		});
		t.setInitialDelay(100);
		t.start();
	}

	/**
	 * A thread safe reset method. Clears the queues using synchronized blocks so that the clearing does not interrupt ongoing reading or writing.
	 * This method should only be called after worker threads putting data into the graph have been shut down, and before new ones are started.
	 */
	void reset() {
		synchronized (iterativeQueue) {
			iterativeQueue.clear();
		}
		synchronized (recursiveQueue) {
			recursiveQueue.clear();
		}
		timesIterative.clear();
		timesRecursive.clear();
		largestTime = 0;
	}

	/**
	 * Checks the data queues and adds them to the main lists, marking the component for repainting if any data is added. 
	 * This method should only be called from the Swing Event Dispatch thread, as it is not thread safe with the paintComponent method. 
	 */
	private void checkQueues() {
		boolean dirty = false;
		
		synchronized (iterativeQueue) {
			for (Long l : this.iterativeQueue) {
				timesIterative.add(l);
				if (l > largestTime) {
					largestTime = l;
				}
				dirty = true;
			}
			iterativeQueue.clear();
		}
		
		synchronized (recursiveQueue) {
			for (Long l : this.recursiveQueue) {
				timesRecursive.add(l);
				if (l > largestTime) {
					largestTime = l;
				}
				dirty = true;
			}
			recursiveQueue.clear();
		}

		if (dirty) {
			SwingUtilities.windowForComponent(this).repaint();
		}
	}

	/**
	 * Queues another measure of the time taken by the iterative function. Every 100 ms the queue is emptied and put onto the graph in the order it was added.
	 * This method can be called from any thread without effecting the main draw function.
	 * @param time how long it took to calculate the latest number in the sequence.
	 */
	public void addIterativeStep(Long time) {
		synchronized (iterativeQueue) {
			iterativeQueue.add(time);
		}
	}

	/**
	 * Queues another measure of the time taken by the recursive function. Every 100 ms the queue is emptied and put onto the graph in the order it was added.
	 * This method can be called from any thread without effecting the main draw function.
	 * @param time how long it took to calculate the latest number in the sequence.
	 */
	public void addRecursiveStep(Long time) {
		synchronized (recursiveQueue) {
			recursiveQueue.add(time);
		}
	}

	/**
	 * Draws the graph.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Setting up constants
		int markerWidth = 10;
		int markerStart = 3;

		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int fHeight = metrics.getHeight() + 4;
		Graphics2D graph = (Graphics2D) g;
		
		int height = this.getHeight();
		int width = this.getWidth();

		//cWidth and cHeight are used to keep track of where we are on the plane. They are incremented every time we move
		int cWidth = 0;
		int cHeight = height;

		
		CenterString(graph, fHeight / 2, height / 2, "Time in Nanoseconds", -90);
		cWidth += fHeight;

		
		//Drawing the y axis. First we figure out how long (in pixels) the label on the largest time value is, then we move
		//the width pointer that far over as all our labels will be right aligned to the width pointer. Then we split the
		//available height into even intervals and draw labels plus markers at each interval. Finally we draw the y axis line.
		long maxTime = largestTime;
		cWidth += metrics.stringWidth("" + maxTime) * 3 / 2;
		
		int timeCount = Math.max(2, (height - fHeight * 3 - markerWidth - fHeight / 2) / fHeight);
		int drawHeight = height - fHeight * 4 - markerWidth;

		for (int i = 0; i < timeCount; i++) {
			RightAlignString(graph, cWidth, drawHeight, "" + (maxTime * i / (timeCount - 1)));
			graph.drawLine(cWidth + markerStart, drawHeight, cWidth + markerWidth, drawHeight);
			drawHeight -= fHeight;
		}
		cWidth += markerWidth;
		
		graph.drawLine(cWidth, height - fHeight * 4 - markerWidth, cWidth, drawHeight + fHeight);

		
		//Drawing the bottom labels
		graph.setFont(graph.getFont().deriveFont(Font.BOLD));
		
		graph.setColor(Color.red);
		CenterString(graph, width / 3, cHeight - fHeight, "Iterative", 0);

		graph.setColor(Color.green);
		CenterString(graph, 2 * width / 3, cHeight - fHeight, "Recursive", 0);
		
		cHeight -= fHeight * 2;
		
		graph.setFont(graph.getFont().deriveFont(Font.PLAIN));
		graph.setColor(Color.black);
		CenterString(graph, width / 2, cHeight - fHeight / 2, "Size of Sequence", 0);
		
		cHeight -= fHeight;

		
		//Drawing the x axis and the data lines. First we divide the available space up into even intervals. 
		//We try to have a marker for each sequence, but if there is not enough room we will omit some. 
		//First we calculate the width of the largest number label, then use that to divide up the space.
		//If we end up with more intervals than numbers, we get rid of the extra intervals.
		int maxNum = Math.max(0, Math.max(timesIterative.size(), timesRecursive.size()) - 1);
		int numWidth = metrics.stringWidth("" + maxNum) * 2;
		int numCount = Math.max(2, (width - cWidth) / numWidth);
		
		if (numCount > maxNum) {
			numCount = Math.max(1, maxNum);
		}
		//Recalculate width even if numCount didn't change as it allows us to utilize a bit of extra space in certain cases
		numWidth = (width - cWidth - numWidth / 2) / (numCount);

		
		//setting up constants
		int numHeight = cHeight - fHeight / 2;
		int markerHeightStart = cHeight - fHeight - markerStart;
		int markerHeightEnd = cHeight - fHeight - markerWidth;
		int graphStartX = cWidth;
		
		
		int lastValueIterative = -1;
		int lastValueRecursive = -1;
		
		for (int i = 0; i <= numCount; i++) {
			//Drawing each label and its marker
			int num = (maxNum * i / (numCount));
			CenterString(graph, cWidth, numHeight, "" + num, 0);
			graph.drawLine(cWidth, markerHeightStart, cWidth, markerHeightEnd);

			//For each set of data, we check to see if it has a value for the current interval. If it does, we draw a line
			//between it and the value at the previous interval. 
			if (timesIterative.size() > num) {
				int h2 = (int) (1.0 * timesIterative.get(num) / (1.0 * maxTime) * fHeight * (timeCount - 1));
				if (lastValueIterative != -1) {
					graph.setColor(Color.red);
					graph.setStroke(new BasicStroke(4));
					graph.drawLine(cWidth - numWidth, markerHeightEnd - lastValueIterative, cWidth,
							markerHeightEnd - h2);
					graph.setColor(Color.black);
					graph.setStroke(new BasicStroke(1));
				}
				lastValueIterative = h2;
			}
			if (timesRecursive.size() > num) {
				int h2 = (int) (1.0 * timesRecursive.get(num) / (1.0 * maxTime) * fHeight * (timeCount - 1));
				if (lastValueRecursive != -1) {
					graph.setColor(Color.green);
					graph.setStroke(new BasicStroke(4));
					graph.drawLine(cWidth - numWidth, markerHeightEnd - lastValueRecursive, cWidth,
							markerHeightEnd - h2);
					graph.setColor(Color.black);
					graph.setStroke(new BasicStroke(1));
				}
				lastValueRecursive = h2;
			}

			cWidth += numWidth;
		}
		//draw the x axis itself and dispose of the graphics object. 
		graph.drawLine(graphStartX, cHeight - fHeight - markerWidth, cWidth - numWidth,
				cHeight - fHeight - markerWidth);
		graph.dispose();

	}

	/**
	 * Draws the text centered on x and y rotated with the rotation provided in
	 * degrees.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param text
	 * @param rotation
	 */
	private static void CenterString(Graphics2D g, int x, int y, String text, double rotation) {
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int offX = metrics.stringWidth(text) / 2;
		int offY = metrics.getAscent() / 3;
		AffineTransform t = g.getTransform();
		g.translate(x, y);
		g.rotate(Math.PI * rotation / 180);
		g.drawString(text, -offX, offY);
		g.setTransform(t);

	}

	/**
	 * Draws the text aligned so that the center of the text is on y and the
	 * rightmost part of the text ends at x.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param text
	 * @param rotation
	 */
	private static void RightAlignString(Graphics2D g, int x, int y, String text) {
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int offX = metrics.stringWidth(text);
		int offY = metrics.getAscent() / 3;
		g.drawString(text, x - offX, y + offY);

	}
}