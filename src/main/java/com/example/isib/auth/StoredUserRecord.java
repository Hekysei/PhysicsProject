package com.example.isib.auth;

record StoredUserRecord(String username, String passwordHash) {

  private static final String SEPARATOR = ":";

  static StoredUserRecord parse(String line) {
    if (line == null || line.isBlank()) {
      return null;
    }

    int separatorIndex = line.indexOf(SEPARATOR);
    if (separatorIndex <= 0 || separatorIndex == line.length() - 1) {
      return null;
    }

    String username = line.substring(0, separatorIndex).trim();
    String passwordHash = line.substring(separatorIndex + 1).trim();
    if (username.isEmpty() || passwordHash.isEmpty()) {
      return null;
    }
    return new StoredUserRecord(username, passwordHash);
  }

  String serialize() {
    return username + SEPARATOR + passwordHash;
  }
}
