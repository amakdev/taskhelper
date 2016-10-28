package com.amakdev.android.taskhelper;

/**
 * Created by amakov on 28.10.2016.
 */

public interface TaskRunnable<Request, Result> {

    Result execute(Request request) throws Exception;

}
