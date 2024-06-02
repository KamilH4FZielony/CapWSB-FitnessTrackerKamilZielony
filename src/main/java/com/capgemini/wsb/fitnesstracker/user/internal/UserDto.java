package com.capgemini.wsb.fitnesstracker.user.internal;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for a user.
 * Used for transferring user data between the logic and presentation layers.
 *
 * @param Id The unique identifier of the user. Can be null if the user is not persisted yet.
 * @param firstName The first name of the user.
 * @param lastName The last name of the user.
 * @param birthdate The birthdate of the user, formatted as "yyyy-MM-dd".
 * @param email The email address of the user.
 */

record UserDto(@Nullable Long Id, String firstName, String lastName,
               @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
               String email) {

}
