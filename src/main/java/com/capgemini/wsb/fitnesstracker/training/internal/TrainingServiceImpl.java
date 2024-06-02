package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.*;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserProvider userProvider;
    private final EmailSender emailSender;

    public TrainingServiceImpl(TrainingRepository trainingRepository, UserProvider userProvider, EmailSender emailSender) {
        this.trainingRepository = trainingRepository;
        this.userProvider = userProvider;
        this.emailSender = emailSender;
    }

    private double getMetValue(ActivityType activityType) {
        return ActivityUtils.getMetValue(activityType);
    }
    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    @Override
    public List<Training> getTrainingsEndedAfter(Date date) {
        return trainingRepository.findByEndTimeAfter(date);
    }

    @Override
    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    @Override
    public Training updateTrainingDistance(Long trainingId, double distance) {
        return trainingRepository.findById(trainingId)
                .map(training -> {
                    training.setDistance(distance);
                    return trainingRepository.save(training);
                })
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));
    }

    @Override
    public Training updateTraining(Long id, Training training) {
        return trainingRepository.findById(id)
                .map(existingTraining -> {
                    existingTraining.setActivityType(training.getActivityType());
                    existingTraining.setStartTime(training.getStartTime());
                    existingTraining.setEndTime(training.getEndTime());

                    // Calculate calories burned
                    int caloriesBurned = calculateCaloriesBurned(existingTraining);
                    existingTraining.setCaloriesBurned(caloriesBurned);

                    return trainingRepository.save(existingTraining);
                })
                .orElseThrow(() -> new TrainingNotFoundException(id));
    }
    private int calculateCaloriesBurned(Training training) {
        double met = getMetValue(training.getActivityType());
        double durationHours = calculateDurationHours(training.getStartTime(), training.getEndTime());
        double weightKg = training.getUser().getWeight();

        // Using melValue method from ActivityUtils
        double melValue = ActivityUtils.getMetValue(training.getActivityType());

        // Calories burned formula: melValue * weight (kg) * duration (hours)
        return (int) (melValue * weightKg * durationHours);
    }

    private double calculateDurationHours(Date startTime, Date endTime) {
        // Calculate duration in milliseconds
        long durationMillis = endTime.getTime() - startTime.getTime();

        // Convert milliseconds to hours
        return durationMillis / (1000.0 * 60 * 60);
    }

    @Override
    public Training getTrainingById(Long trainingId) {
        return trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));
    }

    @Override
    public List<Training> getAllTrainingsByUser(Long id) {
        return trainingRepository.findByUserId(id);
    }

    @Override
    public List<Training> getAllTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    @Override
    public void sendTrainingFinishedNotification(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));

        User user = training.getUser();
        String recipientEmail = user.getEmail();

        String senderName = "CapWSB Fitness Tracker";
        String activityName = training.getActivityType().getDisplayName();
        long durationMinutes = (training.getEndTime().getTime() - training.getStartTime().getTime()) / (60 * 1000);

        String subject = "Trening finished";
        String content = String.format(
                " %s,\n\n" +
                        "Congrats! Trening finished!\n\n" +
                        "Specifics:\n" +
                        "- Activity: %s\n" +
                        "- Trening Time: %d minutes\n\n" +
                        "Keep it going!!\n\n" +
                        "%s",
                user.getFirstName(), activityName, durationMinutes, senderName);

        emailSender.send(new EmailDto(recipientEmail, subject, content));
    }
}
