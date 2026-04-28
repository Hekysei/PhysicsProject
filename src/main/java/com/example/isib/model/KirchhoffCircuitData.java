package com.example.isib.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KirchhoffCircuitData {
  @DecimalMin(value = "-1000000.0", inclusive = true, message = "Напряжение должно быть в разумном диапазоне.")
  @DecimalMax(value = "1000000.0", inclusive = true, message = "Напряжение должно быть в разумном диапазоне.")
  private double v1 = 12.0;
  @DecimalMin(value = "0.01", message = "Сопротивление R1 должно быть больше 0.")
  private double r1 = 100.0;
  @DecimalMin(value = "0.01", message = "Сопротивление R2 должно быть больше 0.")
  private double r2 = 200.0;
  @DecimalMin(value = "0.01", message = "Сопротивление R3 должно быть больше 0.")
  private double r3 = 200.0;
  @DecimalMin(value = "0.01", message = "Сопротивление R4 должно быть больше 0.")
  private double r4 = 100.0;
  @DecimalMin(value = "0.01", message = "Сопротивление R5 должно быть больше 0.")
  private double r5 = 500.0;
  @DecimalMin(value = "0.01", message = "Сопротивление R6 должно быть больше 0.")
  private double r6 = 500.0;

  /** Включить случайный разброс входных U и R по заданным процентам. */
  private boolean errorsEnabled = true;
  /** Относительная половина интервала для ЭДС, % (например 1 → ±1% к номиналу U). */
  @DecimalMin(value = "0.0", message = "Погрешность источника не может быть отрицательной.")
  @DecimalMax(value = "50.0", message = "Погрешность источника не должна превышать 50%.")
  private double voltageErrorPercent = 1.0;
  /** То же для номиналов резисторов, % (например 2 → ±2%). */
  @DecimalMin(value = "0.0", message = "Погрешность резисторов не может быть отрицательной.")
  @DecimalMax(value = "50.0", message = "Погрешность резисторов не должна превышать 50%.")
  private double resistorErrorPercent = 2.0;

  public KirchhoffCircuitData() {
  }

  public KirchhoffCircuitData(KirchhoffCircuitData other) {
    this(
        other.getV1(),
        other.getR1(),
        other.getR2(),
        other.getR3(),
        other.getR4(),
        other.getR5(),
        other.getR6());
    this.errorsEnabled = other.isErrorsEnabled();
    this.voltageErrorPercent = other.getVoltageErrorPercent();
    this.resistorErrorPercent = other.getResistorErrorPercent();
  }

  public KirchhoffCircuitData(double V1, double R1, double R2, double R3,
      double R4, double R5, double R6) {
    this.v1 = V1;
    this.r1 = R1;
    this.r2 = R2;
    this.r3 = R3;
    this.r4 = R4;
    this.r5 = R5;
    this.r6 = R6;
  }

  @AssertTrue(message = "Напряжение источника не должно быть равно 0.")
  public boolean isVoltageNonZero() {
    return Math.abs(v1) >= 0.01;
  }
}
