package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.Scanner;
import java.util.*;

public class ConcurrentREPL {//Part 1 looks like it's finished, but I'm not 100% sure yet.

	static String currentWorkingDirectory;
	static Thread T1 = null;
	static TreeMap <Integer, ThreadAndCommand> stillRunnin = null; //Map of all threads in each command running with indexes as keys //Changed so it only accepts a single thread (the last thread which when finished means the command is finished)
	static int mapIndex = 1;//Increments as more background processes are added
	
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
			else if(command.trim().equals("repl_jobs")) {
				System.out.println("Enterred repl_jobs");
				if(!stillRunnin.isEmpty()) {
					while (Map.Entry<Integer, ThreadAndCommand> entry: stillRunnin.entrySet()) {
						int k = entry.getKey();
						ThreadAndCommand tNc = entry.getValue();
						if(!tNc.getT().isAlive()) {
							stillRunnin.remove(k);
						} else if(tNc.getT().isAlive()){
							System.out.println(k +". "+ tNc.toString());//Need a way to print the exact command
						}
					}	
				}
				
				
			}
			else if(command.startsWith("kill")) {
				String[] nee = command.split(" ");
				if(nee.length == 2) {				
					String tred = nee[1];
					int target = Integer.parseInt(tred);
					ThreadAndCommand oof = stillRunnin.get(target);
					if(oof.getT().isAlive()) {//I check for alive here, I think thats right.
						oof.getT().interrupt();
						stillRunnin.remove(target);
					}
				} else {
					//Dunno what to do here. I dont think anything.
				}
	
			} 
			else if(command.endsWith("&")) {
				System.out.println("Made a time delay command");
				
				String[] noAmp = command.split("&");
				String commandCut = noAmp[0].trim();
				//LinkedList<Thread> threads = new LinkedList<Thread>();//List of all threads in command
				//I changed this area so we are only passing the last thread of the command, so when that one is all finished it should be done.
				if(!commandCut.equals("")) {
					ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(commandCut);
					while(filterlist != null) {
						Thread T = new Thread(filterlist);
						T.start();
						ThreadAndCommand thisGuy = new ThreadAndCommand(T, command);
						filterlist = (ConcurrentFilter) filterlist.getNext();
						T1 = T; //The last thread	
					}
					stillRunnin.put(mapIndex, thisGuy); //Changed
					mapIndex++;
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
	

	
	



		


