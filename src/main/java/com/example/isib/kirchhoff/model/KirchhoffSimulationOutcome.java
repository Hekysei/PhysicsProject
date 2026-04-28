package com.example.isib.kirchhoff.model;

public record KirchhoffSimulationOutcome(
    KirchhoffCircuitData requestedData,
    KirchhoffCircuitData effectiveData,
    KirchhoffCircuitResults results) {
}
