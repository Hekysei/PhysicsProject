package com.example.isib.model;

import org.springframework.stereotype.Component;

@Component
public class CircuitModel {

    /**
     * Расчёт электрической цепи по законам Кирхгофа
     * @param data Входные параметры цепи
     * @return Результаты расчёта
     */
    public CircuitResults calculate(CircuitData data) {
        CircuitResults results = new CircuitResults();
        ErrorRate err = new ErrorRate();
        err.setActive(true);

        double v1 = err.applyVoltageSourceError(data.getV1());
        double r1 = err.applyResistorNominalError(data.getR1());
        double r2 = err.applyResistorNominalError(data.getR2());
        double r3 = err.applyResistorNominalError(data.getR3());
        double r4 = err.applyResistorNominalError(data.getR4());
        double r5 = err.applyResistorNominalError(data.getR5());
        double r6 = err.applyResistorNominalError(data.getR6());

        data.setV1(v1);
        data.setR1(r1);
        data.setR2(r2);
        data.setR3(r3);
        data.setR4(r4);
        data.setR5(r5);
        data.setR6(r6);

        double rBlock1 = calculateParallelResistance(r2, r3);
        double rBlock2 = calculateParallelResistance(r5, r6);

        results.setTotalResistance(r1 + rBlock1 + r4 + rBlock2);

        double totalCurrentAmps = v1 / results.getTotalResistance();
        results.setTotalCurrent(totalCurrentAmps * 1000);

        results.setVoltageR1(totalCurrentAmps * r1);
        results.setVoltageBlock1(totalCurrentAmps * rBlock1);
        results.setVoltageR4(totalCurrentAmps * r4);
        results.setVoltageBlock2(totalCurrentAmps * rBlock2);

        results.setCurrentR2((results.getVoltageBlock1() / r2) * 1000);
        results.setCurrentR3((results.getVoltageBlock1() / r3) * 1000);
        results.setCurrentR5((results.getVoltageBlock2() / r5) * 1000);
        results.setCurrentR6((results.getVoltageBlock2() / r6) * 1000);

        results.setKclValid(validateKCL(results));
        results.setKvlValid(validateKVL(data, results));

        return results;
    }

    private double calculateParallelResistance(Double r1, Double r2) {
        if (r1 == null || r2 == null || r1 == 0 || r2 == 0) {
            return 0;
        }
        return (r1 * r2) / (r1 + r2);
    }

    /**
     * Проверка первого закона Кирхгофа (KCL) по расчётным токам (погрешности только на входе).
     */
    private Boolean validateKCL(CircuitResults results) {
        double tolerance = 0.01;
        double inputCurrent = results.getTotalCurrent();
        double branchCurrent1 = results.getCurrentR2() + results.getCurrentR3();
        double branchCurrent2 = results.getCurrentR5() + results.getCurrentR6();

        boolean kcl1 = Math.abs(inputCurrent - branchCurrent1) < tolerance;
        boolean kcl2 = Math.abs(inputCurrent - branchCurrent2) < tolerance;

        return kcl1 && kcl2;
    }

    /**
     * Проверка второго закона Кирхгофа (KVL)
     * Сумма напряжений в контуре равна напряжению источника
     */
    private Boolean validateKVL(CircuitData data, CircuitResults results) {
        double tolerance = 0.1;
        double totalVoltage = results.getVoltageR1() + results.getVoltageBlock1()
                + results.getVoltageR4() + results.getVoltageBlock2();

        return Math.abs(data.getV1() - totalVoltage) < tolerance;
    }
}
