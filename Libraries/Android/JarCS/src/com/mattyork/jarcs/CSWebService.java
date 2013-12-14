package com.mattyork.jarcs;

import org.apache.http.impl.client.DefaultHttpClient;

public class CSWebService {
	
	public enum PostFilterType{
		PostFilterTypeTop,
	    PostFilterTypeAsk,
	    PostFilterTypeNew,
	    PostFilterTypeJobs,
	    PostFilterTypeBest
	}
	
	/***
	 * Default constructor
	 */
	public CSWebService(){
		client = new DefaultHttpClient();
	}
	
	public DefaultHttpClient client;
	
	
}
