package com.amakdev.android.taskhelper;

import android.app.Activity;

/**
 * Task executor to be used inside concrete component.
 * <p>
 * Note, that {@link #registerListener(TaskListener)}
 * and {@link #unregisterListener()} should be called in
 * matching lifecycle methods, for example:
 * <ul>
 * <li><code>onCreate() - onDestroy()</code>
 * <li><code>onStart() - onStop()</code>
 * <li><code>onResume() - onPause()</code>
 * </ul
 */
public class LocalTaskExecutor {

    /**
     * Create executor instance for {@link Activity}
     *
     * @param activity activity component
     * @return ready for use task executor
     */
    public static LocalTaskExecutor getForActivity(Activity activity) {
        String componentTag = activity.getClass().getCanonicalName();
        return new LocalTaskExecutor(activity, componentTag);
    }

    /**
     * Create executor instance for {@link android.app.Fragment}
     *
     * @param fragment fragment component
     * @return ready for use task executor
     */
    public static LocalTaskExecutor getForFragment(android.app.Fragment fragment) {
        Activity activity = fragment.getActivity();
        String componentTag = fragment.getClass().getCanonicalName() + ":" + fragment.getClass().getCanonicalName() + "/" + fragment.getTag();
        return new LocalTaskExecutor(activity, componentTag);
    }

    /**
     * Create executor instance for support {@link android.support.v4.app.Fragment}
     *
     * @param fragment support fragment component
     * @return ready for use task executor
     */
    public static LocalTaskExecutor getForFragment(android.support.v4.app.Fragment fragment) {
        Activity activity = fragment.getActivity();
        String componentTag = fragment.getClass().getCanonicalName() + ":" + fragment.getClass().getCanonicalName() + "/" + fragment.getTag();
        return new LocalTaskExecutor(activity, componentTag);
    }

    private final ContextTaskExecutor contextTaskExecutor;
    private final String componentTag;

    /**
     * Create task executor in Activity contet to be used inside concrete component
     *
     * @param activity     Activity context
     * @param componentTag unique component tag inside activity
     */
    public LocalTaskExecutor(Activity activity, String componentTag) {
        TaskHelper taskHelper = TaskHelper.getInstance(activity);
        this.contextTaskExecutor = taskHelper.getTaskExecutor(activity);
        this.componentTag = componentTag;
    }

    /**
     * Retain component tag
     *
     * @return component tag
     */
    public String getComponentTag() {
        return componentTag;
    }

    /**
     * Register listener for component
     *
     * @param taskListener listener for tasks
     */
    public void registerListener(TaskListener taskListener) {
        contextTaskExecutor.registerListener(componentTag, taskListener);
    }

    /**
     * Unregister component listener
     */
    public void unregisterListener() {
        contextTaskExecutor.unregisterListener(componentTag);
    }

    /**
     * Run task for execution
     *
     * @param taskTag      tag of task. Should be unique inside component
     * @param request      Request object to send into task
     * @param taskRunnable runnable to execute
     */
    public void executeTask(String taskTag, Object request, TaskRunnable taskRunnable) {
        contextTaskExecutor.executeTask(componentTag, taskTag, request, taskRunnable);

    }

}
