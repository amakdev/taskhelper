package com.amakdev.android.taskhelper;

/**
 * Created by amakov on 28.10.2016.
 */

class TaskResult {

    private final boolean isSuccess;
    private final Object result;
    private final Exception exception;

    TaskResult(boolean isSuccess, Object result, Exception exception) {
        this.isSuccess = isSuccess;
        this.result = result;
        this.exception = exception;
    }

    boolean isSuccess() {
        return isSuccess;
    }

    Object getResult() {
        return result;
    }

    Exception getException() {
        return exception;
    }

}
