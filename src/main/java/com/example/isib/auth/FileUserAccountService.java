package com.example.isib.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

@Service
public class FileUserAccountService implements UserDetailsService {

  private static final String ROLE = "USER";
  private static final int MAX_USERNAME_LENGTH = 64;
  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final int MAX_PASSWORD_LENGTH = 128;

  private final PasswordEncoder passwordEncoder;
  private final Path usersFilePath;
  private final ReadWriteLock storageLock = new ReentrantReadWriteLock();

  public FileUserAccountService(
      PasswordEncoder passwordEncoder,
      @Value("${app.security.users-file:data/kirchhoff-users.txt}") String usersFilePath) {
    this.passwordEncoder = passwordEncoder;
    this.usersFilePath = Path.of(usersFilePath);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String normalizedUsername = normalizeUsername(username);

    storageLock.readLock().lock();
    try {
      ensureStorageExists();
      try (Stream<String> lines = Files.lines(usersFilePath, StandardCharsets.UTF_8)) {
        return lines
            .map(StoredUserRecord::parse)
            .filter(record -> record != null && record.username().equals(normalizedUsername))
            .findFirst()
            .map(record -> User.withUsername(record.username())
                .password(record.passwordHash())
                .roles(ROLE)
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + normalizedUsername));
      } catch (IOException ex) {
        throw new IllegalStateException("Failed to read users file", ex);
      }
    } finally {
      storageLock.readLock().unlock();
    }
  }

  public void registerUser(String username, String rawPassword) {
    String normalizedUsername = normalizeUsername(username);
    validatePassword(rawPassword);

    storageLock.writeLock().lock();
    try {
      ensureStorageExists();
      List<StoredUserRecord> users = readAllUsers();
      if (users.stream().anyMatch(record -> record.username().equals(normalizedUsername))) {
        throw new IllegalArgumentException("Пользователь с таким именем уже существует.");
      }

      users.add(new StoredUserRecord(normalizedUsername, passwordEncoder.encode(rawPassword)));
      persistUsers(users);
    } finally {
      storageLock.writeLock().unlock();
    }
  }

  private List<StoredUserRecord> readAllUsers() {
    try (Stream<String> lines = Files.lines(usersFilePath, StandardCharsets.UTF_8)) {
      return lines
          .map(StoredUserRecord::parse)
          .filter(record -> record != null)
          .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to read users file", ex);
    }
  }

  private void persistUsers(List<StoredUserRecord> users) {
    String content = users.stream()
        .map(StoredUserRecord::serialize)
        .collect(java.util.stream.Collectors.joining(System.lineSeparator()));
    String normalizedContent = content.isEmpty() ? "" : content + System.lineSeparator();
    Path tempFile = usersFilePath.resolveSibling(usersFilePath.getFileName() + ".tmp");
    try {
      Files.writeString(
          tempFile,
          normalizedContent,
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING,
          StandardOpenOption.WRITE);
      try {
        Files.move(
            tempFile,
            usersFilePath,
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE);
      } catch (IOException atomicMoveEx) {
        Files.move(tempFile, usersFilePath, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to write users file", ex);
    }
  }

  private void ensureStorageExists() {
    try {
      Path parent = usersFilePath.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      if (Files.notExists(usersFilePath)) {
        Files.createFile(usersFilePath);
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to initialize users file", ex);
    }
  }

  private static String normalizeUsername(String username) {
    String normalized = username == null ? "" : username.trim();
    if (normalized.length() < 3) {
      throw new IllegalArgumentException("Имя пользователя должно содержать минимум 3 символа.");
    }
    if (normalized.length() > MAX_USERNAME_LENGTH) {
      throw new IllegalArgumentException("Имя пользователя не должно превышать 64 символа.");
    }
    if (!normalized.matches("[A-Za-z0-9_\\-.]+")) {
      throw new IllegalArgumentException("Используйте только латиницу, цифры, точку, дефис и подчеркивание.");
    }
    return normalized;
  }

  private static void validatePassword(String rawPassword) {
    if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH) {
      throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов.");
    }
    if (rawPassword.length() > MAX_PASSWORD_LENGTH) {
      throw new IllegalArgumentException("Пароль не должен превышать 128 символов.");
    }
  }
}
