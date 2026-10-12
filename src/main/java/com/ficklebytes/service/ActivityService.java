package com.ficklebytes.service;


import com.ficklebytes.entity.Activity;
import com.ficklebytes.enums.ActivityName;
import com.ficklebytes.repository.ActivityRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public void registerActivity(final int userId,
        @NonNull final ActivityName activityName, final int targetId) {
        Activity activity = Activity.builder()
            .userId(userId)
            .activity(activityName)
            .timestamp(LocalDateTime.now(ZoneOffset.UTC))
            .targetId(targetId)
            .build();
        activityRepository.save(activity);
    }

    public void registerActivity(final int userId,
        @NonNull final ActivityName activityName) {
        registerActivity(userId, activityName, -1);
    }

}
