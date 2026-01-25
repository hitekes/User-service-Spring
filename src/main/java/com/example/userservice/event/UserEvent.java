package com.example.userservice.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Событие пользователя для Kafka")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    @Schema(
            description = "Тип события",
            example = "USER_CREATED или USER_DELETED",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String eventType;

    @Schema(
            description = "ID пользователя",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long userId;

    @Schema(
            description = "Email пользователя",
            example = "ivan@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userEmail;

    @Schema(
            description = "Имя пользователя",
            example = "Иван Иванов",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userName;

    @Schema(
            description = "Время создания события",
            example = "2024-01-15T10:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
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