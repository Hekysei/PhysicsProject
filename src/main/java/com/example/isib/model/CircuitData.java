package com.example.isib.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircuitData {
  private double V1 = 12.0;
  private double R1 = 100.0;
  private double R2 = 200.0;
  private double R3 = 200.0;
  private double R4 = 100.0;
  private double R5 = 500.0;
  private double R6 = 500.0;

  /** Включить случайный разброс входных U и R по заданным процентам. */
  private boolean errorsEnabled = true;
  /** Относительная половина интервала для ЭДС, % (например 1 → ±1% к номиналу U). */
  private double voltageErrorPercent = 1.0;
  /** То же для номиналов резисторов, % (например 2 → ±2%). */
  private double resistorErrorPercent = 2.0;

  public CircuitData() {
  }

  public CircuitData(double V1, double R1, double R2, double R3,
      double R4, double R5, double R6) {
    this.V1 = V1;
    this.R1 = R1;
    this.R2 = R2;
    this.R3 = R3;
    this.R4 = R4;
    this.R5 = R5;
    this.R6 = R6;
  }

}
