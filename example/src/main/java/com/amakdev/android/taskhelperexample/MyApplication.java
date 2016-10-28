package com.amakdev.android.taskhelperexample;

import android.app.Application;

import com.amakdev.android.taskhelper.TaskHelper;
import com.amakdev.android.taskhelper.TaskHelperHolderApplication;

/**
 * Created by amakov on 28.10.2016.
 */

public class MyApplication extends Application implements TaskHelperHolderApplication {

    private TaskHelper taskHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        taskHelper = new TaskHelper(this);
    }

    @Override
    public TaskHelper getInstance() {
        return taskHelper;
    }

}
