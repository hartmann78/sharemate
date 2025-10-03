package com.practice.sharemate.generators;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DateGenerator {
    public static final Random random = new Random();

    public LocalDateTime generateRandomDate() {
        int year = randomYear();
        int month = randomMonth();
        int day = randomDay(year, month);
        int hour = randomHour();
        int minute = randomMinute();
        int second = randomSecond();

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    public int randomYear() {
        return random.nextInt(LocalDateTime.now().getYear(), LocalDateTime.now().getYear() + 1);
    }

    public int randomMonth() {
        return random.nextInt(LocalDateTime.now().getMonth().getValue(), 13);
    }

    public int randomDay(int year, int month) {
        List<Integer> days31 = List.of(1, 3, 5, 7, 8, 10, 12);
        List<Integer> days30 = List.of(4, 6, 9, 11);

        if (days31.contains(month)) {
            return random.nextInt(LocalDateTime.now().getDayOfMonth(), 32);
        } else if (days30.contains(month)) {
            return random.nextInt(LocalDateTime.now().getDayOfMonth(), 31);
        } else {
            if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
                return random.nextInt(LocalDateTime.now().getDayOfMonth(), 30);
            } else {
                return random.nextInt(LocalDateTime.now().getDayOfMonth(), 29);
            }
        }
    }

    public int randomHour() {
        return random.nextInt(LocalDateTime.now().getHour(), 24);
    }

    public int randomMinute() {
        return random.nextInt(LocalDateTime.now().getMinute(), 60);
    }

    public int randomSecond() {
        return random.nextInt(LocalDateTime.now().getSecond(), 60);
    }
}