package generators;

import com.practice.sharemate.model.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserGenerator {
    public User generateUser() {
        return User.builder()
                .name(generateName())
                .email(generateEmail())
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public String generateName() {
        return RandomString.make(8);
    }

    public String generateEmail() {
        return RandomString.make(10) + "@mail.com";
    }
}
