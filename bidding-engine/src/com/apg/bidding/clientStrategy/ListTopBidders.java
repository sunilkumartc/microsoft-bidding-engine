package com.apg.bidding.clientStrategy;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.apg.bidding.BiddingHandler;
import com.apg.bidding.User;

/**
 * List top bidders operation class
 * @author sohil
 *
 */
public class ListTopBidders implements ClientOperationInterface {

	@Override
	public void execute(Scanner sc, User testUser) {
		try {
		BiddingHandler bh = BiddingHandlerSingleton.getInstance();
		
		System.out.print("Insert auction id to list top 5 bidders\n >>>: ");
		long auctionId = sc.nextLong();
		
		System.out.println(bh.getTopBidders(auctionId));
		
		} catch (InputMismatchException ime) {
			System.err.println("Input Mismatch!");
		}
	}

}
