package com.amakdev.android.taskhelper;

/**
 * Created by amakov on 28.10.2016.
 */

public interface TaskListener {

    void onTaskProgress(String componentTag, String taskTag, boolean inProgress);

    void onTaskCompleted(String componentTag, String taskTag, Object resultObject);

    void onTaskFailed(String componentTag, String taskTag, Exception ex);

}
