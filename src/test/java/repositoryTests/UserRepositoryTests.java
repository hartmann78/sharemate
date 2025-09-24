package repositoryTests;

import generators.UserGenerator;
import repositoryTests.model.User;
import org.junit.jupiter.api.Assertions;
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
        User newUser = userRepository.save(user);

        Optional<User> checkUser = userRepository.findById(newUser.getId());

        Assertions.assertTrue(checkUser.isPresent());
        Assertions.assertEquals(checkUser.get(), newUser);
    }

    @Test
    void getUsers() {
        User user1 = userGenerator.generateUser();
        User user2 = userGenerator.generateUser();

        User newUser1 = userRepository.save(user1);
        User newUser2 = userRepository.save(user2);

        List<User> findAllUsers = userRepository.findAll();

        Assertions.assertTrue(findAllUsers.contains(newUser1));
        Assertions.assertTrue(findAllUsers.contains(newUser2));
    }
}
