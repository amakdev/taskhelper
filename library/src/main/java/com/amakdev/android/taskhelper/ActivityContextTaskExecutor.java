package com.amakdev.android.taskhelper;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Task executor for all tasks in context of one concrete Activity instance.
 * Activity instance is same means that its id is same
 */
class ActivityContextTaskExecutor implements ContextTaskExecutor, Task.Callback {

    private final Handler handler;
    private final Executor executor;
    private final Map<String, TaskListener> listenerByComponentTagMap = new HashMap<>();
    private final AtomicBoolean isUIVisible = new AtomicBoolean(false);
    private final Map<String, Task> taskByTagMap = new HashMap<>();

    /**
     * Create instance of executor
     *
     * @param handler  UI thread handler
     * @param executor task executor
     */
    ActivityContextTaskExecutor(Handler handler, Executor executor) {
        this.handler = handler;
        this.executor = executor;
    }

    @Override
    public void registerListener(String componentTag, TaskListener taskListener) {
        if (listenerByComponentTagMap.containsKey(componentTag)) {
            throw new IllegalStateException("Component [" + componentTag + "] have already registered listener");
        }
        listenerByComponentTagMap.put(componentTag, taskListener);
        if (isUIVisible.get()) {
            List<Task> list = new ArrayList<>(taskByTagMap.values());
            for (Task task : list) {
                if (componentTag.equals(task.getComponentTag())) {
                    task.attachListener(taskListener);
                }
            }
        }
    }

    @Override
    public void unregisterListener(String componentTag) {
        TaskListener taskListener = listenerByComponentTagMap.remove(componentTag);
        if (taskListener == null) {
            throw new IllegalStateException("Component [" + componentTag + "] hve not registered listener");
        }
        if (isUIVisible.get()) {
            for (Task task : taskByTagMap.values()) {
                if (componentTag.equals(task.getComponentTag())) {
                    task.detachListener();
                }
            }
        }
    }

    @Override
    public void executeTask(String componentTag, String taskTag, Object request, TaskRunnable taskRunnable) {
        Task task = new Task(componentTag, taskTag, handler, taskRunnable, this);
        taskByTagMap.put(task.getId(), task);
        task.runTask(executor, request);
        if (isUIVisible.get()) {
            TaskListener taskListener = listenerByComponentTagMap.get(task.getComponentTag());
            task.attachListener(taskListener);
        }
    }

    /**
     * Notify that UI became visible
     */
    void notifyUIVisible() {
        isUIVisible.set(true);
        List<Task> list = new ArrayList<>(taskByTagMap.values());
        for (Task task : list) {
            TaskListener taskListener = listenerByComponentTagMap.get(task.getComponentTag());
            if (taskListener != null) {
                task.attachListener(taskListener);
            }
        }
    }

    /**
     * Notify that UI became invisible
     */
    void notifyUIHidden() {
        isUIVisible.set(false);
        for (Task task : taskByTagMap.values()) {
            task.detachListener();
        }
    }

    @Override
    public void onFinish(Task task) {
        task.detachListener();
        taskByTagMap.remove(task.getId());
    }

}
