package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toDto_convertsUserToUserDto() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        user.setId(1L); // Set ID, typically set by JPA

        UserDto result = userMapper.toDto(user);

        assertEquals(user.getId(), result.Id());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());
        assertEquals(user.getBirthdate(), result.birthdate());
        assertEquals(user.getEmail(), result.email());
    }

    @Test
    void toEntity_convertsUserDtoToUser() {
        UserDto userDto = new UserDto(null, "Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");

        User result = userMapper.toEntity(userDto);

        assertNull(result.getId());
        assertEquals(userDto.firstName(), result.getFirstName());
        assertEquals(userDto.lastName(), result.getLastName());
        assertEquals(userDto.birthdate(), result.getBirthdate());
        assertEquals(userDto.email(), result.getEmail());
    }
}
