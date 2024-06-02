package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for managing {@link User} entities.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    /**
     * Retrieves all users.
     *
     * @return A list of {@link UserDto} representing all users.
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    /**
     * Retrieves a specific user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The UserDto of the requested user.
     * @throws ResponseStatusException If the user is not found.
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUser(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    /**
     * Creates a new user.
     *
     * @param userDto The UserDto containing the data to create a new user.
     * @return The UserDto of the newly created user.
     */
    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userService.createUser(user);
        return userMapper.toDto(savedUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * Updates an existing user.
     *
     * @param id The ID of the user to update.
     * @param userDto The UserDto containing the updated data.
     * @return The UserDto of the updated user.
     */
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Searches for users by email or age.
     *
     * @param email Optional email to search for.
     * @param age Optional age to search for users older than.
     * @return A list of UserDto matching the search criteria.
     * @throws ResponseStatusException If invalid search parameters are provided.
     */
    @GetMapping("/search")
    public List<UserDto> searchUsers(@RequestParam(required = false) String email, @RequestParam(required = false) Integer age) {
        if (email != null) {
            return userService.findUsersByEmail(email)
                    .stream()
                    .map(userMapper::toDto)
                    .toList();
        } else if (age != null) {
            return userService.findUsersAgeGreaterThan(age)
                    .stream()
                    .map(userMapper::toDto)
                    .toList();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search parameters");
        }
    }
}