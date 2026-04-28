package com.example.isib.kirchhoff.api;

import com.example.isib.kirchhoff.model.KirchhoffCircuitData;
import com.example.isib.kirchhoff.model.KirchhoffCircuitModel;
import com.example.isib.kirchhoff.model.KirchhoffSimulationOutcome;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kirchhoff/api/circuits")
public class KirchhoffSimulationRestController {

  private final KirchhoffCircuitModel kirchhoffCircuitModel;

  public KirchhoffSimulationRestController(KirchhoffCircuitModel kirchhoffCircuitModel) {
    this.kirchhoffCircuitModel = kirchhoffCircuitModel;
  }

  @GetMapping("/defaults")
  public KirchhoffCircuitData defaults() {
    return new KirchhoffCircuitData();
  }

  @PostMapping
  public KirchhoffSimulationResponse calculateCircuit(
      @Valid @RequestBody KirchhoffCircuitData circuitData) {
    KirchhoffSimulationOutcome outcome = kirchhoffCircuitModel.simulate(circuitData);
    return KirchhoffSimulationResponse.from(outcome);
  }
}
