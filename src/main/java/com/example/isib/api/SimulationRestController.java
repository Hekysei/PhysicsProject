package com.example.isib.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.isib.model.CircuitData;
import com.example.isib.model.CircuitResults;
import com.example.isib.model.CircuitModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
public class SimulationRestController {

  @Autowired
  private CircuitModel circuitModel;

  @GetMapping("/rest_main")
  public String MainPage(Model model) {
    CircuitData circuitData = new CircuitData();

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    return "main";
  }

  @PostMapping("/rest_main")
  public String calculateCircuit(@ModelAttribute CircuitData circuitData, Model model) {
    CircuitResults results = circuitModel.calculate(circuitData);

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("results", results);
    model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    return "main";
  }
}
