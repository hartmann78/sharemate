package generators;

import repositoryTests.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class RequestGenerator {
    public Request generateRequest(Long requestorId) {
        return Request.builder()
                .requestorId(requestorId)
                .description(generateDescription())
                .created(LocalDateTime.now())
                .answers(new ArrayList<>())
                .build();
    }

    private String generateDescription() {
        return UUID.randomUUID().toString();
    }
}
