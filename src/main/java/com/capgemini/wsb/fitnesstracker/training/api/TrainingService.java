package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface for managing training operations.
 */
public interface TrainingService {

    /**
     * Creates a new training.
     *
     * @param training The training object to save.
     * @return The saved training object with an assigned unique identifier.
     */
    Training createTraining(Training training);

    /**
     * Retrieves a training by its ID.
     *
     * @param trainingId The ID of the training to retrieve.
     * @return An {@link Optional} containing the found training or {@link Optional#empty()} if no training is found.
     */
    Optional<Training> getTraining(Long trainingId);

    /**
     * Retrieves all trainings.
     *
     * @return A list of all trainings.
     */
    List<Training> getAllTrainings();

    /**
     * Retrieves all trainings associated with the specified user ID.
     *
     * @param userId the ID of the user
     * @return a list of trainings associated with the user
     */
    List<Training> getTrainingsByUserId(Long userId);

    /**
     * Retrieves all trainings that ended after the specified date.
     *
     * @param date the date to compare against
     * @return a list of trainings that ended after the specified date
     */
    List<Training> getTrainingsEndedAfter(Date date);

    /**
     * Retrieves all trainings with the specified activity type.
     *
     * @param activityType the activity type to search for
     * @return a list of trainings with the specified activity type
     */
    List<Training> getTrainingsByActivityType(ActivityType activityType);

    /**
     * Updates the distance of a training.
     *
     * @param trainingId The ID of the training to update.
     * @param distance   The new distance value.
     * @return The updated training.
     * @throws TrainingNotFoundException if the training with the specified ID is not found.
     */
    Training updateTrainingDistance(Long trainingId, double distance);

    /**
     * Sends a training completion notification email to the user who completed the training.
     *
     * @param trainingId the ID of the completed training
     */
    void sendTrainingFinishedNotification(Long trainingId);

    Training getTrainingById(Long id);

    Training updateTraining(Long id, Training training);

    List<Training> getAllTrainingsByUser(Long id);

    List<Training> getAllTrainingsByActivityType(ActivityType activityType);
}