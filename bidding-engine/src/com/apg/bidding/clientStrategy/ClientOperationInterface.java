package com.apg.bidding.clientStrategy;

import java.util.Scanner;

import com.apg.bidding.User;

/**
 * Strategy interface to facilitate various client operation
 * @author sohil
 *
 */
public interface ClientOperationInterface {
	
	public void execute(Scanner sc, User testUser);
}
