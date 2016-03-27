package com.apg.bidding;

/**
 * Bid is a separate class associating
 * Auction, User and bidPrice
 *  
 * @author sohil
 *
 */
public class Bid implements Comparable<Bid>{
	private long auctionId;
	private User bidUser;
	private double bidPrice;
	
	public Bid(User bidUser, long auctionId, double bidPrice) {
		super();
		this.auctionId = auctionId;
		this.bidUser = bidUser;
		this.bidPrice = bidPrice;
	}
		
	public long getAuctionId() {
		return auctionId;
	}


	public void setAuctionId(long auctionId) {
		this.auctionId = auctionId;
	}

	public User getBidUser() {
		return bidUser;
	}
	
	public void setBidUser(User bidUser) {
		this.bidUser = bidUser;
	}

	public double getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(double bidPrice) {
		this.bidPrice = bidPrice;
	}

	@Override
	public String toString() {
		return "Bid [bidUser=" + bidUser + ", bidPrice=" + bidPrice + "]";
	}

	@Override
	public int compareTo(Bid o) {
		if (this.bidPrice < o.bidPrice)
		    return -1;
		else if(this.bidPrice > o.bidPrice)
		    return 1;
		
		return 0;
		
	}
	
	
}
