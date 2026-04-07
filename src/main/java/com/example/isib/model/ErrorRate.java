package com.example.isib.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.random.RandomGenerator;

/**
 * Погрешности входных данных цепи (как в методичке):
 * <ul>
 *   <li>источник ЭДС — 1% от номинала напряжения;</li>
 *   <li>номиналы резисторов — ±2%.</li>
 * </ul>
 * При {@code active == false} значения не изменяются.
 */
@NoArgsConstructor
@Getter
@Setter
public class ErrorRate {

    private boolean active = false;

    /** Относительная погрешность напряжения источника (1% → 0.01). */
    private double voltageRelativeError = 0.01;

    /** Средняя относительная погрешность номинала резистора (2% → 0.02). */
    private double resistorRelativeError = 0.02;

    private final RandomGenerator random = RandomGenerator.getDefault();

    /**
     * Моделирует относительную погрешность источника напряжения (±1% к номиналу).
     */
    public double applyVoltageSourceError(double nominalVolts) {
        if (!active || nominalVolts <= 0) {
            return nominalVolts;
        }
        double delta = (random.nextDouble() * 2 - 1) * voltageRelativeError * nominalVolts;
        return round2(nominalVolts + delta);
    }

    /**
     * Моделирует среднюю относительную погрешность номинала резистора (±2%).
     */
    public double applyResistorNominalError(double nominalOhms) {
        if (!active || nominalOhms <= 0) {
            return nominalOhms;
        }
        double factor = 1 + (random.nextDouble() * 2 - 1) * resistorRelativeError;
        return round2(nominalOhms * factor);
    }

    private static double round2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
