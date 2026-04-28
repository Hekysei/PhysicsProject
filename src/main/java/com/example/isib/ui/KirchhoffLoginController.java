package com.example.isib.ui;

import com.example.isib.auth.FileUserAccountService;
import com.example.isib.auth.UserRegistrationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
  public String registerPage(Model model) {
    model.addAttribute("registrationForm", new UserRegistrationForm());
    return "kirchhoff-register";
  }

  @PostMapping("/register")
  public String register(
      @Valid @ModelAttribute("registrationForm") UserRegistrationForm registrationForm,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "kirchhoff-register";
    }

    try {
      fileUserAccountService.registerUser(
          registrationForm.getUsername(),
          registrationForm.getPassword());
      return "redirect:/login?registered";
    } catch (IllegalArgumentException ex) {
      model.addAttribute("errorMessage", ex.getMessage());
      return "kirchhoff-register";
    }
  }
}
