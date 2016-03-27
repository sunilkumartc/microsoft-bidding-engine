package com.apg.bidding;

import java.util.Scanner;

import com.apg.bidding.clientStrategy.ClientOperationInterface;
import com.apg.bidding.clientStrategy.ClientOperationParser;

/**
 * This program runs complete auction system
 * @author sohil
 *
 */
public class RunAuction {
	private static boolean ISADMIN = true;
	
	private static String getCommandList(){
		StringBuilder sb = new StringBuilder();
		sb.append("help - All commands\n")
		.append("create - create an auction\n")
		.append("placebid - place bid for auction\n")
		.append("list - list of all available auction\n")
		.append("listtopbidders - get list of top 5 bidders by price\n")
		.append("quit - quit the system\n\n");
		
		return sb.toString();
	}
	
	
	public static void main(String[] args){
		
		// create user
		User testUser = new User("sam", ISADMIN);
						
		Scanner sc = new Scanner(System.in);
		System.out.println("Auction System\n");
		System.out.println("Type \"help\" for displaying all commands");
		

		while(true){
			System.out.print(">> ");
			
			String operation = null;
			
			if(!sc.hasNextLine()){
				sc.nextLine();
			}
			
			operation = sc.nextLine();
			try { 
				if(operation.startsWith("help")) {
					System.out.println(getCommandList());
				} else if(operation.startsWith("quit")) {
					sc.close();
					System.exit(0);				
				} else if(operation.length() >= 3){
					//replaced with strategy
					try {
						ClientOperationInterface coi = new ClientOperationParser(operation).toOperation();
						coi.execute(sc, testUser);
					} catch(Exception e){
						System.out.println("Wrong command, type \"help\" for list of commands.");
						//e.printStackTrace();
					}
				} else {
					System.err.println("Wrong command, type \"help\" for list of commands.");
				}
			} catch (Exception e){				
				System.out.println("Exception while performing operation...");
				e.printStackTrace();
				
			}

		}
	}


}
