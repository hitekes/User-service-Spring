package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Ответ с информацией о пользователе")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @Schema(
            description = "Уникальный идентификатор пользователя",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long id;

    @Schema(
            description = "Имя пользователя",
            example = "Иван Иванов",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
            description = "Email пользователя",
            example = "ivan@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "Возраст пользователя",
            example = "25",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer age;

    @Schema(
            description = "Дата и время создания пользователя",
            example = "2024-01-15T10:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime createdAt;
}