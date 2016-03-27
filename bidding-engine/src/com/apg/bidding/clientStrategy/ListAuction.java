package com.apg.bidding.clientStrategy;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.apg.bidding.BiddingHandler;
import com.apg.bidding.User;

/**
 * List Auction operation class
 * @author sohil
 *
 */
public class ListAuction implements ClientOperationInterface {

	@Override
	public void execute(Scanner sc, User testUser) {
		try {
			BiddingHandler bh = BiddingHandlerSingleton.getInstance();
			System.out.println(bh.getAuctionListAsString());
			
		} catch (InputMismatchException ime) {
			System.err.println("Input Mismatch!");
		}
		
	}

}
