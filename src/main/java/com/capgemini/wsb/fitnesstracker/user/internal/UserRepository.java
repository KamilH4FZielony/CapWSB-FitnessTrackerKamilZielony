package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;

import java.util.Objects;
import java.util.Optional;
import java.util.List;


interface UserRepository extends JpaRepository<User, Long> {

    default Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findFirst();
    }

    List<User> findByEmailContainingIgnoreCase(String email);

    /**
     * Finds users who are older than a certain age.
     *
     * @param cutDate The date before which the user must have been born to be included in the results.
     * @return A list of {@link User} entities that match the search criteria.
     */
    @Query("SELECT u FROM User u WHERE u.birthdate <= :cutDate")
    List<User> findUsersAgeGreaterThan(LocalDate cutDate);
}
