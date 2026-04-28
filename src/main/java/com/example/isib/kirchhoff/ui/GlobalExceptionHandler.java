package com.example.isib.kirchhoff.ui;

import com.example.isib.kirchhoff.auth.UserRegistrationForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {KirchhoffSimulationController.class, KirchhoffLoginController.class})
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleIllegalState(IllegalStateException ex, HttpServletRequest request, Model model) {
    request.setAttribute(
        "errorMessage",
        "Внутренняя ошибка приложения. Попробуйте повторить действие позже.");
    if (request.getRequestURI().startsWith("/register")) {
      model.addAttribute("registrationForm", new UserRegistrationForm());
      return "kirchhoff/kirchhoff-register";
    }
    return "kirchhoff/kirchhoff-main";
  }
}
