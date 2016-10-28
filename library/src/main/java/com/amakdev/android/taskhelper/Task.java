package com.amakdev.android.taskhelper;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Created by amakov on 28.10.2016.
 */

class Task {

    interface Callback {

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

    Task(String componentTag, String taskTag, Handler handler, TaskRunnable taskRunnable, Callback callback) {
        this.componentTag = componentTag;
        this.taskTag = taskTag;
        this.handler = handler;
        this.taskRunnable = taskRunnable;
        this.callback = callback;
    }

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

    void detachListener() {
        if (taskListener != null) {
            if (isProgressDisplayed) {
                isProgressDisplayed = false;
                this.taskListener.onTaskProgress(componentTag, taskTag, false);
                this.taskListener = null;
            }
        }
    }

    void notifyListener() {
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

    public String getId() {
        return componentTag + ":" + taskTag;
    }

    public String getComponentTag() {
        return componentTag;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }

}
