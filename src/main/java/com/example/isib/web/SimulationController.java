package com.example.isib.web;

import org.springframework.stereotype.Controller;
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

@Controller
public class SimulationController {
    
    @Autowired
    private CircuitModel circuitModel;
    
    @GetMapping("/main")
    public String MainPage(Model model) {
        // Инициализация формы с значениями по умолчанию
        CircuitData circuitData = new CircuitData();
        circuitData.setV1(12.0);
        circuitData.setR1(100.0);
        circuitData.setR2(200.0);
        circuitData.setR3(200.0);
        circuitData.setR4(100.0);
        circuitData.setR5(500.0);
        circuitData.setR6(500.0);
        
        model.addAttribute("circuitData", circuitData);
        model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        return "main";
    }

    @PostMapping("/main")
    public String calculateCircuit(@ModelAttribute CircuitData circuitData, Model model) {
        // Передача данных в модель для расчёта
        CircuitResults results = circuitModel.calculate(circuitData);
        
        model.addAttribute("circuitData", circuitData);
        model.addAttribute("results", results);
        model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        return "main";
    }
}
