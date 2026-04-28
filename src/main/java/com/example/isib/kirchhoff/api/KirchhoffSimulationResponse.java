package com.example.isib.kirchhoff.api;

import com.example.isib.kirchhoff.model.KirchhoffCircuitData;
import com.example.isib.kirchhoff.model.KirchhoffCircuitResults;
import com.example.isib.kirchhoff.model.KirchhoffSimulationOutcome;

public record KirchhoffSimulationResponse(
    KirchhoffCircuitData requestedData,
    KirchhoffCircuitData effectiveData,
    KirchhoffCircuitResults results) {

  public static KirchhoffSimulationResponse from(KirchhoffSimulationOutcome outcome) {
    return new KirchhoffSimulationResponse(
        outcome.requestedData(),
        outcome.effectiveData(),
        outcome.results());
  }
}
