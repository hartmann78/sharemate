package generators;

import net.bytebuddy.utility.RandomString;
import repositoryTests.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestGenerator {
    public Request generateRequest(Long requestorId) {
        return Request.builder()
                .requestorId(requestorId)
                .description(generateDescription())
                .created(LocalDateTime.now())
                .answers(new ArrayList<>())
                .build();
    }

    public String generateDescription() {
        return RandomString.make(32);
    }
}
