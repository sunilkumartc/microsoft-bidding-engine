package com.apg.bidding;

import java.util.UUID;

/**
 * User class for handling all the users
 * associated with Bidding Engine
 * 
 * @author sohil
 *
 */
public class User {

	private UUID uuid;
	private String name;
	private boolean isAdmin;
	
	public User(String name, boolean isAdmin) {
		this.name = name;
		this.uuid = UUID.randomUUID();
		this.isAdmin = isAdmin;
	}

	public String getId() {
		return uuid.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public void inform(String info) {
		System.out.println("Information: "+ info);
	}

	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}
	
	
}
