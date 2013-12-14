package com.mattyork.jarcs.AsyncTasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.mattyork.jarcs.CSManager;
import com.mattyork.jarcs.CSObjects.CSComment;
import com.mattyork.jarcs.CSObjects.CSPost;

public class LoadCommentsFromPostAsyncTask extends
		AsyncTask<CSPost, Void, ArrayList<CSComment>> {
	
	String url;
	CSPost mHnPost;

	public LoadCommentsFromPostAsyncTask(CSPost mHnPost) {
		super();
		this.mHnPost = mHnPost;
		
		this.url = "http://coinspotting.com/item?id="+mHnPost.PostId;
	}



	@Override
	protected ArrayList<CSComment> doInBackground(CSPost... params) {
		// TODO Auto-generated method stub
		
		HttpGet httpGet = new HttpGet(url);

		String htmlResponseString = "";

		HttpResponse response;
		try {
			response = CSManager.getInstance().Service.client.execute(httpGet);
			htmlResponseString = EntityUtils.toString(response.getEntity());

			ArrayList<CSComment> comments = CSComment
					.parsedCommentsFromHTML(htmlResponseString, mHnPost);

			return comments;

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
