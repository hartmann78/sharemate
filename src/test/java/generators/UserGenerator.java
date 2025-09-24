package generators;

import repositoryTests.model.User;

import java.util.ArrayList;
import java.util.UUID;

public class UserGenerator {
    public User generateUser() {
        return User.builder()
                .name(generateName())
                .email(generateEmail())
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    private String generateName() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateEmail() {
        return UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
    }
}
