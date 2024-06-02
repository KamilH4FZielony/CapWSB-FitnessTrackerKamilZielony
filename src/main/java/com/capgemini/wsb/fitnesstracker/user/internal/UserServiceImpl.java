package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

/**
 * Service implementation for managing {@link User} entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;
    /**
     * Creates a new user in the database.
     *
     * @param user The user object to save.
     * @return The saved user object with an assigned unique identifier.
     * @throws IllegalArgumentException if the user already has a database ID.
     */
    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }


    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to be deleted.
     */
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Updates an existing user entity with new data.
     *
     * @param userId The ID of the user to update.
     * @param user The new data for the user.
     * @return The updated {@link User} entity.
     * @throws IllegalArgumentException if the user is not found.
     */
    @Override
    public User updateUser(Long userId, User user) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setBirthdate(user.getBirthdate());
                    existingUser.setEmail(user.getEmail());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Finds users by their email address, ignoring case sensitivity.
     * Partial matches of the email address are also considered.
     *
     * @param email The email address fragment to search for.
     * @return A list of {@link User} entities that contain the given email fragment.
     */
    @Override
    public List<User> findUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    /**
     * Finds users who are older than a specified age.
     * The calculation is based on the current date minus the number of years specified.
     *
     * @param age The age to compare against.
     * @return A list of {@link User} entities who are older than the specified age.
     */
    @Override
    public List<User> findUsersAgeGreaterThan(int age) {
        LocalDate cutDate = LocalDate.now().minusYears(age);
        return userRepository.findUsersAgeGreaterThan(cutDate);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return An {@link Optional} containing the found user or {@link Optional#empty()} if no user is found.
     */
    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Finds a user by their exact email address.
     *
     * @param email The email address of the user to search for.
     * @return An {@link Optional} containing the found user or {@link Optional#empty()} if no user is found.
     */
    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all {@link User} entities.
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}