package generators;

import net.bytebuddy.utility.RandomString;
import repositoryTests.model.Request;
import repositoryTests.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class RequestGenerator {
    public Request generateRequest(User requestor) {
        return Request.builder()
                .requestor(requestor)
                .description(generateDescription())
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .answers(new ArrayList<>())
                .build();
    }

    public String generateDescription() {
        return RandomString.make(32);
    }
}
