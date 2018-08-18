package com.example.android.jobschedulerexample;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_PROGRESS;
import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_START;
import static com.example.android.jobschedulerexample.MainActivity.MSG_JOB_STOP;

public class ProgressHandler extends Handler {

    private WeakReference<MainActivity> mainActivityWeakReference;
    private TextView textViewProgress;

    ProgressHandler(final MainActivity activity){
        super();
        this.mainActivityWeakReference = new WeakReference<>(activity);
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        MainActivity mainActivity = mainActivityWeakReference.get();
        if (mainActivity==null){
            return;
        }
        textViewProgress = (TextView) mainActivity.findViewById(R.id.text_Progress);
        textViewProgress.setText(String.valueOf(msg.obj));
        switch (msg.what){
            case MSG_JOB_START:
                textViewProgress.setTextColor(getColor(R.color.colorPrimaryDark));
                break;
            case MSG_JOB_PROGRESS:
                textViewProgress.setTextColor(getColor(R.color.colorAccent));
                break;
            case MSG_JOB_STOP:
                textViewProgress.setTextColor(getColor(R.color.colorPrimary));
                break;
        }
    }

    private int getColor(@ColorRes int color) {
        return mainActivityWeakReference.get().getResources().getColor(color);
    }
}
