package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.Scanner;

public class ConcurrentREPL {//Part 1 looks like it's finished, but I'm not 100% sure yet.

	static String currentWorkingDirectory;
	static Thread T1 = null;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		System.out.print(Message.NEWCOMMAND);
		while(true) {
			//obtaining the command from the user
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(!command.trim().equals("")) {//Currently the problem is we dont wait for the line to finish before printing newcommand. 
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				
				
				while(filterlist != null) {//The execution of the command. I believe this is where we .start stuff.
					if(T1 != null) {//This forces the list to run sequentially. It is wrong. 
						try {
							T1.join(1000);
						} catch (InterruptedException e) {
							throw new IllegalStateException();
						}
					}//We have to find a way to join threads if they require input, run all filters concurrently.
					
					Thread T = new Thread(filterlist);
					T.start();
					filterlist = (ConcurrentFilter) filterlist.getNext();
					T1 = T; //The last thread
					
				}
				try {//Waiting for T1 to finish
					T1.join(1000);
				} catch (InterruptedException e) {
					throw new IllegalStateException();
				}
			}	
			System.out.print(Message.NEWCOMMAND); //Moved newcommand to the end
		}
		s.close();
		System.out.print(Message.GOODBYE);
	
	}
}

		


