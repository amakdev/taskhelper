package com.amakdev.android.taskhelper;

/**
 * Background task runnable
 * @param <Request> request type
 * @param <Result> result type
 */
public interface TaskRunnable<Request, Result> {

    /**
     * Called in background thread
     *
     * @param request request object provided from {@link ContextTaskExecutor#executeTask(String, String, Object, TaskRunnable)}
     * @return result of task
     * @throws Exception for error
     */
    Result execute(Request request) throws Exception;

}
