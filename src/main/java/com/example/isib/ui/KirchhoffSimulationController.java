package com.example.isib.ui;

import org.springframework.stereotype.Controller;
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


@Controller
public class KirchhoffSimulationController {

  @Autowired
  private KirchhoffCircuitModel kirchhoffCircuitModel;

  @GetMapping("/")
  public String MainPage(Model model) {
    KirchhoffCircuitData circuitData = new KirchhoffCircuitData();

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    return "kirchhoff-main";
  }

  @PostMapping("/")
  public String calculateCircuit(@ModelAttribute KirchhoffCircuitData circuitData, Model model) {
    KirchhoffCircuitResults results = kirchhoffCircuitModel.calculate(circuitData);

    model.addAttribute("circuitData", circuitData);
    model.addAttribute("results", results);
    model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    return "kirchhoff-main";
  }
}
