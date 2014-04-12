package g0v.ly.lylog.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by HMW on 2014/4/12.
 */
public class RESTFunctionManager {

	String responseStr;

	public void restGet(URL getUrl) {
		DefaultHttpClient 	client 	= new DefaultHttpClient();
		HttpGet 			request = new HttpGet(getUrl.toString());
		try {
			HttpResponse response 	= client.execute(request);
			responseStr 			= EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
