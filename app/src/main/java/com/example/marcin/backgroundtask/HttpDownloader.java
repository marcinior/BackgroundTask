package com.example.marcin.backgroundtask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HttpDownloader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_downloader);
        Button getMetadataButton = (Button) findViewById(R.id.downloadInformationButton);
        Button downloadDataButton = (Button) findViewById(R.id.dowloadFileButton);

        getMetadataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText) findViewById(R.id.adressEditText)).getText().toString();

                if (!url.isEmpty()) {
                    getFileMetadataAsync(url);
                } else {
                    Toast.makeText(HttpDownloader.this, "Uzupełnij pole adres!", Toast.LENGTH_LONG).show();
                }
            }
        });

        downloadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText) findViewById(R.id.adressEditText)).getText().toString();

                if (!url.isEmpty()) {
                    FileDownloadService f = new FileDownloadService();
                    FileDownloadService.startServiceHelper(HttpDownloader.this, url);
                } else {
                    Toast.makeText(HttpDownloader.this, "Uzupełnij pole adres!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int progress = bundle.getInt(FileDownloadService.PROGRESS);
            long bytes = bundle.getLong(FileDownloadService.BYTES);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.downloadProgressBar);
            TextView downloadedBytesTextView = (TextView) findViewById(R.id.downloadBytesTextView);
            progressBar.setProgress(progress);
            downloadedBytesTextView.setText(Long.toString(bytes));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(
                FileDownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void getFileMetadataAsync(String url) {
        AsyncMetadataDownloader asyncMetadataDownloader = new AsyncMetadataDownloader((TextView) findViewById(R.id.sizeTextView), (TextView) findViewById(R.id.typeTextView));
        asyncMetadataDownloader.execute(url);
    }

}
