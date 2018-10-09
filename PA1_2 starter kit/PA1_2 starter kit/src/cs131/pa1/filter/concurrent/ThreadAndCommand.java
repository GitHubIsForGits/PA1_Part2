// New class created so that repl_jobs can print out the command associated with each thread
package cs131.pa1.filter.concurrent;

public class ThreadAndCommand{
	private Thread tee;
	private String com;
	
	ThreadAndCommand(Thread T, String C){
		this.tee = T;
		this.com = C;
	}
	
	public Thread getT(){//Return the thread
		return tee;
	}
	
	public String toString(){//Return the command
		return com;
	}
}
