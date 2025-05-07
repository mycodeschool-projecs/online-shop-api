package mycode.online_shop_api.app.users.repository;

import mycode.online_shop_api.app.users.mock.UserMockData;
import mycode.online_shop_api.app.users.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {

        User johnDoe = UserMockData.createUser("john.doe@example.com", "John Doe");
        User savedUser = userRepository.save(johnDoe);


        Optional<User> found = userRepository.findById(savedUser.getId());


        assertTrue(found.isPresent());
        assertEquals("john.doe@example.com", found.get().getEmail());
        assertEquals("John Doe", found.get().getFullName());

        assertNotNull(found.get().getOrders());
    }

    @Test
    void testFindByNonExistentId() {
        Optional<User> found = userRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail() {

        String email = "jane.doe@example.com";
        User janeDoe = UserMockData.createUser(email, "Jane Doe");
        userRepository.save(janeDoe);


        Optional<User> found = userRepository.findByEmail(email);


        assertTrue(found.isPresent());
        assertEquals(email, found.get().getEmail());
        assertEquals("Jane Doe", found.get().getFullName());
        assertNotNull(found.get().getOrders());
    }

    @Test
    void testFindByNonExistentEmail() {

        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");


        assertFalse(found.isPresent());
    }




}