package com.amakdev.android.taskhelper;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Task which manages execution of concrete task
 */
class Task {

    /**
     * Callback to be executed when task will finish and deliver result
     */
    interface Callback {

        /**
         * Called when task finished execution and delivered result
         *
         * @param task finished task
         */
        void onFinish(Task task);

    }

    private final String componentTag;
    private final String taskTag;
    private final Handler handler;
    private final TaskRunnable taskRunnable;
    private final Callback callback;

    private volatile TaskResult taskResult;
    private TaskListener taskListener;
    private boolean isProgressDisplayed = false;

    /**
     * Create new task to execute {@link TaskRunnable}
     *
     * @param componentTag tag of component inside UI context
     * @param taskTag      task of task inside component
     * @param handler      ui thread handler
     * @param taskRunnable runnable to execute
     * @param callback     callback for finish notification
     */
    Task(String componentTag, String taskTag, Handler handler, TaskRunnable taskRunnable, Callback callback) {
        this.componentTag = componentTag;
        this.taskTag = taskTag;
        this.handler = handler;
        this.taskRunnable = taskRunnable;
        this.callback = callback;
    }

    /**
     * Execute task
     *
     * @param executor executor for task
     * @param request  Request from {@link ContextTaskExecutor#executeTask(String, String, Object, TaskRunnable)}
     */
    void runTask(Executor executor, final Object request) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = false;
                Object result = null;
                Exception exception = null;
                try {
                    result = taskRunnable.execute(request);
                    isSuccess = true;
                } catch (Exception e) {
                    exception = e;
                }
                taskResult = new TaskResult(isSuccess, result, exception);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyListener();
                    }
                });
            }
        });
    }

    /**
     * Attach UI listener
     *
     * @param taskListener listener object
     */
    void attachListener(TaskListener taskListener) {
        if (taskListener != null) {
            this.taskListener = taskListener;
            if (taskResult != null) {
                deliverResult();
            } else {
                if (!isProgressDisplayed) {
                    isProgressDisplayed = true;
                    this.taskListener.onTaskProgress(componentTag, taskTag, true);
                }
            }
        }
    }

    /**
     * Detach previously attached UI listener
     */
    void detachListener() {
        if (taskListener != null) {
            if (isProgressDisplayed) {
                isProgressDisplayed = false;
                this.taskListener.onTaskProgress(componentTag, taskTag, false);
                this.taskListener = null;
            }
        }
    }

    private void notifyListener() {
        if (taskListener != null) {
            deliverResult();
        }
    }

    private void deliverResult() {
        if (taskResult.isSuccess()) {
            taskListener.onTaskCompleted(componentTag, taskTag, taskResult.getResult());
        } else {
            taskListener.onTaskFailed(componentTag, taskTag, taskResult.getException());
        }
        callback.onFinish(this);
    }

    /**
     * Get unique task id
     *
     * @return unique task id
     */
    String getId() {
        return componentTag + ":" + taskTag;
    }

    /**
     * Get component tag in which context task is executed
     *
     * @return component tag
     */
    String getComponentTag() {
        return componentTag;
    }

}
