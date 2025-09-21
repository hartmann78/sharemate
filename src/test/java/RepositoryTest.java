import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

// Выдаёт ошибку
// java.lang.IllegalStateException: Unable to find a @SpringBootConfiguration by searching packages upwards from the test.
// You can use @ContextConfiguration, @SpringBootTest(classes=...) or other Spring Test supported mechanisms
// to explicitly declare the configuration classes to load. Classes annotated with @TestConfiguration are not considered.

@DataJpaTest
public class RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void addUser() {
        User user = User.builder()
                .name("User 1")
                .email("someone1234@mail.com")
                .build();

        userRepository.save(user);

        Optional<User> findUser = userRepository.findById(1L);

        Assertions.assertTrue(findUser.isPresent());
        Assertions.assertEquals("User 1", findUser.get().getName());
    }
}
