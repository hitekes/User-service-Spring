package com.example.userservice.service;

import com.example.userservice.dto.UserRequestDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.event.UserEvent;
import com.example.userservice.exception.EmailAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        try {
            User user = new User();
            user.setName(userRequestDto.getName());
            user.setEmail(userRequestDto.getEmail());
            user.setAge(userRequestDto.getAge());

            User savedUser = userRepository.save(user);

            UserEvent event = UserEvent.createUserCreatedEvent(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getName()
            );
            kafkaProducerService.sendUserEvent(event);

            return convertToResponseDto(savedUser);

        } catch (DataIntegrityViolationException e) {
            log.warn("Attempt to create user with existing email: {}", userRequestDto.getEmail(), e);
            throw new EmailAlreadyExistsException(userRequestDto.getEmail(), e);
        } catch (Exception e) {
            log.error("Failed to create user: {}", userRequestDto, e);
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            String oldEmail = user.getEmail();

            user.setName(userRequestDto.getName());
            user.setEmail(userRequestDto.getEmail());
            user.setAge(userRequestDto.getAge());

            User updatedUser = userRepository.save(user);
            if (!oldEmail.equals(userRequestDto.getEmail())) {
                log.info("Email updated for user {}: {} -> {}",
                        id, oldEmail, userRequestDto.getEmail());
            }

            return convertToResponseDto(updatedUser);

        } catch (DataIntegrityViolationException e) {
            log.warn("Attempt to update user with existing email: {}", userRequestDto.getEmail(), e);
            throw new EmailAlreadyExistsException(userRequestDto.getEmail(), e);
        } catch (Exception e) {
            log.error("Failed to update user with id {}: {}", id, userRequestDto, e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        String userEmail = user.getEmail();
        String userName = user.getName();

        userRepository.deleteById(id);
        UserEvent event = UserEvent.createUserDeletedEvent(id, userEmail, userName);
        kafkaProducerService.sendUserEvent(event);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return convertToResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private UserResponseDto convertToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}