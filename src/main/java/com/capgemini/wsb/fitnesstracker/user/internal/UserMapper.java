package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Component;

@Component
class UserMapper {
    /**
     * Converts a {@link User} object to a {@link UserDto}.
     *
     * @param user The user object to be converted.
     * @return The corresponding {@link UserDto} object.
     */
    UserDto toDto(User user) {
        return new UserDto(user.getId(),
                           user.getFirstName(),
                           user.getLastName(),
                           user.getBirthdate(),
                           user.getEmail());
    }
    /**
     * Converts a {@link UserDto} to a {@link User} entity.
     *
     * @param userDto The {@link UserDto} to be converted.
     * @return The corresponding {@link User} entity.
     */
    User toEntity(UserDto userDto) {
        return new User(
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.birthdate(),
                        userDto.email());
    }

}
