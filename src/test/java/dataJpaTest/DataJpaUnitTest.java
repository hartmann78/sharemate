package dataJpaTest;

import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@ComponentScan({"com.practice.sharemate"})

public class DataJpaUnitTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void createUser() {
        User user = generateUser();
        User newUser = userRepository.save(user);

        Optional<User> check = userRepository.findById(1L);
        Assertions.assertTrue(check.isPresent());
        Assertions.assertEquals(check.get(), newUser);
    }

    private User generateUser() {
        return User.builder()
                .name(generate())
                .email(generate() + "@mail.com")
                .build();
    }

    private String generate() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
