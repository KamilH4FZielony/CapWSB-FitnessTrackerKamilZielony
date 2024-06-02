package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link Training} entities and {@link TrainingDto}.
 */
@Component
@RequiredArgsConstructor
class TrainingMapper {

    private final UserProvider userProvider;

    /**
     * Converts a {@link Training} object to a {@link TrainingDto}.
     *
     * @param training The training object to be converted.
     * @return The corresponding {@link TrainingDto} object.
     */
    TrainingDto toDto(Training training) {
        return new TrainingDto(
                training.getId(),
                training.getUser().getId(),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }

    /**
     * Converts a {@link TrainingDto} to a {@link Training} entity.
     *
     * @param trainingDto The {@link TrainingDto} to be converted.
     * @return The corresponding {@link Training} entity.
     */
    Training toEntity(TrainingDto trainingDto) {
        return userProvider.getUser(trainingDto.getUserId())
                .map(user -> new Training(
                        user,
                        trainingDto.getStartTime(),
                        trainingDto.getEndTime(),
                        trainingDto.getActivityType(),
                        trainingDto.getDistance(),
                        trainingDto.getAverageSpeed()
                ))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}