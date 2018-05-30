package com.example.marcin.backgroundtask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_downloader);
        Button getMetadataButton = (Button) findViewById(R.id.downloadInformationButton);

        getMetadataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText) findViewById(R.id.adressEditText)).getText().toString();

                if(!url.isEmpty()){
                    getFileMetadataAsync(url);
                } else {
                    Toast.makeText(HttpDownloader.this,"Uzupełnij pole adres!",Toast.LENGTH_LONG);
                }
            }
        });

    }

    private void getFileMetadataAsync(String url){
        AsyncMetadataDownloader asyncMetadataDownloader = new AsyncMetadataDownloader((TextView)findViewById(R.id.sizeTextView),(TextView)findViewById(R.id.typeTextView));
        asyncMetadataDownloader.execute(url);
    }

}
