package com.example.isib.ui;

import com.example.isib.auth.FileUserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KirchhoffLoginController {

  private final FileUserAccountService fileUserAccountService;

  public KirchhoffLoginController(FileUserAccountService fileUserAccountService) {
    this.fileUserAccountService = fileUserAccountService;
  }

  @GetMapping("/login")
  public String login() {
    return "kirchhoff-login";
  }

  @GetMapping("/register")
  public String registerPage() {
    return "kirchhoff-register";
  }

  @PostMapping("/register")
  public String register(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam("confirmPassword") String confirmPassword,
      Model model) {
    if (!password.equals(confirmPassword)) {
      model.addAttribute("errorMessage", "Пароли не совпадают.");
      model.addAttribute("username", username);
      return "kirchhoff-register";
    }

    try {
      fileUserAccountService.registerUser(username, password);
      return "redirect:/login?registered";
    } catch (IllegalArgumentException ex) {
      model.addAttribute("errorMessage", ex.getMessage());
      model.addAttribute("username", username);
      return "kirchhoff-register";
    }
  }
}
