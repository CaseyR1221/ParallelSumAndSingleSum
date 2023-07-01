package concurrency;

public class SumParallelTasks implements Runnable {
	private final int[] array;
    private final int start;
    private final int end;
    private long partialSum;

    public SumParallelTasks(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    public long getPartialSum() {
        return partialSum;
    }

    @Override
    public void run() {
        partialSum = 0;
        
        for (int i = start; i < end; i++) {
        	
            partialSum += array[i];
            
        }
    }
}
