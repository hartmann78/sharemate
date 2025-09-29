package generators;

import net.bytebuddy.utility.RandomString;
import repositoryTests.model.Request;
import repositoryTests.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestGenerator {
    public Request generateRequest(User requestor) {
        return Request.builder()
                .requestor(requestor)
                .description(generateDescription())
                .created(LocalDateTime.now())
                .answers(new ArrayList<>())
                .build();
    }

    public String generateDescription() {
        return RandomString.make(32);
    }
}
