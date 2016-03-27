package com.apg.bidding.clientStrategy;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.apg.bidding.Bid;
import com.apg.bidding.BiddingHandler;
import com.apg.bidding.User;

/**
 * place bid operation class
 * 
 * @author sohil
 *
 */
public class PlaceBid implements ClientOperationInterface {

	@Override
	public void execute(Scanner sc, User testUser) {
		
		try {
			BiddingHandler bh = BiddingHandlerSingleton.getInstance();
			
			System.out.print("Insert auction id for which you want to place bid\n >>>: ");
			long auctionId = sc.nextLong();
			
			System.out.print("Insert your bid amount\n >>>: ");
			double bidPrice = sc.nextDouble();
			
			// creating bid
			Bid bid = new Bid(testUser, auctionId, bidPrice);
			
			//placing bid
			bh.placeBid(testUser, auctionId, bid);
		} catch (InputMismatchException ime) {
			System.err.println("Err - Wrong Input!");
		}
		
	}

}
