package com.example.marcin.backgroundtask;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by marcin on 30.05.18.
 */

public class FileDownloadService extends IntentService {
    private static final String ACTION = "task1";
    private static final String PARAMETER = "parameter1";
    public static final String NOTIFICATION = "serviceNotification";
    public static final String PROGRESS = "progress";
    public static final String BYTES = "bytes";

    public static void startServiceHelper(Context context, String parameter) {
        Log.d("FileDownloadService", "im in static method");
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.setAction(ACTION);
        intent.putExtra(PARAMETER, parameter);
        context.startService(intent);
    }

    public FileDownloadService() {
        super("com.example.marcin.backgroundtask.FileDownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION.equals(action)) {
                final String param = intent.getStringExtra(PARAMETER);
                downloadFile(param);
            } else {
                Log.e("FileDownloadService", "unknown action");
            }
        }
        Log.d("FileDownloadService", "the service has been completed successfully");
    }

    private void sendBroadcast(int progress,long total){
        Intent broadcastIntend = new Intent(NOTIFICATION);
        broadcastIntend.putExtra(PROGRESS,progress);
        broadcastIntend.putExtra(BYTES,total);
        sendBroadcast(broadcastIntend);
    }

    private void downloadFile(String sUrl) {
        HttpURLConnection connection = null;
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String path;
        int count = 0;
        int length;
        long total = 0;

        try {
            URL url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            length = connection.getContentLength();
            String fileName = url.getPath().substring(url.getPath().lastIndexOf("/")+1);
            path = Environment.getExternalStorageDirectory().toString() + File.separator + fileName;

            if(new File(path).exists()){
                new File(path).delete();
            }

            inputStream = new BufferedInputStream(url.openStream(),8192);
            outputStream = new FileOutputStream(path);

            byte[] buffor = new byte[1024];

            while ((count = inputStream.read(buffor))!= -1){
                total += count;
                sendBroadcast((int) ((total * 100) / length),total);
                outputStream.write(buffor);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
