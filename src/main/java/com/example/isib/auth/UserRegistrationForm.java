package com.example.isib.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationForm {

  @NotBlank(message = "Имя пользователя обязательно.")
  @Size(min = 3, max = 64, message = "Имя пользователя должно содержать от 3 до 64 символов.")
  @Pattern(
      regexp = "[A-Za-z0-9_\\-.]+",
      message = "Используйте только латиницу, цифры, точку, дефис и подчеркивание.")
  private String username = "";

  @NotBlank(message = "Пароль обязателен.")
  @Size(min = 8, max = 128, message = "Пароль должен содержать от 8 до 128 символов.")
  private String password = "";

  @NotBlank(message = "Подтверждение пароля обязательно.")
  private String confirmPassword = "";

  @AssertTrue(message = "Пароли не совпадают.")
  public boolean isPasswordConfirmed() {
    return password != null && password.equals(confirmPassword);
  }
}
