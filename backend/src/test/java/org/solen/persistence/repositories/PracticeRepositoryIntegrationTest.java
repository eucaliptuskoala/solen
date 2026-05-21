package org.solen.persistence.repositories;

import org.solen.domain.practices.Category;
import org.solen.domain.practices.Practice;
import org.solen.domain.users.User;
import org.solen.persistence.converters.CategoryConverter;
import org.solen.persistence.converters.PracticeConverter;
import org.solen.persistence.converters.UserConverter;
import org.solen.persistence.jparepos.PracticeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({PracticeRepository.class, PracticeConverter.class, UserConverter.class, CategoryConverter.class})
class PracticeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private PracticeJpaRepository jpaRepository;

    @Autowired
    private PracticeRepository practiceRepository;

    User user;
    Category category;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(null)
                .name("Test User")
                .email("email@email.com")
                .password("password")
                .isAdmin(false)
                .build();

        category = Category.builder()
                .id(null)
                .name("Test Category")
                .build();
    }

    private User persistUser() {
        var entity = entityManager.persist(userConverter.convertToEntity(user));
        entityManager.flush();
        return userConverter.convertToDomain(entity);
    }

    private Category persistCategory() {
        var entity = entityManager.persist(categoryConverter.convertToEntity(category));
        entityManager.flush();
        return categoryConverter.convertToDomain(entity);
    }

    private Practice newPractice(User creator, Category category) {
        return Practice.builder()
                .name("Drink Water")
                .description("8 glasses per day")
                .streak(0)
                .lastUpdatedStreak(LocalDateTime.now())
                .thresholdDays(3)
                .creator(creator)
                .category(category)
                .build();
    }

    @Test
    void savePracticeTest() {
        User creator = persistUser();
        Category cat = persistCategory();

        Practice saved = practiceRepository.save(newPractice(creator, cat));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Drink Water");
    }

    @Test
    void findPracticeByIdTest() {
        User creator = persistUser();
        Category cat = persistCategory();

        Practice saved = practiceRepository.save(newPractice(creator, cat));

        Practice found = practiceRepository.findById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Drink Water");
    }

    @Test
    void findAllPracticesTest() {
        User creator = persistUser();
        Category cat = persistCategory();

        List<Practice> practices = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            practices.add(newPractice(creator, cat));
        }

        for(Practice practice : practices){
            practiceRepository.save(practice);
        }

        List<Practice> foundPractices =  practiceRepository.findAll();

        assertThat(foundPractices).hasSameSizeAs(practices);
    }

    @Test
    void findPracticesByNameTest() {
        User creator = persistUser();
        Category cat = persistCategory();

        List<Practice> practices = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            practices.add(newPractice(creator, cat));
        }

        for(Practice practice : practices){
            practiceRepository.save(practice);
        }

        List<Practice> foundPractices =  practiceRepository.findByName("Drink Water");

        assertThat(foundPractices).hasSameSizeAs(practices);
    }

    @Test
    void findPracticesByCreatorIdTest(){
        User creator = persistUser();
        Category cat = persistCategory();

        List<Practice> practices = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            practices.add(newPractice(creator, cat));
        }

        for(Practice practice : practices){
            practiceRepository.save(practice);
        }

        List<Practice> foundPractices =  practiceRepository.findByCreatorId(creator.getId());

        assertThat(foundPractices).hasSameSizeAs(practices);
    }

    @Test
    void deletePracticeTest() {
        User creator = persistUser();
        Category cat = persistCategory();

        Practice saved = practiceRepository.save(newPractice(creator, cat));

        practiceRepository.deleteById(saved.getId());

        assertThat(practiceRepository.findById(saved.getId())).isNull();
        assertThat(jpaRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void existsByPracticeIdAndCreatorEmail(){
        User creator = persistUser();
        Category cat = persistCategory();

        Practice saved = practiceRepository.save(newPractice(creator, cat));
        boolean exists = practiceRepository.existsByPracticeIdAndCreatorEmail(saved.getId(), creator.getEmail());

        assertThat(exists).isTrue();
    }
}