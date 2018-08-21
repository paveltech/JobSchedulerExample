package com.example.android.jobschedulerexample;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.example.android.jobschedulerexample.MainActivity.MESSENGER_KEY;
import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_PROGRESS;
import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_START;

@SuppressLint("NewApi")
public class JobScheduleService extends JobService {

    private static final String TAG = JobScheduleService.class.getSimpleName();
    private Messenger messenger;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        //messenger = intent.getParcelableExtra(MESSENGER_KEY);
        //notification(getApplicationContext());
        return START_STICKY;

    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        new customApiCall().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "Destroy called");
    }

    private void notification(Context context, String appName, String link) {
        //String title = context.getString(R.string.app_name);
        //Intent intent = new Intent(context , MainActivity.class);
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("market://details?id="
                + link));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(appName)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Connect to network")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Creae Notification Manager

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }


    private class customApiCall extends AsyncTask<Void , Void , JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {

            JSONObject jsonObject = null;

            DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost("https://banglahdnatok.com/myad/incomingCallBlockerAd.txt");
// Depends on your web service
            httppost.setHeader("Content-type", "application/json");

            InputStream inputStream = null;
            String result = null;
            try {
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                //StringBuilder sb = new StringBuilder();

                //jsonObject = new JSONObject(reader.readLine());
                Log.d("JSON" , ""+reader.readLine());

            } catch (Exception e) {
                // Oops
            }
            finally {
                try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            //Log.d("JSON" , ""+jsonObject.toString());
        }
    }
}

