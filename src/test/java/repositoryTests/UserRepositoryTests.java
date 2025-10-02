package repositoryTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.UserRepository;
import generators.UserGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    UserGenerator userGenerator = new UserGenerator();

    @Test
    void createUserAndFindById() {
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

    @Test
    void updateUser() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        User updateUser = userGenerator.generateUser();

        while (user.getEmail().equals(updateUser.getEmail())) {
            updateUser.setEmail(userGenerator.generateEmail());
        }

        if (updateUser.getName() != null && !updateUser.getName().equals(user.getName())) {
            user.setName(updateUser.getName());
        }

        if (updateUser.getEmail() != null && !updateUser.getEmail().equals(user.getEmail())) {
            user.setEmail(updateUser.getEmail());
        }

        userRepository.save(user);

        Optional<User> checkUpdatedUser = userRepository.findById(user.getId());
        assertTrue(checkUpdatedUser.isPresent());
        assertEquals(user, checkUpdatedUser.get());
    }

    @Test
    void deleteUser() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isEmpty());
    }
}
