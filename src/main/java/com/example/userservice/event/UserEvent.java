package com.example.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String eventType;
    private Long userId;
    private String userEmail;
    private String userName;
    private LocalDateTime eventTime;

    public static UserEvent createUserCreatedEvent(Long userId, String userEmail, String userName) {
        return new UserEvent(
                "USER_CREATED",
                userId,
                userEmail,
                userName,
                LocalDateTime.now()
        );
    }

    public static UserEvent createUserDeletedEvent(Long userId, String userEmail, String userName) {
        return new UserEvent(
                "USER_DELETED",
                userId,
                userEmail,
                userName,
                LocalDateTime.now()
        );
    }
}