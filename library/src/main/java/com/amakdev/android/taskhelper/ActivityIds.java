package com.amakdev.android.taskhelper;

import android.app.Activity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Holder for storing unique activity ids
 */
class ActivityIds {

    private static class ActivityId {

        private ActivityId(Activity activity, int id) {
            this.activity = activity;
            this.id = id;
        }

        private final Activity activity;
        private final int id;

    }

    private List<ActivityId> items = new LinkedList<>();

    /**
     * Store id for activity reference
     *
     * @param activity activity reference
     * @param id       unique id
     */
    void storeId(Activity activity, int id) {
        items.add(new ActivityId(activity, id));
    }

    /**
     * Get id for activity reference
     *
     * @param activity activity reference
     * @return unique id
     */
    int getId(Activity activity) {
        for (ActivityId activityId : items) {
            if (activityId.activity == activity) {
                return activityId.id;
            }
        }
        return -1;
    }

    /**
     * Remove id for activity reference
     *
     * @param activity activity reference
     * @return unique id
     */
    int removeId(Activity activity) {
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
