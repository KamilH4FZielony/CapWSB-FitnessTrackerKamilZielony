package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;

public class ActivityUtils {

    public static double getMetValue(ActivityType activityType) {
        switch (activityType) {
            case RUNNING:
                return 9.8;
            case CYCLING:
                return 7.5;
            case SWIMMING:
                return 8.0;
            case WALKING:
                return 3.8;
            default:
                return 1.0;
        }
    }
}