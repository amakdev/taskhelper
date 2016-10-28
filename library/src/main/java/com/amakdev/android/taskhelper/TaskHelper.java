package com.amakdev.android.taskhelper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper class for managing activity unique ids. Should be created inside
 * {@link Application#onCreate()} to work properly
 */
public class TaskHelper implements Application.ActivityLifecycleCallbacks {

    /**
     * Get instance of TaskHelper
     *
     * @param context UI context
     * @return TaskHelper instance from {@link TaskHelperHolderApplication}
     */
    public static synchronized TaskHelper getInstance(Context context) {
        Application application = (Application) context.getApplicationContext();
        if (!(application instanceof TaskHelperHolderApplication)) {
            throw new RuntimeException("Your android.app.Application should implement com.amakdev.android.taskhelper.TaskHelperHolderApplication interface");
        }
        TaskHelperHolderApplication holderApplication = (TaskHelperHolderApplication) application;
        return holderApplication.getInstance();
    }

    private static final String KEY_ACTIVITY_ID = TaskHelper.class.getCanonicalName() + ".KEY_ACTIVITY_ID";

    private final AtomicInteger idSequence = new AtomicInteger(0);
    private final ActivityIds activityIds = new ActivityIds();
    private final Map<Integer, ActivityContextTaskExecutor> activityTaskExecutorMap = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Executor executor = Executors.newFixedThreadPool(8);

    /**
     * Create global {@link TaskHelper} object
     *
     * @param context any context
     */
    public TaskHelper(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        int id;
        if (savedInstanceState == null) {
            id = idSequence.getAndIncrement();
            activityTaskExecutorMap.put(id, new ActivityContextTaskExecutor(handler, executor));
        } else {
            id = savedInstanceState.getInt(KEY_ACTIVITY_ID, -1);
        }
        activityIds.storeId(activity, id);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        int id = activityIds.getId(activity);
        ActivityContextTaskExecutor activityContextTaskExecutor = activityTaskExecutorMap.get(id);
        activityContextTaskExecutor.notifyUIVisible();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        int id = activityIds.getId(activity);
        ActivityContextTaskExecutor activityContextTaskExecutor = activityTaskExecutorMap.get(id);
        activityContextTaskExecutor.notifyUIHidden();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        int id = activityIds.getId(activity);
        outState.putInt(KEY_ACTIVITY_ID, id);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        int id = activityIds.removeId(activity);
        if (activity.isFinishing()) {
            activityTaskExecutorMap.remove(id);
        }
    }

    /**
     * Retain task executor instance to be used in specified activity context
     *
     * @param activity Activity context
     * @return ready to use task executor
     */
    public ContextTaskExecutor getTaskExecutor(Activity activity) {
        int id = activityIds.getId(activity);
        return activityTaskExecutorMap.get(id);
    }

}
