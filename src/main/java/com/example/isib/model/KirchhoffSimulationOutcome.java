package com.example.isib.model;

public record KirchhoffSimulationOutcome(
    KirchhoffCircuitData requestedData,
    KirchhoffCircuitData effectiveData,
    KirchhoffCircuitResults results) {
}
