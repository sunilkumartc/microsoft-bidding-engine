package com.apg.bidding.clientStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Strategy parser for various client operations
 * @author sohil
 *
 */
public class ClientOperationParser {
	
	private String operationString;
	
	private static Map<String, ClientOperationInterface> stringToOperationMap = new HashMap<String, ClientOperationInterface>(){{
		put("create", new CreateAuction());
		put("placebid", new PlaceBid());
		put("list", new ListAuction());
		put("listtopbidders", new ListTopBidders());
	}};
	
	public ClientOperationParser(String operationString){
		this.operationString = operationString;
	}
	
	public ClientOperationInterface toOperation(){
		
		if(null == this.operationString || this.operationString.length() == 0){
			return null;
		}
		
		if(!stringToOperationMap.containsKey(this.operationString)){
			return null;
		}
		
		return stringToOperationMap.get(this.operationString);
	}
}
