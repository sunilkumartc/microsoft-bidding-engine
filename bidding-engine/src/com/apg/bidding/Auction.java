package com.apg.bidding;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Auction class is responsible for handling 
 * all operations related to auction event
 * 
 * @author sohil
 *
 */
public class Auction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515565488602883736L;
	
	private double bestBid;
	private boolean isAuctionOn;
	private long auctionId;
	
	private User owner;
	private User highestBidder;
	private String highestBidderName;	
	private Map<String, User> biddersMap;
	
	private Item item;
	
	private Date closingTime;

	public Auction(Item item, Date closingTime, long auctionId, User owner) {
		super();
		this.item = item;
		this.bestBid = this.item.getStartingPrice();
		this.closingTime = closingTime;
		this.auctionId = auctionId;
		this.owner = owner;
		this.biddersMap = new HashMap<String, User>();
		
		this.setUpAuctionClosingTime();
		
		if(!isAuctionOn)
			try{
				owner.inform("Old date entered...");
			} catch(Exception e){
				e.printStackTrace();
			}
		
	}

	public Auction() {
		super();
		this.item = null;
		this.closingTime = null;
		this.auctionId = 0;
		this.owner = null;
	}
	
	
	public synchronized Date getClosingTime() {
		return this.closingTime;
	}
	
	/**
	 * Gets complete details of Auction Item
	 * @return
	 */
	public synchronized String getItemDescriptionForAuction() {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = Util.getDateFormat();
		sb.append(this.item.getItemDescription())
		  .append(String.format("Item auctionId: %d\n",this.auctionId))
		  .append(String.format("Number of bidders till now: %d\n", this.biddersMap.size()))
		  .append(String.format("Auction closing time(IST): %s\n\n", sdf.format(this.closingTime)))
		  .append(String.format("Auction status: %s\n\n", (this.isAuctionOn() ? "OPEN" : "CLOSED")))
		  .append(this.getResult());
		return sb.toString();
	}
	
	public synchronized String getSingleLineItemDescription(){
		String closingTimeString = Util.getDateFormat().format(closingTime);
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-15d%-20s%-20.2f%-20s%-10s\n", this.auctionId, this.item.getItemName(), this.item.getStartingPrice(),
				closingTimeString, Boolean.toString(this.isAuctionOn)));
		return sb.toString();
	}
	private String getResult(){
		if(this.highestBidder == null)
			return "No higher Bid.";
		
		return String.format("Auction has been won by bidder- %s for bid of: %.2f\n", this.highestBidderName, this.bestBid);
	}
	
	/**
	 * Responsible for placing bid by Bidder
	 * @param bidder
	 * @param bid
	 */
	public synchronized void placeBid(User bidder, Bid bid) {
		try{
			if(!this.isAuctionOn()) {
				bidder.inform("Aunction is closed. Later buddy...");
				return;
			}
			
			String auctionId = bidder.getId();
			this.biddersMap.put(auctionId, bidder);
			
			// not allowing any bid which is lower than last best bid
			if(bid.getBidPrice() <= this.bestBid){
				bidder.inform("Your bid is not high enough!");
				return;
			}
			
			this.highestBidder = bidder;
			this.highestBidderName = bidder.getName();
			this.bestBid = bid.getBidPrice();
			
			bidder.inform("Congo, your bid is accepted!");
			
			//inform all other bidders
			for(Entry<String, User> e: this.biddersMap.entrySet()){
				User restBidder = e.getValue();
				//inform others - upgrade or loose
				restBidder.inform(String.format("TO ALL BIDDERS: New bid of: %.2fRs is placed on Auction ID - %d", this.bestBid, bid.getAuctionId()));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * setting Auction closing time
	 */
	public synchronized void setUpAuctionClosingTime(){
		if(this.closingTime.before(new Date())){
			this.isAuctionOn = false;
		} else {
			this.isAuctionOn = true;
			
			(new Timer(true)).schedule(new TimerTask(){
				public void run(){
					System.out.println("Auction timer started!");
					Auction.this.closeAuction();
				}
			}, closingTime);
		}
		System.out.println("Auction timer set!");
	}
	
	/**
	 * closing auction with specific "auctionId"
	 */
	public synchronized void closeAuction(){
		System.out.println("Closing the auction with auctionId : "+this.auctionId);
		this.isAuctionOn = false;
		
		try{
			if(this.highestBidder == null){
				System.out.println("No bidders for auction : "+this.auctionId);
				//inform bid owner
				this.owner.inform(String.format("No one matched the value for auction item - %s", this.item.getItemName()));
				
				//inform bidders
				for(User bidder : this.biddersMap.values()){
					bidder.inform(String.format("Auction for item - %s is closed, no one matched starting price of: %.2fRs", this.item.getItemName(),
							this.item.getStartingPrice()));
				}
			} else {
				System.out.println(String.format("Item - %s has been sold out!!!", this.item.getItemName()));
				//inform owner, u got that sold out
				this.owner.inform(String.format("TO OWNER: Your item - %s has been sold to bidder - %s at bid of: %.2fRs", this.item.getItemName(),
							this.highestBidderName, this.bestBid));
				
				//inform all other
				for(Entry<String, User> e: this.biddersMap.entrySet()){
					User bidder = e.getValue();
					if(bidder == this.highestBidder){
						//inform bid winner - congo
						bidder.inform(String.format("TO BIDDER: Congratulations! your bid of: %.2fRs has won the auction for item - %s", this.bestBid,
								this.item.getItemName()));
					} else {
						//inform others - bad luck
						bidder.inform(String.format("TO ALL BIDDERS: Auction item - %s has been won by %s for bid of: %.2fRs", this.item.getItemName(),
								this.highestBidderName, this.bestBid));
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public boolean isAuctionOn() {
		return isAuctionOn;
	}
	
	public double getBestBid() {
		return bestBid;
	}
	
}
