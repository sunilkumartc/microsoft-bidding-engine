package com.apg.bidding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BiddingHandler is a handler class which
 * implements BiddingSystemInterface
 * 
 * @author sotrived
 *
 */
public class BiddingHandler implements BiddingSystemInterface{
	
	private static final String AUCTION_STATE_FILENAME = "auctionState.dat";
	
	
	/**
	 * These counters would be used for removal of old auction from map.
	 * Amount of time for which Auction should run
	 */
	private static final int AUCTION_RUNTIME_AMOUNT = 2;
	
	/**
	 * Field of time which will be effective for auction run
	 */
	private static final int AUCTION_RUNTIME_FIELD = GregorianCalendar.MINUTE;
	
	private static final int TOPBIDDERSLISTSIZE = 5;
	private Long nextAuctionId;
	private Map<Long, Auction> auctionMap;
	//for creating map of bidders and their bid
	private Map<Long, List<Bid>> biddersMap;
	
	public BiddingHandler() {
		super();
		this.nextAuctionId = 0L;
		this.auctionMap = new ConcurrentHashMap<Long, Auction>();
		this.biddersMap = new ConcurrentHashMap<Long, List<Bid>>();
	}

	
	@Override
	public long createAuction(Item item, User owner, String closingTimeStr) {
		// TODO Auto-generated method stub
		Long auctionId=null;
		
		synchronized(this.nextAuctionId){
			auctionId = this.nextAuctionId;
			this.nextAuctionId++;
		}
		
		Date closingTime = null;
		
		try {
			SimpleDateFormat sdf = Util.getDateFormat();
			closingTime = sdf.parse(closingTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			owner.inform("Closing time could not be parsed, please check if you entered proper Date&Time.");
			return -1;
		} 
		

		Auction auction = new Auction(item, closingTime, auctionId, owner);
		System.out.println("Auction created: "+auction.getItemDescriptionForAuction());
		owner.inform(String.format("Auction has been created for item: %s, generated auction id: %d", item.getItemName(), auctionId));
		
		// put auction ids into map
		this.auctionMap.put(auctionId, auction);
		
		return auctionId;
	}

	@Override
	public String getAuctionListAsString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-15s%-20s%-20s%-20s%-20s\n\n", "ID", "Title", "Price", "Closing Time", "isOpen?"));
		
		Calendar cal = new GregorianCalendar();
		for(Entry<Long, Auction> e: this.auctionMap.entrySet()){
			Long id = e.getKey();
			Auction a = e.getValue();
			
			
			// treating older auctions - removing them after checking if their running time
			cal.setTime(a.getClosingTime());
			cal.add(AUCTION_RUNTIME_FIELD, AUCTION_RUNTIME_AMOUNT);
			
			if(new GregorianCalendar().after(cal)){
				System.out.println("Removing auction with key: "+id);
				this.auctionMap.remove(id);
				continue;
			}
			sb.append(a.getSingleLineItemDescription());
		}
		return sb.toString();
	}

	@Override
	public String getAuctionDetails(long auctionId) {
		// TODO Auto-generated method stub
		Auction a = this.auctionMap.get(auctionId);
		if(a == null)
			return null;
		
		return a.getItemDescriptionForAuction();
	}
	

	@Override
	public Boolean placeBid(User bidder, long auctionId, Bid bid) {
		// TODO Auto-generated method stub
		Auction a = this.auctionMap.get(auctionId);
		if(a == null){
			bidder.inform("Wrong auction id: "+auctionId);
			return false;
		}
		
		// creating TopBiddersMap
		List<Bid> bidList;
		if(this.biddersMap.containsKey(auctionId)){
			bidList = this.biddersMap.get(auctionId);
		} else {
			bidList = new ArrayList<Bid>();		
		}
		
		// no bid is added to topList which is lower than last best bid
		if(bid.getBidPrice() > a.getBestBid()){
			bidList.add(bid);
			this.biddersMap.put(auctionId, bidList);
		}
		
		a.placeBid(bidder, bid);
		return true;
	}
	
	/**
	 * Auction state saved by admin
	 */
	@Override
	public Boolean saveAuctionState(User user) {
		// TODO Auto-generated method stub
		if(!user.isAdmin()) {
			user.inform("Operation of saveAuctionState can be performed by ADMIN user only!");
			return false;
		}
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(AUCTION_STATE_FILENAME)));
			oos.writeObject(this.auctionMap);
			oos.writeObject(this.nextAuctionId);
			oos.close();
			System.out.println("State saved to - "+AUCTION_STATE_FILENAME);
			user.inform("State saved.");
			return true;
			
		} catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	
	@Override
	public Boolean restoreAuctionState(User user) {
		// TODO Auto-generated method stub
		if(!user.isAdmin()) {
			user.inform("Operation of saveAuctionState can be performed by ADMIN user only!");
			return false;
		}
		
		try{
			ObjectInputStream ois  = new ObjectInputStream(new BufferedInputStream(new FileInputStream(AUCTION_STATE_FILENAME)));
			this.auctionMap = (Map<Long, Auction>)ois.readObject();
			this.nextAuctionId = (Long)ois.readObject();
			ois.close();
			
		} catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// start auction from their states
		for(Auction auction: auctionMap.values())
			auction.setUpAuctionClosingTime();
		
		System.out.println("State restored from file- "+AUCTION_STATE_FILENAME);
		user.inform("Auction state has been restored.");
		return true;
	}

	@Override
	public String getSimpleDateFormatString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String getTopBidders(long auctionId){
		List<Bid> bidList = this.biddersMap.get(auctionId);
		Collections.sort(bidList, Collections.reverseOrder());
		
		if(bidList.size() >= TOPBIDDERSLISTSIZE)
			return bidList.subList(0, 5).toString();
		
		return bidList.toString();
	}
}
