package g0v.ly.android.rest;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RESTMethods {
    private static final Logger logger = LoggerFactory.getLogger(RESTMethods.class);

    public void restGet(String getUrl, RestApiCallback restApiCallback) {
        RESTGetAsyncTask restGetAsyncTask = new RESTGetAsyncTask(getUrl, restApiCallback);
        restGetAsyncTask.execute();
    }

    public interface RestApiCallback {
        void getDone(String response, long spendTime, int page);
    }

    private class RESTGetAsyncTask extends AsyncTask<Void, Integer, Void> {
        long spendTime;
        String getUrl;
        String responseStr;
        RestApiCallback restApiCallback;

        public RESTGetAsyncTask(String url, RestApiCallback callback) {
            getUrl = url;
            restApiCallback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            spendTime = System.currentTimeMillis();

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(getUrl);
            try {
                HttpResponse response = client.execute(request);
                responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                logger.error("[doInBackground] " + e);
            }
            spendTime = System.currentTimeMillis() - spendTime;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int page;
            String[] temp;

            temp = getUrl.split("=");
            page = Integer.valueOf(temp[1].substring(0, temp[1].lastIndexOf("&")));
            restApiCallback.getDone(responseStr, spendTime, page);
            logger.trace("[onPostExecute] get page " + page + " done");
        }
    }
}
