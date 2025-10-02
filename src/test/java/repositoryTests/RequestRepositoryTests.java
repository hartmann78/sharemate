package repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.repository.UserRepository;
import generators.DateGenerator;
import generators.RequestGenerator;
import generators.UserGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    DateGenerator dateGenerator = new DateGenerator();

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
        request.setCreated(dateGenerator.generateRandomDate());
        requestRepository.save(request);

        Request request2 = requestGenerator.generateRequest(user);
        request2.setCreated(dateGenerator.generateRandomDate());
        requestRepository.save(request2);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        List<Request> userRequests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId());
        assertTrue(userRequests.contains(request));
        assertTrue(userRequests.contains(request2));

        System.out.println(userRequests);
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
