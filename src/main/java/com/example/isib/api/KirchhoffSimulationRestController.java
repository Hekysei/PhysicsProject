package com.example.isib.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.isib.model.KirchhoffCircuitData;
import com.example.isib.model.KirchhoffCircuitResults;
import com.example.isib.model.KirchhoffCircuitModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
public class KirchhoffSimulationRestController {

  @Autowired
  private KirchhoffCircuitModel kirchhoffCircuitModel;

  @GetMapping("/rest_main")
  public String MainPage(Model model) {
    KirchhoffCircuitData circuitData = new KirchhoffCircuitData();

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    return "kirchhoff-main";
  }

  @PostMapping("/rest_main")
  public String calculateCircuit(@ModelAttribute KirchhoffCircuitData circuitData, Model model) {
    KirchhoffCircuitResults results = kirchhoffCircuitModel.calculate(circuitData);

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("results", results);
    model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    return "kirchhoff-main";
  }
}
