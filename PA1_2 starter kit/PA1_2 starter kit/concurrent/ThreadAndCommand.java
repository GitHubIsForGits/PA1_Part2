public class ThreadAndCommand{
	private Thread tee;
	private string com;
	
	ThreadAndCommand(Thread T, String C){
		this.tee = T;
		this.com = C;
		return this;
	}
	
	public Thread getT(){//Return the thread
		return tee;
	}
	
	public Thread toString(){//Return the command
		return com;
	}
}
