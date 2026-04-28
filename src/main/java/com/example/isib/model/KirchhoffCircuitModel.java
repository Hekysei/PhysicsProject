package com.example.isib.model;

import org.springframework.stereotype.Component;

@Component
public class KirchhoffCircuitModel {

    private static final double MAX_ERROR_PERCENT = 50.0;

    /**
     * Расчёт электрической цепи по законам Кирхгофа
     * @param data Входные параметры цепи
     * @return Результаты расчёта
     */
    public KirchhoffSimulationOutcome simulate(KirchhoffCircuitData data) {
        validateInput(data);

        KirchhoffCircuitData requestedData = new KirchhoffCircuitData(data);
        KirchhoffCircuitData effectiveData = applyInputError(requestedData);
        KirchhoffCircuitResults results = new KirchhoffCircuitResults();

        double v1 = effectiveData.getV1();
        double r1 = effectiveData.getR1();
        double r2 = effectiveData.getR2();
        double r3 = effectiveData.getR3();
        double r4 = effectiveData.getR4();
        double r5 = effectiveData.getR5();
        double r6 = effectiveData.getR6();

        double rBlock1 = calculateParallelResistance(r2, r3);
        double rBlock2 = calculateParallelResistance(r5, r6);

        results.setTotalResistance(r1 + rBlock1 + r4 + rBlock2);
        if (results.getTotalResistance() <= 0) {
            throw new IllegalArgumentException("Общее сопротивление цепи должно быть больше 0.");
        }

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
        results.setKvlValid(validateKVL(effectiveData, results));

        return new KirchhoffSimulationOutcome(requestedData, effectiveData, results);
    }

    public KirchhoffCircuitResults calculate(KirchhoffCircuitData data) {
        return simulate(data).results();
    }

    private double calculateParallelResistance(Double r1, Double r2) {
        if (r1 == null || r2 == null || r1 <= 0 || r2 <= 0) {
            throw new IllegalArgumentException("Сопротивления параллельной ветви должны быть больше 0.");
        }
        return (r1 * r2) / (r1 + r2);
    }

    /**
     * Проверка первого закона Кирхгофа (KCL) по расчётным токам (погрешности только на входе).
     */
    private Boolean validateKCL(KirchhoffCircuitResults results) {
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
    private Boolean validateKVL(KirchhoffCircuitData data, KirchhoffCircuitResults results) {
        double tolerance = 0.1;
        double totalVoltage = results.getVoltageR1() + results.getVoltageBlock1()
                + results.getVoltageR4() + results.getVoltageBlock2();

        return Math.abs(data.getV1() - totalVoltage) < tolerance;
    }

    /** Проценты в доли; ограничение 0…50% для устойчивости расчёта. */
    private static double clampPercentToFraction(double percent) {
        if (percent < 0) {
            return 0;
        }
        if (percent > MAX_ERROR_PERCENT) {
            return MAX_ERROR_PERCENT / 100.0;
        }
        return percent / 100.0;
    }

    private static void validateInput(KirchhoffCircuitData data) {
        if (data == null) {
            throw new IllegalArgumentException("Данные цепи обязательны.");
        }
        if (Math.abs(data.getV1()) < 0.01) {
            throw new IllegalArgumentException("Напряжение источника не должно быть равно 0.");
        }
        validateResistance("R1", data.getR1());
        validateResistance("R2", data.getR2());
        validateResistance("R3", data.getR3());
        validateResistance("R4", data.getR4());
        validateResistance("R5", data.getR5());
        validateResistance("R6", data.getR6());
    }

    private static void validateResistance(String label, double resistance) {
        if (resistance <= 0) {
            throw new IllegalArgumentException("Сопротивление " + label + " должно быть больше 0.");
        }
    }

    private static KirchhoffCircuitData applyInputError(KirchhoffCircuitData data) {
        KirchhoffErrorRate err = new KirchhoffErrorRate();
        err.setActive(data.isErrorsEnabled());
        err.setVoltageRelativeError(clampPercentToFraction(data.getVoltageErrorPercent()));
        err.setResistorRelativeError(clampPercentToFraction(data.getResistorErrorPercent()));

        KirchhoffCircuitData adjusted = new KirchhoffCircuitData(data);
        adjusted.setV1(err.applyVoltageSourceError(data.getV1()));
        adjusted.setR1(err.applyResistorNominalError(data.getR1()));
        adjusted.setR2(err.applyResistorNominalError(data.getR2()));
        adjusted.setR3(err.applyResistorNominalError(data.getR3()));
        adjusted.setR4(err.applyResistorNominalError(data.getR4()));
        adjusted.setR5(err.applyResistorNominalError(data.getR5()));
        adjusted.setR6(err.applyResistorNominalError(data.getR6()));
        return adjusted;
    }
}
