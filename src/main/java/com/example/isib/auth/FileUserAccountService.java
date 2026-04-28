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
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

@Service
public class FileUserAccountService implements UserDetailsService {

  private static final String ROLE = "USER";

  private final PasswordEncoder passwordEncoder;
  private final Path usersFilePath;

  public FileUserAccountService(
      PasswordEncoder passwordEncoder,
      @Value("${app.security.users-file:data/kirchhoff-users.txt}") String usersFilePath) {
    this.passwordEncoder = passwordEncoder;
    this.usersFilePath = Path.of(usersFilePath);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String normalizedUsername = normalizeUsername(username);

    ensureStorageExists();

    try {
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
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to read users file", ex);
    }
  }

  public void registerUser(String username, String rawPassword) {
    String normalizedUsername = normalizeUsername(username);
    validatePassword(rawPassword);
    ensureStorageExists();

    try {
      if (userExists(normalizedUsername)) {
        throw new IllegalArgumentException("Пользователь с таким именем уже существует.");
      }

      StoredUserRecord record = new StoredUserRecord(
          normalizedUsername,
          passwordEncoder.encode(rawPassword));
      Files.writeString(
          usersFilePath,
          record.serialize() + System.lineSeparator(),
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to write users file", ex);
    }
  }

  private boolean userExists(String username) throws IOException {
    try (Stream<String> lines = Files.lines(usersFilePath, StandardCharsets.UTF_8)) {
      return lines
          .map(StoredUserRecord::parse)
          .anyMatch(record -> record != null && record.username().equals(username));
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
    if (!normalized.matches("[A-Za-z0-9_\\-.]+")) {
      throw new IllegalArgumentException("Используйте только латиницу, цифры, точку, дефис и подчеркивание.");
    }
    return normalized;
  }

  private static void validatePassword(String rawPassword) {
    if (rawPassword == null || rawPassword.length() < 4) {
      throw new IllegalArgumentException("Пароль должен содержать минимум 4 символа.");
    }
  }
}
