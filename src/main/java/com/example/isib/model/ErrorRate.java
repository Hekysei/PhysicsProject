package com.example.isib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Класс для обработки погрешностей измерений.
 * Содержит логику для генерации случайных ошибок и расчета доверительного интервала.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorRate {
    private boolean active = false;
    private List<Double> measurements = new ArrayList<>();
    private double confidenceLevel = 0.95;
    private double studentCoefficient = 2.0;
    private double systematicError = 0.5;

    private final Random random = new Random();


    public double applyNoise(double trueValue) {
        if (!active || trueValue < 0) return trueValue;

        double randomComponent = -1.0 + (2.0 * random.nextDouble());
        double measuredValue = trueValue + systematicError + randomComponent;
        return Math.round(measuredValue * 100.0) / 100.0;
    }
public void setElementInList(Double element){
  measurements.add(element );
}

    public double calculateAbsoluteError() {

        if (measurements.isEmpty()) return systematicError;
        double sum = 0;
        for (double m : measurements) sum += m;
        double average = sum / measurements.size();

        double varianceSum = 0;
        for (double m : measurements) {
            varianceSum += Math.pow(m - average, 2);
        }

        int n = measurements.size();
        // S - выборочное среднеквадратичное отклонение
        double standardDeviation = Math.sqrt(varianceSum / Math.max(1, n - 1));

        // Стандартная ошибка среднего (S_x = S / sqrt(n))
        double standardError = standardDeviation / Math.sqrt(n);

        // 3. Статистическая погрешность (t * S_x)
        double statisticalError = studentCoefficient * standardError;

        // 4. Полная абсолютная погрешность
        double totalError = Math.sqrt(Math.pow(systematicError, 2) + Math.pow(statisticalError, 2));

        return Math.round(totalError * 100.0) / 100.0;
    }

    public void resetMeasurements() {
        this.measurements.clear();
    }
}
