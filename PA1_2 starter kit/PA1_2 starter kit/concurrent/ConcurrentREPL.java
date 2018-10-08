package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrentREPL {//Part 1 looks like it's finished, but I'm not 100% sure yet.

	static String currentWorkingDirectory;
	static Thread T1 = null;
	static LinkedBlockingQueue<Thread> stillRunnin; //List of still running threads
	
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
			} 
			
			
			
			//Part 2 stuff
			else if(command.equals("repl_jobs")) {
				if (!stillRunnin.isEmpty()) {
					for (Thread t: stillRunnin) {
					
						if(!t.isAlive()) {
							stillRunnin.remove(t);
						}
						else if(t.isAlive()) {
							System.out.println(t.toString());
						}
					}
					
				}	
			} 
			else if(command.startsWith("kill")) {
				String[] nee = command.split(" ");
				char[] tred = nee[1].toCharArray();
				if (tred.length == 1) {
					int i = Character.getNumericValue(tred[0]);
					//Make the code to kill the thread in position i
				}
			} 
			else if(command.endsWith("&")) {
				String[] noAmp = command.split("&");
				String commandCut = noAmp[0].trim();
				if(!commandCut.equals("")) {
					ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(commandCut);
					while(filterlist != null) {//The execution of the command. I believe this is where we .start stuff.
						Thread T = new Thread(filterlist);
						T.start();
						filterlist = (ConcurrentFilter) filterlist.getNext();
						T1 = T; //The last thread	
					}
					
				}
				
				
			}
			//End of part 2 stuff
			
			
			
			
			
			
			
			else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				
				
				while(filterlist != null) {//The execution of the command. I believe this is where we .start stuff.
					
					Thread T = new Thread(filterlist);
					T.start();
					filterlist = (ConcurrentFilter) filterlist.getNext();
					T1 = T; //The last thread
					
				} 
				
				try {//Waiting for T1 to finish, so the carrot doesnt get printed over
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

		


