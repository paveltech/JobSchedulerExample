package com.example.android.jobschedulerexample;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.jobschedulerexample.MainActivity.MESSENGER_KEY;
import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_PROGRESS;
import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_START;

@SuppressLint("NewApi")
public class JobScheduleService extends JobService{

    private static final String TAG = JobScheduleService.class.getSimpleName();
    private Messenger messenger;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG , "onStartCommand");
        messenger = intent.getParcelableExtra(MESSENGER_KEY);
        return START_STICKY;

    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG , "onStartJob");
        sendMessage(MSG_JOB_START, getString(R.string.job_started) + "\n Job Id : " +
                jobParameters.getJobId());
        new JobAsyncTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }


    private class JobAsyncTask extends AsyncTask<JobParameters , Void , JobParameters>{
        private final JobService jobService;

        JobAsyncTask(JobService jobService){
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
            SystemClock.sleep(5000);
            return jobParameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            sendMessage(MSG_JOB_PROGRESS, "Background process completed");
        }
    }

    private void sendMessage(int messageID , @Nullable Object params){
        if (messenger==null){
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }
        Message message = Message.obtain();
        message.what = messageID;
        message.obj = params;
        try{
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }
}
