package com.example.marcin.backgroundtask;

import android.os.AsyncTask;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marcin on 30.05.18.
 */

public class AsyncMetadataDownloader extends AsyncTask<String,Integer,Void> {
    String fileType;
    int fileSize;
    TextView sizeTextView;
    TextView typeTextView;

    public AsyncMetadataDownloader(TextView size, TextView type){
        sizeTextView = size;
        typeTextView = type;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String urlString = strings[0];
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            fileSize = connection.getContentLength();
            fileType = connection.getContentType();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        sizeTextView.setText(Integer.toString(fileSize));
        typeTextView.setText(fileType);
        super.onPostExecute(aVoid);
    }
}
