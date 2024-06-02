package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceImplTest {

    private TrainingServiceImpl trainingService;
    private TrainingRepository trainingRepository;
    private UserProvider userProvider;
    private EmailSender emailSender;

    private Training training;
    private User user;

    @BeforeEach
    void setUp() {
        trainingRepository = new TrainingRepository() {
            private final Map<Long, Training> database = new HashMap<>();
            private long currentId = 1L;

            @Override
            public List<Training> findAll() {
                return new ArrayList<>(database.values());
            }

            @Override
            public List<Training> findByUserId(Long userId) {
                List<Training> result = new ArrayList<>();
                for (Training training : database.values()) {
                    if (training.getUser().getId().equals(userId)) {
                        result.add(training);
                    }
                }
                return result;
            }

            @Override
            public List<Training> findByEndTimeAfter(Date date) {
                List<Training> result = new ArrayList<>();
                for (Training training : database.values()) {
                    if (training.getEndTime().after(date)) {
                        result.add(training);
                    }
                }
                return result;
            }

            @Override
            public List<Training> findByActivityType(ActivityType activityType) {
                List<Training> result = new ArrayList<>();
                for (Training training : database.values()) {
                    if (training.getActivityType() == activityType) {
                        result.add(training);
                    }
                }
                return result;
            }

            @Override
            public Optional<Training> findById(Long id) {
                return Optional.ofNullable(database.get(id));
            }

            @Override
            public <S extends Training> S save(S entity) {
                if (entity.getId() == null) {
                    entity.setId(currentId++);
                }
                database.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public void deleteById(Long id) {
                database.remove(id);
            }
            @Override
            public Training getReferenceById(Long aLong) {
                return findById(aLong).orElse(null);
            }

            // Other methods remain unimplemented for brevity

            @Override
            public Page<Training> findAll(Pageable pageable) {
                // Implement pagination for tests
                List<Training> trainingList = new ArrayList<>(database.values());
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), trainingList.size());
                List<Training> paginatedList = trainingList.subList(start, end);
                return new PageImpl<>(paginatedList, pageable, trainingList.size());
            }

            @Override
            public <S extends Training> List<S> saveAll(Iterable<S> entities) {
                // Dummy implementation
                return null;
            }

            @Override
            public <S extends Training> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Training> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public <S extends Training, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }

            @Override
            public <S extends Training> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public <S extends Training> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Training> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public <S extends Training> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public List<Training> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public void delete(Training entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Training> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Training> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends Training> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<Training> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Training getOne(Long aLong) {
                return null;
            }

            @Override
            public Training getById(Long aLong) {
                return null;
            }

            @Override
            public List<Training> findAll(Sort sort) {
                return null;
            }

        };

        userProvider = new UserProvider() {
            @Override
            public Optional<User> getUser(Long userId) {
                User user = new User();
                user.setId(userId);
                return Optional.of(user);
            }

            @Override
            public Optional<User> getUserByEmail(String email) {
                return Optional.empty();
            }

            @Override
            public List<User> findAllUsers() {
                return Collections.emptyList();
            }
        };

        emailSender = emailDto -> {

        };

        trainingService = new TrainingServiceImpl(trainingRepository, userProvider, emailSender);

        user = new User();
        user.setId(1L);

        training = new Training(user, new Date(), new Date(), ActivityType.RUNNING, 10.0, 5.0);
        training.setId(1L);
        trainingRepository.save(training);
    }

    @Test
    void createTraining_createsTraining() {
        Training createdTraining = trainingService.createTraining(training);

        assertNotNull(createdTraining);
        assertEquals(user.getId(), createdTraining.getUser().getId());
    }

    @Test
    void getTrainingById_returnsTrainingWhenFound() {
        trainingRepository.save(training);

        Training foundTraining = trainingService.getTrainingById(training.getId());

        assertNotNull(foundTraining);
        assertEquals(training.getId(), foundTraining.getId());
    }

    @Test
    void getTrainingById_throwsExceptionWhenNotFound() {
        assertThrows(TrainingNotFoundException.class, () -> trainingService.getTrainingById(999L));
    }

    @Test
    void updateTraining_updatesTraining() {
        trainingRepository.save(training);

        training.setActivityType(ActivityType.CYCLING);
        Training updatedTraining = trainingService.updateTraining(training.getId(), training);

        assertNotNull(updatedTraining);
        assertEquals(ActivityType.CYCLING, updatedTraining.getActivityType());
    }

    @Test
    void getAllTrainings_returnsAllTrainings() {
        trainingRepository.save(training);

        List<Training> trainings = trainingService.getAllTrainings();

        assertFalse(trainings.isEmpty());
    }

    @Test
    void getAllTrainingsByUser_returnsTrainingsByUser() {
        trainingRepository.save(training);

        List<Training> trainings = trainingService.getAllTrainingsByUser(user.getId());

        assertFalse(trainings.isEmpty());
    }

    @Test
    void getAllTrainingsByActivityType_returnsTrainingsByActivityType() {
        trainingRepository.save(training);

        List<Training> trainings = trainingService.getAllTrainingsByActivityType(ActivityType.RUNNING);

        assertFalse(trainings.isEmpty());
    }
}
