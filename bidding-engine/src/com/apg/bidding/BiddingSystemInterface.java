package com.apg.bidding;
/**
 * Bidding System Interface
 * 
 * @author sohil
 *
 */
public interface BiddingSystemInterface {
	/**
	 * Creates auction for item owner and returns unique auction id.
	 * 
	 * @param item - Item object
	 * @param owner - User Object
	 * @param closingTimeStr - date and time string to close Auction
	 * @return Auction id
	 */
	public long createAuction(Item item, User owner, String closingTimeStr);
	
	
	/**
	 * This gets list of auctions as String
	 * @return String - list of auctions
	 */
	public String getAuctionListAsString();
	
	
	/**
	 * This gets details of auction as String
	 * 
	 * @param auctionId - for which details are required
	 * @return String - with details for particular auction id
	 */
	public String getAuctionDetails(long auctionId);
	
	
	/**
	 * Place bid for particular auction id
	 * 
	 * @param bidder - User object placing bid
	 * @param auctionId - auction id for which bid is placed
	 * @param bid - bid amount OR have it as 'Bid' object i.e. public Boolean placeBid(User bidder, long auctionId, double bid);
	 * @return - true if there are no exception else false
	 */
	public Boolean placeBid(User bidder, long auctionId, Bid bid);
	
	
	/**
	 * Saves current state of system
	 * @param user - can only be done by User object with admin privileges
	 * @return - true for success else false
	 */
	public Boolean saveAuctionState(User user);
	
	
	/**
	 * Restores state of the system
	 * @param user - can only be done with admin privileges
	 * @return - true for success else false
	 */
	public Boolean restoreAuctionState(User user);
	
	
	/**
	 * Gets Java SimpleDateFormat String 
	 * 
	 * @return - Java SimpleDateFormat String
	 */
	public String getSimpleDateFormatString();
	
}
