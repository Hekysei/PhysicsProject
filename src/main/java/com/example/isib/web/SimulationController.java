package com.example.isib.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.example.isib.model.CircuitData;
import com.example.isib.model.CircuitResults;

@Controller
public class SimulationController {

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

    @PostMapping("/main/calculate")
    public String calculateCircuit(@ModelAttribute CircuitData circuitData, Model model) {
        // Расчёт по законам Кирхгофа
        CircuitResults results = calculateKirchhoff(circuitData);
        
        model.addAttribute("circuitData", circuitData);
        model.addAttribute("results", results);
        model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        return "main";
    }

    private CircuitResults calculateKirchhoff(CircuitData data) {
        CircuitResults results = new CircuitResults();
        
        // Расчёт эквивалентных сопротивлений параллельных блоков
        double rBlock1 = (data.getR2() * data.getR3()) / (data.getR2() + data.getR3());
        double rBlock2 = (data.getR5() * data.getR6()) / (data.getR5() + data.getR6());
        
        // Общее сопротивление
        results.setTotalResistance(data.getR1() + rBlock1 + data.getR4() + rBlock2);
        
        // Общий ток (I = V/R)
        results.setTotalCurrent((data.getV1() / results.getTotalResistance()) * 1000); // в mA
        
        // Напряжения на элементах
        results.setVoltageR1(results.getTotalCurrent() / 1000 * data.getR1());
        results.setVoltageBlock1(results.getTotalCurrent() / 1000 * rBlock1);
        results.setVoltageR4(results.getTotalCurrent() / 1000 * data.getR4());
        results.setVoltageBlock2(results.getTotalCurrent() / 1000 * rBlock2);
        
        // Токи через параллельные ветви
        results.setCurrentR2(results.getVoltageBlock1() / data.getR2() * 1000); // в mA
        results.setCurrentR3(results.getVoltageBlock1() / data.getR3() * 1000);
        results.setCurrentR5(results.getVoltageBlock2() / data.getR5() * 1000);
        results.setCurrentR6(results.getVoltageBlock2() / data.getR6() * 1000);
        
        return results;
    }
}
