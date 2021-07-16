//Author Name: Daniel McGee
//Date: 7/9/2021
//Program Name: ArraySumMcGee
//Purpose: Implement and test both a parallel and single threaded array sum.

package lib;

import java.util.Random;

public class ArraySumMcGee {
	/**
	 * A private worker class that calls the sumArray method.
	 */
	private static class Worker implements Runnable{
		private int sum;
		private final int workerCount;
		private final int index;
		private final int[] array;
		private boolean done = false;
		public Worker(int[] array, int workerCount, int index) {
			super();
			this.workerCount = workerCount;
			this.index = index;
			this.array = array;
		}
		@Override
		public void run() {
			this.sum = sumArrayHelper(this.array, this.workerCount, this.index);
			this.done = true;
			synchronized(this.array) {
				this.array.notify();
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		int size = 200000000;
		int threads = Math.min(Math.max(Runtime.getRuntime().availableProcessors(), 5),1000);

		System.out.println("Generating random array...");
		int[] randomNums = new int[size];
		Random r = new Random();
		for(int i = 0; i<size; i++) {
			randomNums[i] = r.nextInt(10) + 1;
		}
		System.out.println("Summing array...");
		
		
		long time1 = System.currentTimeMillis();
		int sum1 = sumArray(randomNums,threads);
		time1 = System.currentTimeMillis() - time1;
		System.out.printf("Using %d threads, the multi-thread sum got a sum of %d in %d.%04d seconds.\n",threads,sum1,time1/1000, time1%1000);
		
		
		int sum2 = 0;
		long time2 = System.currentTimeMillis();
		for(int i = 0; i<randomNums.length;i++) {
			sum2 += randomNums[i];
		}
		time2 = System.currentTimeMillis() - time2;
		System.out.printf("The single thread sum got a sum of %d in %d.%04d seconds.\n",sum2,time2/1000, time2%1000);
		
	}
	
	/**
	 * A helper method for the multi-threaded array sum. Adds all values in a specific range calculated from the length of the array, the number of other workers, and the worker index of this worker.
	 * It should be noted that using a simple for loop that jumps 1 index at a time over a range is significantly faster
	 * than using an array that loops over the entire array but jumps workerCount indexes at a time.
	 */
	private static int sumArrayHelper(int[] array, int workerCount, int workerIndex) {
		if(workerIndex>=workerCount) {
			throw new IllegalArgumentException(String.format("workerIndex %d is out of bounds for workerCount %d",workerIndex,workerCount));
		}
		int length = array.length/workerCount;
		int start = workerIndex * length;
		int end = start + length;
		if(workerIndex == workerCount-1) {
			end = array.length;
		}
		int count = 0;
		
		for(int i = start; i < end; i++) {
			count += array[i];
		}
		return count;
	}
	
	/**
	 * A multithreaded array sum method. Uses the array as a lock to communicate with worker threads. 
	 * Spawns workerCount workers. Each worker uses the static sumArrayHelper method to sum a portion of the array.
	 * @param array the array to be summed. Will be used as a lock as well.
	 * @param workerCount The number of workers to be created
	 * @return the sum of the entire array.
	 * @throws InterruptedException
	 */
	public static int sumArray(int[] array, int workerCount) throws InterruptedException {
		Worker workers[] = new Worker[workerCount];
		int count = 0;
		synchronized(array) {
			for(int i=0; i<workerCount; i++) {
				workers[i] = new Worker(array, workerCount, i);
				new Thread(workers[i]).start();
			}
			boolean done = false;
			while(!done) {
				array.wait();
				done = true;
				for(int i=0; i<workerCount; i++) {
					done = done && workers[i].done;
				}
			}
			for(int i=0; i<workerCount; i++) {
				count += workers[i].sum;
			}
		}
		return count;
	}
}
