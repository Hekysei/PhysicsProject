package com.example.isib.kirchhoff.ui;

import com.example.isib.kirchhoff.model.KirchhoffCircuitData;
import com.example.isib.kirchhoff.model.KirchhoffCircuitModel;
import com.example.isib.kirchhoff.model.KirchhoffSimulationOutcome;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KirchhoffSimulationController {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
  private final KirchhoffCircuitModel kirchhoffCircuitModel;

  public KirchhoffSimulationController(KirchhoffCircuitModel kirchhoffCircuitModel) {
    this.kirchhoffCircuitModel = kirchhoffCircuitModel;
  }

  @ModelAttribute("date")
  public String date() {
    return LocalDate.now().format(DATE_FORMATTER);
  }

  @GetMapping("/kirchhoff")
  public String mainPage(Model model) {
    model.addAttribute("circuitData", new KirchhoffCircuitData());
    return "kirchhoff/kirchhoff-main";
  }

  @PostMapping("/kirchhoff")
  public String calculateCircuit(
      @Valid @ModelAttribute("circuitData") KirchhoffCircuitData circuitData,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "kirchhoff/kirchhoff-main";
    }

    KirchhoffSimulationOutcome outcome = kirchhoffCircuitModel.simulate(circuitData);

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("effectiveCircuitData", outcome.effectiveData());
    model.addAttribute("results", outcome.results());
    return "kirchhoff/kirchhoff-main";
  }
}
