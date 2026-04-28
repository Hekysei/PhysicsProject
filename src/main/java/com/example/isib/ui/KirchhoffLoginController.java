package com.example.isib.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KirchhoffLoginController {

  @GetMapping("/login")
  public String login() {
    return "kirchhoff-login";
  }
}
