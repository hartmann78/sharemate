package repositoryTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import generators.UserGenerator;
import repositoryTests.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import repositoryTests.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    UserGenerator userGenerator = new UserGenerator();

    @Test
    void createUser() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());
    }

    @Test
    void getUsers() {
        User user1 = userGenerator.generateUser();
        User user2 = userGenerator.generateUser();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> findAllUsers = userRepository.findAll();
        assertTrue(findAllUsers.contains(user1));
        assertTrue(findAllUsers.contains(user2));
    }
}
