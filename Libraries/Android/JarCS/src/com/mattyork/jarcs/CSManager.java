package com.mattyork.jarcs;

import java.util.HashMap;

import org.apache.http.cookie.Cookie;

import com.mattyork.jarcs.CSObjects.CSUser;

public class CSManager {

	/***
	 * HN Manager is a singleton, and as such, needs a static reference.
	 */
	private static CSManager instance = null;
	
	public CSWebService Service;
	public String postFNID;
	String userSubmissionFNID;
	Cookie SessionCookie;
	CSUser SessionUser;
	HashMap<String, Integer> MarkAsReadDictionary;
	
	
	protected CSManager(){
		Service = new CSWebService();
	}
	
	public static CSManager getInstance() {
	      if(instance == null) {
	         instance = new CSManager();
	      }
	      return instance;
	   }
}
