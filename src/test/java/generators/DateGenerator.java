package generators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class DateGenerator {
    private static final Random random = new Random();

    public LocalDateTime randomDate() {
        int year = randomYear();
        int month = randomMonth();
        int day = randomDay(year, month);
        int hour = randomHour();
        int minute = randomMinute();
        int second = randomSecond();

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    public int randomYear() {
        return random.nextInt(2020, LocalDateTime.now().getYear());
    }

    public int randomMonth() {
        return random.nextInt(1, 13);
    }

    public int randomDay(int year, int month) {
        List<Integer> days31 = List.of(1, 3, 5, 7, 8, 10, 12);
        List<Integer> days30 = List.of(4, 6, 9, 11);

        if (days31.contains(month)) {
            return random.nextInt(1, 32);
        } else if (days30.contains(month)) {
            return random.nextInt(1, 31);
        } else {
            if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
                return random.nextInt(1, 30);
            } else {
                return random.nextInt(1, 29);
            }
        }
    }

    public int randomHour() {
        return random.nextInt(0, 24);
    }

    public int randomMinute() {
        return random.nextInt(0, 60);
    }

    public int randomSecond() {
        return random.nextInt(0, 60);
    }
}