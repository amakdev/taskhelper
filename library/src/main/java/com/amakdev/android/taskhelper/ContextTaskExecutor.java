package com.amakdev.android.taskhelper;

/**
 * Created by amakov on 28.10.2016.
 */

public interface ContextTaskExecutor {

    /**
     * Register listener for component
     *
     * @param componentTag tag of component (for example, fragment tag). Should be unique inside activity
     * @param taskListener listener for tasks
     */
    void registerListener(String componentTag, TaskListener taskListener);

    /**
     * Unregister component listener
     *
     * @param componentTag tag of component (for example, fragment tag). Should be unique inside activity
     */
    void unregisterListener(String componentTag);

    /**
     * Run task for execution
     *
     * @param componentTag tag of component (for example, fragment tag). Should be unique inside activity
     * @param taskTag      tag of task. Should be unique inside component
     * @param request      Request object to send into task
     * @param taskRunnable runnable to execute
     */
    void executeTask(String componentTag, String taskTag, Object request, TaskRunnable taskRunnable);

}
