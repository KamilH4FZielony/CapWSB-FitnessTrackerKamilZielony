package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_createsUser() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Andrzej", createdUser.getFirstName());

        Optional<User> foundUser = userRepository.findById(createdUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Andrzej", foundUser.get().getFirstName());
    }

    @Test
    void deleteUser_deletesUser() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        User createdUser = userRepository.save(user);

        userService.deleteUser(createdUser.getId());

        Optional<User> foundUser = userRepository.findById(createdUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void updateUser_updatesUser() {
        User existingUser = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        existingUser = userRepository.save(existingUser);

        User updateData = new User("Maciej", "Gora", LocalDate.of(1998, 2, 3), "mgora@mail.com");

        User updatedUser = userService.updateUser(existingUser.getId(), updateData);

        assertNotNull(updatedUser);
        assertEquals("Maciej", updatedUser.getFirstName());
        assertEquals("Gora", updatedUser.getLastName());
        assertEquals("mgora@mail.com", updatedUser.getEmail());

        Optional<User> foundUser = userRepository.findById(updatedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Maciej", foundUser.get().getFirstName());
    }

    @Test
    void findUsersByEmail_findsUsersByEmail() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        userRepository.save(user);

        List<User> users = userService.findUsersByEmail("apierdola");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("apierdola@mail.com", users.get(0).getEmail());
    }

    @Test
    void findUsersAgeGreaterThan_findsUsersByAge() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1989, 2, 3), "apierdola@mail.com");
        userRepository.save(user);

        List<User> users = userService.findUsersAgeGreaterThan(30);

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Andrzej", users.get(0).getFirstName());
    }

    @Test
    void getUser_getsUser() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        user = userRepository.save(user);

        Optional<User> foundUser = userService.getUser(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Andrzej", foundUser.get().getFirstName());
    }

    @Test
    void getUserByEmail_getsUserByEmail() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        userRepository.save(user);

        Optional<User> foundUser = userService.getUserByEmail("apierdola@mail.com");

        assertTrue(foundUser.isPresent());
        assertEquals("apierdola@mail.com", foundUser.get().getEmail());
    }

    @Test
    void findAllUsers_findsAllUsers() {
        User user = new User("Andrzej", "Pierdoła", LocalDate.of(1998, 2, 3), "apierdola@mail.com");
        userRepository.save(user);

        List<User> users = userService.findAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Andrzej", users.get(0).getFirstName());
    }
}
