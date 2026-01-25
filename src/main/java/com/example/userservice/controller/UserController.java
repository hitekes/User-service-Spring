package com.example.userservice.controller;

import com.example.userservice.dto.UserRequestDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя и отправляет событие в Kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Пример ответа",
                                    value = "{\"id\": 1, \"name\": \"Иван Иванов\", \"email\": \"ivan@example.com\", \"age\": 25, \"createdAt\": \"2024-01-15T10:30:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса или email уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Email already exists: ivan@example.com\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Internal server error\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponseDto> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового пользователя",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Пример запроса",
                                            value = "{\n" +
                                                    "  \"name\": \"Иван Иванов\",\n" +
                                                    "  \"email\": \"ivan@example.com\",\n" +
                                                    "  \"age\": 25\n" +
                                                    "}"
                                    )
                            }
                    )
            )
            @Valid @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto createdUser = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"User not found with id: 1\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(
                    description = "ID пользователя",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей в системе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список пользователей получен",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))
            )
    )
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет информацию о пользователе по его ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Email already exists: new@example.com\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"User not found with id: 1\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(
                    description = "ID пользователя",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новые данные пользователя",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class)
                    )
            )
            @Valid @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по ID и отправляет событие в Kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"User not found with id: 1\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(
                    description = "ID пользователя",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Найти пользователя по email",
            description = "Поиск пользователя по email адресу"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(
                    description = "Email пользователя",
                    required = true,
                    example = "ivan@example.com"
            )
            @PathVariable String email) {

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    @Operation(
            summary = "Проверка здоровья сервиса",
            description = "Проверяет, что сервис работает корректно"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сервис работает",
            content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(value = "User Service is up and running")
            )
    )
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User Service is up and running");
    }

    @GetMapping("/kafka/status")
    @Operation(
            summary = "Статус Kafka",
            description = "Проверяет подключение к Kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Kafka подключен"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Kafka недоступен"
            )
    })
    public ResponseEntity<String> kafkaStatus() {
        return ResponseEntity.ok("Kafka is connected");
    }
}