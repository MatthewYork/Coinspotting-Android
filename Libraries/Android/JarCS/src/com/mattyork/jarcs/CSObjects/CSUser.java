package com.mattyork.jarcs.CSObjects;

import android.R.integer;

public class CSUser {
	String Username;
	int Karma;
	int Age;
	String AboutInfo;

	/***
	 * Creates a user from a given HTML string.
	 * @param htmlString
	 * @return HNUser
	 */
	public static CSUser userFromHTML(String htmlString) {
		return new CSUser();
	}

}
