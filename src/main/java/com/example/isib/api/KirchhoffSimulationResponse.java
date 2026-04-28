package com.example.isib.api;

import com.example.isib.model.KirchhoffCircuitData;
import com.example.isib.model.KirchhoffCircuitResults;
import com.example.isib.model.KirchhoffSimulationOutcome;

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
