package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    /**
     * Finds all trainings associated with the specified user ID.
     *
     * @param userId the ID of the user
     * @return a list of trainings associated with the user
     */
    List<Training> findByUserId(Long userId);

    /**
     * Finds all trainings that ended after the specified date.
     *
     * @param date the date to compare against
     * @return a list of trainings that ended after the specified date
     */
    List<Training> findByEndTimeAfter(Date date);

    /**
     * Finds all trainings with the specified activity type.
     *
     * @param activityType the activity type to search for
     * @return a list of trainings with the specified activity type
     */
    List<Training> findByActivityType(ActivityType activityType);

}
