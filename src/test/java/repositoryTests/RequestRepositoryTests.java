package repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import generators.RequestGenerator;
import generators.UserGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import repositoryTests.model.Request;
import repositoryTests.model.User;
import repositoryTests.repository.RequestRepository;
import repositoryTests.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RequestRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    UserGenerator userGenerator = new UserGenerator();
    RequestGenerator requestGenerator = new RequestGenerator();

    @Test
    void createRequest() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Request request = requestGenerator.generateRequest(user);
        requestRepository.save(request);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Request> checkRequest = requestRepository.findById(request.getId());
        assertTrue(checkRequest.isPresent());
        assertEquals(request, checkRequest.get());
    }

    @Test
    void getUserRequests() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Request request = requestGenerator.generateRequest(user);
        requestRepository.save(request);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        List<Request> userRequests = requestRepository.findAllByRequestorId(user.getId());
        assertTrue(userRequests.contains(request));
    }

    @Test
    void getUserRequestById() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Request request = requestGenerator.generateRequest(user);
        requestRepository.save(request);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Request checkRequest = requestRepository.findByIdAndRequestorId(request.getId(), user.getId());
        assertEquals(request, checkRequest);
    }
}
