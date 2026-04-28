package com.example.isib.api;

import com.example.isib.model.KirchhoffCircuitData;
import com.example.isib.model.KirchhoffCircuitModel;
import com.example.isib.model.KirchhoffSimulationOutcome;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/circuits")
public class KirchhoffSimulationRestController {

  private final KirchhoffCircuitModel kirchhoffCircuitModel;

  public KirchhoffSimulationRestController(KirchhoffCircuitModel kirchhoffCircuitModel) {
    this.kirchhoffCircuitModel = kirchhoffCircuitModel;
  }

  @GetMapping("/kirchhoff/defaults")
  public KirchhoffCircuitData defaults() {
    return new KirchhoffCircuitData();
  }

  @PostMapping("/kirchhoff")
  public KirchhoffSimulationResponse calculateCircuit(
      @Valid @RequestBody KirchhoffCircuitData circuitData) {
    KirchhoffSimulationOutcome outcome = kirchhoffCircuitModel.simulate(circuitData);
    return KirchhoffSimulationResponse.from(outcome);
  }
}
