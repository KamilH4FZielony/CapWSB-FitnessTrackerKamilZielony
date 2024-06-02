package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainingTest {

    private Training training;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        user.setId(1L); // Simulate a user with an ID

        training = new Training();
        training.setId(1L); // Simulate a training with an ID
        training.setUser(user);
        training.setActivityType(ActivityType.RUNNING);
        training.setStartTime(new Date());
        training.setEndTime(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hour later
    }

    @Test
    void testTrainingTimes() {
        assertNotNull(training.getStartTime());
        assertNotNull(training.getEndTime());
        assertEquals(training.getStartTime().getTime() + 3600 * 1000, training.getEndTime().getTime());
    }
}

