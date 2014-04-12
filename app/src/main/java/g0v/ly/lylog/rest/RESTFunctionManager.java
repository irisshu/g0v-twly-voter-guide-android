package g0v.ly.lylog.rest;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class RESTFunctionManager {

	public void restGet(String getUrl) {
		// TODO return response String


		Log.e("RESTFunctionManager :: restGet", "in restGet");
		ThreadRESTGet threadRESTGet = new ThreadRESTGet(getUrl);
		threadRESTGet.start();

		/*
		DefaultHttpClient 	client 	= new DefaultHttpClient();
		HttpGet 			request = new HttpGet(getUrl);
		try {
			HttpResponse response 	= client.execute(request);
			responseStr 			= EntityUtils.toString(response.getEntity());

			Log.e("RESTFunctionManager :: restGet", "responseStr: " + responseStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		//return responseStr;
	}

	private class ThreadRESTGet  extends Thread {

		private String 	getUrl;
		private long	startTime;

		public ThreadRESTGet(String url) {
			getUrl = url;
		}

		@Override
		public void run() {
			super.run();

			startTime = System.currentTimeMillis();

			DefaultHttpClient 	client 	= new DefaultHttpClient();
			HttpGet 			request = new HttpGet(getUrl);

			Log.e("RESTFunctionManager :: ThreadRESTGet", "getUrl: " + getUrl);

			try {
				HttpResponse response 	= client.execute(request);
				String responseStr = EntityUtils.toString(response.getEntity());

				Log.e("RESTFunctionManager :: ThreadRESTGet", "responseStr: " + responseStr);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.e("RESTFunctionManager :: ThreadRESTGet", "spend " + (System.currentTimeMillis() - startTime) / 1000 + " sec to get response.");
		}
	}
}
