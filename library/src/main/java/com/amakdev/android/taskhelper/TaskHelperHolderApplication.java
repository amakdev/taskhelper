package com.amakdev.android.taskhelper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * This interface should be implemented by your application instance.
 * Needed to use static access by {@link TaskHelper#getInstance(Context)}
 * and {@link LocalTaskExecutor#getForActivity(Activity)},
 * {@link LocalTaskExecutor#getForFragment(Fragment)}
 */
public interface TaskHelperHolderApplication {

    /**
     * Retain instance of task helper
     *
     * @return Instance of task helper
     */
    TaskHelper getInstance();

}
