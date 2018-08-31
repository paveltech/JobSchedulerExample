package com.example.android.jobschedulerexample;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.json.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class JobScheduleService extends JobService {

    private static final String TAG = JobScheduleService.class.getSimpleName();



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        //messenger = intent.getParcelableExtra(MESSENGER_KEY);
        return START_STICKY;

    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //notification(getApplicationContext());
        //new customApiCall().execute();
        new JSONParse().execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "Destroy called");
    }

    private void notification(Context context , String appName , String appPackageName , String imageLink) {
        //String title = context.getString(R.string.app_name);
        //Intent intent = new Intent(context , MainActivity.class);
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        //notificationIntent.setData(Uri.parse("market://details?id="+ link));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Job Scheduler");
        builder.setLargeIcon(getBitmap(imageLink));
        builder.setContentTitle(appName);
        builder.setContentText(appPackageName);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setStyle(new Notification.BigPictureStyle().bigPicture(getBitmap(imageLink)));
        builder.setAutoCancel(true);
        // Creae Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        */

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setContentTitle(appName)
                .setContentText(appPackageName)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(result)
                .setLargeIcon(getBitmap(imageLink))
                .setStyle(new Notification.BigPictureStyle().bigPicture(getBitmap(imageLink)))
                .build();

        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notif);

    }


    private Bitmap getBitmap(String link){
        final Bitmap[] bitmap = new Bitmap[1];
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(link)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                       bitmap[0] = resource;
                    }
                });

        return bitmap[0];
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        JSONArray user = null;
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
           JSONObject json = jParser.getJSONFromUrl("https://banglahdnatok.com/myad/job_scheduler.text");

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {

                user = json.getJSONArray("user");
                JSONObject jsonObject = user.getJSONObject(0);

                Log.d("JSON" , ""+json.toString());
                String appName = jsonObject.getString("app_name");
                String appIcon = jsonObject.getString("app_image_link");
                String appDoc = jsonObject.getString("app_doc");
                String appPackage = jsonObject.getString("app_package_name");
                notification(getApplicationContext() , appName , appPackage , appIcon);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}

