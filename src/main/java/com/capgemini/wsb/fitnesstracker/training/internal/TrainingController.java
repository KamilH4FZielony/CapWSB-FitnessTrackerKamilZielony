package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link Training} entities.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    /**
     * Creates a new training.
     *
     * @param trainingDto The TrainingDto containing the data to create a new training.
     * @return The TrainingDto of the newly created training.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto addTraining(@RequestBody TrainingDto trainingDto) {
        Training training = trainingMapper.toEntity(trainingDto);
        Training savedTraining = trainingService.createTraining(training);
        return trainingMapper.toDto(savedTraining);
    }

    /**
     * Retrieves a specific training by ID.
     *
     * @param id The ID of the training to retrieve.
     * @return The TrainingDto of the requested training.
     */
    @GetMapping("/{id}")
    public TrainingDto getTrainingById(@PathVariable Long id) {
        return trainingService.getTraining(id)
                .map(trainingMapper::toDto)
                .orElseThrow(() -> new TrainingNotFoundException(id));
    }

    /**
     * Retrieves all trainings.
     *
     * @return A list of all {@link TrainingDto}.
     */
    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all trainings associated with the specified user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link TrainingDto} associated with the user
     */
    @GetMapping("/user/{userId}")
    public List<TrainingDto> getTrainingsByUserId(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId)
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all trainings that ended after the specified date.
     *
     * @param date the date to compare against
     * @return a list of {@link TrainingDto} that ended after the specified date
     */
    @GetMapping("/ended-after")
    public List<TrainingDto> getTrainingsEndedAfter(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return trainingService.getTrainingsEndedAfter(date)
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all trainings with the specified activity type.
     *
     * @param activityType the activity type to search for
     * @return a list of {@link TrainingDto} with the specified activity type
     */
    @GetMapping("/activity/{activityType}")
    public List<TrainingDto> getTrainingsByActivityType(@PathVariable ActivityType activityType) {
        return trainingService.getTrainingsByActivityType(activityType)
                .stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the distance of a training.
     *
     * @param id       The ID of the training to update.
     * @param distance The new distance value.
     * @return The updated {@link TrainingDto}.
     */
    @PatchMapping("/{id}/distance")
    public TrainingDto updateTrainingDistance(@PathVariable Long id, @RequestBody double distance) {
        Training updatedTraining = trainingService.updateTrainingDistance(id, distance);
        return trainingMapper.toDto(updatedTraining);
    }

    @PostMapping("/{id}/notify")
    public void sendTrainingCompletionNotification(@PathVariable Long id) {
        trainingService.sendTrainingFinishedNotification(id);
    }
}