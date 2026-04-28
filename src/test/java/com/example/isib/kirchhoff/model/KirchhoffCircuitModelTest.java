package com.example.isib.kirchhoff.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KirchhoffCircuitModelTest {

  private final KirchhoffCircuitModel model = new KirchhoffCircuitModel();

  @Test
  void simulateDoesNotMutateRequestedInput() {
    KirchhoffCircuitData input = new KirchhoffCircuitData();
    input.setErrorsEnabled(true);
    input.setVoltageErrorPercent(50.0);
    input.setResistorErrorPercent(50.0);

    double originalVoltage = input.getV1();
    double originalR1 = input.getR1();

    KirchhoffSimulationOutcome outcome = model.simulate(input);

    assertEquals(originalVoltage, input.getV1());
    assertEquals(originalR1, input.getR1());
    assertNotSame(input, outcome.effectiveData());
    assertNotSame(input, outcome.requestedData());
  }

  @Test
  void simulateReturnsValidKirchhoffChecksForDefaultCircuit() {
    KirchhoffSimulationOutcome outcome = assertDoesNotThrow(
        () -> model.simulate(new KirchhoffCircuitData()));

    assertTrue(outcome.results().getKclValid());
    assertTrue(outcome.results().getKvlValid());
    assertTrue(outcome.results().getTotalResistance() > 0);
  }

  @Test
  void simulateRejectsNonPositiveResistance() {
    KirchhoffCircuitData input = new KirchhoffCircuitData();
    input.setR3(0.0);

    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.simulate(input));

    assertEquals("Сопротивление R3 должно быть больше 0.", ex.getMessage());
  }
}
