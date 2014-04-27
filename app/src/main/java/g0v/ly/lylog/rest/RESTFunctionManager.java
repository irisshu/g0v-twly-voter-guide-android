package g0v.ly.lylog.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class RESTFunctionManager {

	public void restGet(String getUrl, RestApiCallback restApiCallback) {
		Log.i("RESTFunctionManager :: restGet", "(0) in restGet");
		RESTGetAsyncTask restGetAsyncTask  = new RESTGetAsyncTask(getUrl, restApiCallback);
		restGetAsyncTask.execute();
	}

	private class RESTGetAsyncTask extends AsyncTask<Void, Integer, Void> {
		long 			spendTime;
		String 			getUrl;
		String 			responseStr;
		RestApiCallback restApiCallback;

		public RESTGetAsyncTask(String url, RestApiCallback callback) {
			getUrl 			= url;
			restApiCallback = callback;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			spendTime = System.currentTimeMillis();

			DefaultHttpClient 	client 	= new DefaultHttpClient();
			HttpGet 			request = new HttpGet(getUrl);
			Log.i("RESTFunctionManager :: ThreadRESTGet", "(1) getUrl: " + getUrl);
			try {
				HttpResponse response 	= client.execute(request);
				responseStr 			= EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.i("RESTFunctionManager :: ThreadRESTGet", "(2) get responseStr");
			} catch (IOException e) {
				e.printStackTrace();
			}
			spendTime = System.currentTimeMillis() - spendTime;
			Log.i("RESTFunctionManager :: ThreadRESTGet", "(3) spend " + spendTime / 1000 +
					"." + spendTime % 1000 + " sec to get response.");
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			String[] temp;
			temp = getUrl.split("=");
			int page = Integer.valueOf(temp[1].substring(0, temp[1].lastIndexOf("&")));
			Log.e("RESTFunctionManager :: ThreadRESTGet", "page: " + page);
			restApiCallback.getDone(responseStr, spendTime, page);
		}
	}
}