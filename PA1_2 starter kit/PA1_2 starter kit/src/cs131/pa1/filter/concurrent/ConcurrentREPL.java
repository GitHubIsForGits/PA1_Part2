package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import java.util.Scanner;
import java.util.*;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	static Thread T1 = null;
	static TreeMap <Integer, ThreadAndCommand> stillRunnin = new TreeMap<Integer, ThreadAndCommand>(); //Map of all threads in each command running with indexes as keys //Changed so it only accepts a single thread (the last thread which when finished means the command is finished)
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
			
			
			
			else if(command.trim().equals("repl_jobs")) {
				if(stillRunnin.size() == 0) {
					
				} else if(!stillRunnin.isEmpty()) { // Create an iterator to avoid ConcurrentModificationException
					Iterator<Map.Entry<Integer, ThreadAndCommand>> entries = stillRunnin.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<Integer, ThreadAndCommand> entry = entries.next();
						entries.remove();
						if(entry == null) { //Handles case with an empty map
							continue;
						}
						int k = entry.getKey();
						ThreadAndCommand tNc = entry.getValue();
						if(!tNc.getT().isAlive()) {//Removes processes from map if they are completed
							stillRunnin.remove(k);
						} else if(tNc.getT().isAlive()){
							System.out.println("	"+k +". "+ tNc.toString()+" &"); //Prints ou the command
						}
					}	
				}
				
				
			}
			else if(command.startsWith("kill")) {
				String[] nee = command.split(" ");
				if (nee.length == 1) {// Checks that a parameter was given 
					System.out.print(Message.REQUIRES_PARAMETER.with_parameter("kill"));
					System.out.print(Message.NEWCOMMAND.toString());
					continue;
				}	
				String tred = nee[1];
				if (!tred.matches("[0-9]+")){// Checks that the parameter is a number
					System.out.print(Message.INVALID_PARAMETER.with_parameter(command));
					System.out.print(Message.NEWCOMMAND.toString());
					continue;
				}			
				int target = Integer.parseInt(tred);
				ThreadAndCommand oof = stillRunnin.get(target);
				if(!(oof == null)) {
					if(oof.getT().isAlive()) {
						oof.getT().interrupt();
						stillRunnin.remove(target);
					} 
				}
				
			}
			else if(command.endsWith("&")) {
				
				String[] noAmp = command.split("&");
				String commandCut = noAmp[0].trim();
				//I changed this area so we are only passing the last thread of the command, so when that one is all finished it should be done.
				if(!commandCut.equals("")) {
					ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(commandCut);
					ThreadAndCommand thisGuy = null;
					while(filterlist != null) {
						Thread T = new Thread(filterlist);
						T.start();
						thisGuy = new ThreadAndCommand(T, commandCut);
						filterlist = (ConcurrentFilter) filterlist.getNext();
						T1 = T; //The last thread
					}
					stillRunnin.put(mapIndex, thisGuy); //Changed
					mapIndex++;
				}
				
				
			}
			else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				
				
				while(filterlist != null) {//The execution of the command. I believe this is where we .start stuff.
					
					Thread T = new Thread(filterlist);
					T.start();
					filterlist = (ConcurrentFilter) filterlist.getNext();
					T1 = T; //The last thread
					
				} 
				if (T1 != null) {
					try {//Waiting for T1 to finish, so the carrot doesnt get printed over
						T1.join(1000);
					} catch (InterruptedException e) {
						throw new IllegalStateException();
					}
				}
			}
			
			System.out.print(Message.NEWCOMMAND); //Moved newcommand to the end
		}
		s.close();
		System.out.print(Message.GOODBYE);
	
	}
}
	

	
	



		


