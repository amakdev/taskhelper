package com.amakdev.android.taskhelper;

/**
 * Background task listener
 */
public interface TaskListener {

    /**
     * Called when task progress is changed. Method is called directly when task
     * starting or finishing to run or when UI component is attached or detached
     * to Task
     *
     * @param componentTag tag of component inside UI context
     * @param taskTag      task of task inside component
     * @param inProgress   is task currently in progress
     */
    void onTaskProgress(String componentTag, String taskTag, boolean inProgress);

    /**
     * Called when task is successfully completed
     *
     * @param componentTag tag of component inside UI context
     * @param taskTag      task of task inside component
     * @param resultObject result of task execution, returned by {@link TaskRunnable#execute(Object)}
     */
    void onTaskCompleted(String componentTag, String taskTag, Object resultObject);

    /**
     * Called when task thrown exception instead of returning result
     *
     * @param componentTag tag of component inside UI context
     * @param taskTag      task of task inside component
     * @param ex           exception that was thrown by {@link TaskRunnable#execute(Object)}
     */
    void onTaskFailed(String componentTag, String taskTag, Exception ex);

}
