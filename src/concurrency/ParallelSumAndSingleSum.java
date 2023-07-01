package concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelSumAndSingleSum {
	private static final int ARRAY_SIZE = 200000000;
    private static final int AVAILABLE_THREADS = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws InterruptedException {
		// make an array that can hold up to the amount of values stated by the ARRAY_SIZE variable
        int[] array = makeArray(ARRAY_SIZE);
        
        // what size chunks the array can be divided into based on the number of usable threads
        int chunkSize = ARRAY_SIZE / AVAILABLE_THREADS;
        
        // sum the values of the array using a single thread
        long startTime = System.currentTimeMillis();
        long singleThreadSum = calculateSingleSum(array);
        long endTime = System.currentTimeMillis();
        
        // calculate the time that the single thread method took
        long singleThreadTime = endTime - startTime;

        // sum the values of the array using multiple threads
        startTime = System.currentTimeMillis();
        long parallelThreadSum = calculateParallelSum(array);
        endTime = System.currentTimeMillis();
        
        // calculate the time that the parallel threads method took
        long parallelThreadTime = endTime - startTime;
        
        // calculate the difference between the time taken by the parallel threads method and the single thread method
        long timeDifference = singleThreadTime - parallelThreadTime;
        
        System.out.println("There Are " + AVAILABLE_THREADS + " Threads Available For Use.");
        System.out.println("");
        System.out.println("Breaking The Array Into Chunks Of " + chunkSize + ".");
        System.out.println("");
        System.out.println("The Sum Calculated Using The Single Thread Method is: " + singleThreadSum);
        System.out.println("");
        System.out.println("The Sum Calculated Using The Parallel Thread Method is: " + parallelThreadSum);
        System.out.println("");
        System.out.println("The Single Thread Took: " + singleThreadTime + " ms to run.");
        System.out.println("");
        System.out.println("The Parallel Threads Took: " + parallelThreadTime + " ms to run.");
        System.out.println("");
        System.out.println("The Parallel Method Was " + timeDifference + " ms faster.");
	}
	
	public static int[] makeArray(int size) {
        Random random = new Random();       
        int[] array = new int[size];
        
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(10) + 1;
        }
        
        return array;
    }
	
	public static long calculateSingleSum(int[] array) {
        long sum = 0;
        
        for (int num : array) {
        	
            sum += num;
            
        }
        
        return sum;
    }

    public static long calculateParallelSum(int[] array) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(AVAILABLE_THREADS );
        int chunkSize = array.length / AVAILABLE_THREADS ;
        SumParallelTasks[] tasks = new SumParallelTasks[AVAILABLE_THREADS];
        
        // divide the array into chunks based on the number of usable threads and submit it to the executor task pool to be ran
        for (int i = 0; i < AVAILABLE_THREADS ; i++) {
            int start = i * chunkSize;
            
            int end = (i == AVAILABLE_THREADS - 1) ? array.length : start + chunkSize;
            
            tasks[i] = new SumParallelTasks(array, start, end);
            
            executorService.submit(tasks[i]);
        }
        
        // wait to terminate the executor until all tasks in the pool are ran, then terminate.
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        long sum = 0;
        
        // get all the partial sums from the tasks and add them together
        for (SumParallelTasks task : tasks) {
        	
            sum += task.getPartialSum();
            
        }
        
        return sum;
    }
    
}
