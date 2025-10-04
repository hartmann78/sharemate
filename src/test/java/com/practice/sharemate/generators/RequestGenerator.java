package com.practice.sharemate.generators;

import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Component
public class RequestGenerator {
    public Request generateRequest() {
        return Request.builder()
                .description(generateDescription())
                .updated(null)
                .build();
    }

    public Request generateRequest(User requestor) {
        return Request.builder()
                .requestor(requestor)
                .description(generateDescription())
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .updated(null)
                .answers(new ArrayList<>())
                .build();
    }

    public String generateDescription() {
        return RandomString.make(32);
    }
}
