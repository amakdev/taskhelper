package com.amakdev.android.taskhelper;

import android.app.Activity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by amakov on 28.10.2016.
 */

public class ActivityIds {

    private static class ActivityId {

        private ActivityId(Activity activity, int id) {
            this.activity = activity;
            this.id = id;
        }

        private final Activity activity;
        private final int id;

    }

    private List<ActivityId> items = new LinkedList<>();

    public void storeId(Activity activity, int id) {
        items.add(new ActivityId(activity, id));
    }

    public int getId(Activity activity) {
        for (ActivityId activityId : items) {
            if (activityId.activity == activity) {
                return activityId.id;
            }
        }
        return -1;
    }

    public int removeId(Activity activity) {
        Iterator<ActivityId> iterator = items.iterator();
        while (iterator.hasNext()) {
            ActivityId activityId = iterator.next();
            if (activityId.activity == activity) {
                iterator.remove();
                return activityId.id;
            }
        }
        return -1;
    }

}
