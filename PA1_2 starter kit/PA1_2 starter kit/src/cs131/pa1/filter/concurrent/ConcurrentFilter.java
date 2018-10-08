package cs131.pa1.filter.concurrent;
import java.util.LinkedList;
import java.util.Queue;

import cs131.pa1.filter.Filter;




public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected Queue<String> input;
	protected Queue<String> output;
	protected boolean aliveCheck = false; //New variable for isDone check.
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedList<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public Filter getNext() {
		return next;
	}
	
	public void process(){
		while (!input.isEmpty()){
			String line = input.poll();
			String processedLine = processLine(line);
			if (processedLine != null){
				output.add(processedLine);
			}
		}	
	}
	
	@Override
	public boolean isDone() {
		//Old code was return input.size() == 0;
		//return this.aliveCheck;//New code
		return input.size() == 0;
	}
	
	protected abstract String processLine(String line);
	
	
	
	
	
	
	//New stuff
	@Override //Not sure how much should be in here but this method is extremely important. I know it has to call process at some point.
	public void run() {//Run contains all code that should be executed by the thread. This is the best I've got for now.
		process();
		
		aliveCheck = true;//We just want our run to begin executing the code the filter represents.
		//Don't call run directly. To start threads make a thread with Thread t = new Thread(FilterOfTheProperType())
		//t.start(); will get the thread going.
	}
	

	
	
	
	
}
