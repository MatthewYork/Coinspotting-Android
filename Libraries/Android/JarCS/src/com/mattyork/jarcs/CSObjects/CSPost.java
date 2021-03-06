package com.mattyork.jarcs.CSObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.mattyork.jarcs.CSManager;
import com.mattyork.jarcs.OMScanner;

public class CSPost {
	public enum PostType {
		PostTypeDefault, PostTypeAskHN, PostTypeJobs, PostTypeShowHN
	}

	public PostType Type;
	public String Username = "";
	public String UrlString = "";
	public String UrlDomain = "";
	public String Title = "";
	public int Points = 0;
	public int CommentCount = 0;
	public String PostId = "";
	public String TimeCreatedString = "";

	/***
	 * Creates an ArrayList of Posts from a given HTML stirng and valid FNID.
	 * 
	 * @param htmlString
	 * @param fnid
	 * @return
	 */
	public static ArrayList<CSPost> parsedPostsFromHTML(String htmlString) {

		// Set up
		List<String> htmlComponents = Arrays
				.asList(htmlString
						.split("\\s*<tr><td align=right valign=top class=\"title\">\\s*"));
		ArrayList<CSPost> postArray = new ArrayList<CSPost>();

		// Scan through components and build posts
		for (int xx = 1; xx < htmlComponents.size(); xx++) {
			Log.i("asdf", xx + "");
			CSPost newPost = CSPost.postFromHTML(htmlComponents.get(xx));

			if (xx == htmlComponents.size() - 1) {
				OMScanner scanner = new OMScanner(htmlComponents.get(xx));
				scanner.skipToString("<td class=\"title\"><a href=\"");
				CSManager.getInstance().postFNID = scanner.scanToString("\"");
			}

			postArray.add(newPost);
		}

		return postArray;
	}

	public static CSPost postFromHTML(String htmlString) {
		CSPost newPost = new CSPost();

		OMScanner scanner = new OMScanner(htmlString);
		int contentIndex = -1;

		// Scan Url
		contentIndex = htmlString.indexOf("<a href=\"");
		if (contentIndex != -1) {
			scanner.setScanIndex(contentIndex + "<a href=\"".length());
			newPost.UrlString = scanner.scanToString("\">");
			newPost.UrlString = newPost.UrlString.replace("\" rel=\"nofollow",
					"");
		}

		// Scan Title
		newPost.Title = scanner.scanToString("</a>");
		scanner.setScanIndex(0);

		// Scan Points
		contentIndex = htmlString.indexOf("id=score_");
		if (contentIndex != -1) {
			scanner.setScanIndex(contentIndex + "id=score_".length());
			scanner.skipToString(">");
			String pointString = scanner.scanToString(" ");
			newPost.Points = Integer.parseInt(pointString);
			scanner.setScanIndex(0);
		}

		// Scan domain
		contentIndex = htmlString.indexOf("class=\"comhead\"> ");
		if (contentIndex != -1) {
			scanner.setScanIndex(contentIndex + "class=\"comhead\"> ".length());
			newPost.UrlDomain = scanner.scanToString(" ");
			scanner.setScanIndex(0);
		}

		// Scan Author
		contentIndex = htmlString.indexOf("<a href=\"user?id=");
		if (contentIndex != -1) {
			scanner.setScanIndex(contentIndex + "<a href=\"user?id=".length());
			scanner.skipToString(">");
			newPost.Username = scanner.scanToString("</a> ");
		} else {
			scanner.skipToString("</a> ");
		}

		if (htmlString.contains("  |")) {
			// Scan Time Ago
			newPost.TimeCreatedString = scanner.scanToString("  |");
		} else if (htmlString.contains("<td class=\"subtext\">")) {
			scanner.setScanIndex(0);
			scanner.skipToString("<td class=\"subtext\">");
			newPost.TimeCreatedString = scanner.scanToString("</td>");
		}

		// Scan Number of Comments
		if (htmlString.contains("  | <a href=\"item?id=")) {
			scanner.setScanIndex(0);
			scanner.skipToString("  | <a href=\"item?id=");
			newPost.PostId = scanner.scanToString("\">");

			// Scan over the comment string to get number of comments (0 if
			// discuss)
			String commentString = scanner.scanToString("</a>");
			if (commentString.contains("discuss")) {
				newPost.CommentCount = 0;
			} else {
				OMScanner commentScanner = new OMScanner(commentString);
				commentScanner.skipToString(">");
				String commentCountString = commentScanner.scanToString(" ");
				if (!commentCountString.contains("comm")) {
					newPost.CommentCount = Integer.parseInt(commentCountString);
				}
			}
		} else if (htmlString.contains("<a href=\"item?id=")) {
			OMScanner idScanner = new OMScanner(htmlString);
			idScanner.skipToString("<a href=\"item?id=");
			newPost.PostId = idScanner.scanToString("\">");
		} else {
			scanner.skipToString("\">");
		}

		// Check if Jobs post
		if (newPost.PostId.length() == 0 && newPost.Points == 0) {
			newPost.Type = PostType.PostTypeJobs;
			if (!newPost.UrlString.contains("http")) {
				newPost.UrlString.replace("item?id=", "");
			}
		}
		// Check if AskHN
		else {
			if (!newPost.UrlString.contains("http")
					&& newPost.PostId.length() > 0) {
				newPost.Type = PostType.PostTypeAskHN;
				// MAYBE UPDATE URL STRING HERE. CODE ISN"T CLEAR!
			} else {
				newPost.Type = PostType.PostTypeDefault;
			}
		}

		if (htmlString.toLowerCase().contains("show hn")) {
			newPost.Type = PostType.PostTypeShowHN;
		}

		return newPost;
	}
}
