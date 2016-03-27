package com.apg.bidding;

/**
 * Item class for all things item
 * 
 * @author sohil
 *
 */
public class Item {
	private String itemName;
	private String itemDescription;
	private double startingPrice;
	
	public Item(String itemName, String itemDescription, double startingPrice) {
		super();
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.startingPrice = startingPrice;
	}
	
	public Item() {
		super();
		this.itemName = null;
		this.itemDescription = null;
		this.startingPrice = 0;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	/**
	 * getting item description
	 * @return
	 */
	public String getItemDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Item Title: %s\n",this.itemName))
		  .append(String.format("Item Description: %s\n",this.itemDescription))
		  .append(String.format("Item starting price: %.2fRs\n",this.startingPrice));
		
		return sb.toString();
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(double startingPrice) {
		this.startingPrice = startingPrice;
	}

	
}
