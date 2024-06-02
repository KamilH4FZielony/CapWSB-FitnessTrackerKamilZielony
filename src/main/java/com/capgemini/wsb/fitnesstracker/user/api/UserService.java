package com.capgemini.wsb.fitnesstracker.user.api;

import java.util.Optional;
import java.util.List;
/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);

    Optional<User> getUser(Long userId);

    Optional<User> getUserByEmail(String email);

    List<User> findAllUsers();

    void deleteUser(Long userId);

    User updateUser(Long userId, User user);

    List<User> findUsersByEmail(String email);

    List<User> findUsersAgeGreaterThan(int age);
}
