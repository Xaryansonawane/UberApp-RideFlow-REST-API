package com.example.UberApp.scheduler;

import com.example.UberApp.model.User;
import com.example.UberApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CancellationResetScheduler {

    private final UserRepository userRepository;

    // 🗓️ Runs on 1st day of every month at 12:00 AM
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthlyCancellations() {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            user.setCancellationCount(0);
            user.setActive(true);
            user.setBlockedUntil(null);
        }

        userRepository.saveAll(users);

        System.out.println("MONTHLY RESET COMPLETED");
    }
}