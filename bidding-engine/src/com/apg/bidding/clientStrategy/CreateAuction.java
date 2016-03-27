package com.apg.bidding.clientStrategy;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.apg.bidding.BiddingHandler;
import com.apg.bidding.Item;
import com.apg.bidding.User;
import com.apg.bidding.Util;

/**
 * CreateAuction operation class
 * 
 * @author sohil
 *
 */
public class CreateAuction implements ClientOperationInterface {
	
	private String simpleDateFormatPattern = Util.DATE_FORMAT;
	@Override
	public void execute(Scanner sc, User testUser) {
		try {

			System.out.print("Insert Item name\n >>>: ");
			String itemName = sc.next();
			
			System.out.print("Insert Item description\n >>>: ");
			String itemDesc = sc.next();
			
			System.out.print("Insert starting price\n >>>: ");
			Double startPrice = sc.nextDouble();
			
			System.out.print("Insert closing time for auction in format "+simpleDateFormatPattern+" (e.g. 26-01-2016T21:39)\n >>>: ");
			String closingTimeStr = sc.next();
			
			//create item
			Item item = new Item(itemName, itemDesc, startPrice);
			
			//Create auction
			BiddingHandler bh = BiddingHandlerSingleton.getInstance();
			long auctionId = bh.createAuction(item, testUser, closingTimeStr);
			
			if(auctionId == -1){
				System.out.println("err: Auction could not be created!");
			} else {
				System.out.println(String.format("Auction created with id: %d", auctionId));
			}
		} catch(InputMismatchException ime) {
			System.err.println("Input mismatch!");
		}
		
	}

}
