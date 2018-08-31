package com.example.android.jobschedulerexample;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Messenger;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int MSG_JOB_START = 0;
    public static final int MSG_JOB_STOP = 1;
    public static final int MSG_JOB_PROGRESS = 2;

    public static final String MESSENGER_KEY = "MESSENGER_KEY";
    private static final String TAG = MainActivity.class.getSimpleName();

    private static int jobID = 0;
    private ProgressHandler progressHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressHandler = new ProgressHandler(this);

        Button scheduleJobButton = (Button) findViewById(R.id.button_schedule_job);
        Button cancelJobButton = (Button) findViewById(R.id.button_cancel_job);

        scheduleJobButton.setOnClickListener(this);
        cancelJobButton.setOnClickListener(this);

        ComponentName componentName = new ComponentName(this , JobScheduleService.class);
        final JobInfo jobInfo = new JobInfo.Builder(50 , componentName)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(5000)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }


    @Override
    protected void onStop() {
        stopService(new Intent(this, JobScheduleService.class));
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(this, JobScheduleService.class);
        Messenger messenger = new Messenger(progressHandler);
        serviceIntent.putExtra(MESSENGER_KEY, messenger);
        startService(serviceIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_schedule_job:

                Log.d(TAG, "Scheduling job");

                ComponentName componentName = new ComponentName(this , JobScheduleService.class);
                final JobInfo jobInfo = new JobInfo.Builder(++jobID , componentName)
                        //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .setPeriodic(10000)
                        .build();
                 JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                 jobScheduler.schedule(jobInfo);
                Toast.makeText(MainActivity.this, "New job scheduled with jobId: " + jobID,
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_cancel_job:
                Log.d(TAG, "Cancel all scheduled jobs");
                JobScheduler scheduler = (JobScheduler) getSystemService(
                        Context.JOB_SCHEDULER_SERVICE);
                List<JobInfo> allPendingJobs = scheduler.getAllPendingJobs();
                for (JobInfo info : allPendingJobs) {
                    int id = info.getId();
                    scheduler.cancel(id);
                }
                Toast.makeText(MainActivity.this, "All Job Canceled", Toast.LENGTH_SHORT).show();

                //or
//                mJobScheduler.cancelAll();
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
