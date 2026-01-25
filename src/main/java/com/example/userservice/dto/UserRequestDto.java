package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание или обновление пользователя")
public class UserRequestDto {

    @Schema(
            description = "Имя пользователя",
            example = "Иван Иванов",
            minLength = 2,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Schema(
            description = "Email пользователя",
            example = "ivan@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(
            description = "Возраст пользователя",
            example = "25",
            minimum = "1",
            maximum = "150",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Age is mandatory")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must be at most 150")
    private Integer age;

    public UserRequestDto() {}

    public UserRequestDto(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}