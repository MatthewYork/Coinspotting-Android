package com.mattyork.jarcs.AsyncTasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.mattyork.jarcs.CSManager;
import com.mattyork.jarcs.CSWebService.PostFilterType;
import com.mattyork.jarcs.CSObjects.CSPost;

public class LoadPostsWithFilterAsyncTask extends
		AsyncTask<String, Void, ArrayList<CSPost>> {

	String url;

	public LoadPostsWithFilterAsyncTask(PostFilterType filter) {
		// TODO Auto-generated constructor stub

		String pathAddition = "";

		switch (filter) {
		case PostFilterTypeTop:
			pathAddition = "";
			break;
		case PostFilterTypeAsk:
			pathAddition = "ask";
			break;
		case PostFilterTypeBest:
			pathAddition = "best?";
			break;
		case PostFilterTypeJobs:
			pathAddition = "jobs";
			break;
		case PostFilterTypeNew:
			pathAddition = "newest";
			break;
		default:
			break;
		}
		this.url = "http://coinspotting.com/" + pathAddition;
	}

	@Override
	protected ArrayList<CSPost> doInBackground(String... params) {
		// TODO Auto-generated method stub

		HttpGet httpGet = new HttpGet(url);

		String htmlResponseString = "";

		HttpResponse response;
		try {
			response = CSManager.getInstance().Service.client.execute(httpGet);
			htmlResponseString = EntityUtils.toString(response.getEntity());

			ArrayList<CSPost> posts = CSPost
					.parsedPostsFromHTML(htmlResponseString);

			return posts;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
