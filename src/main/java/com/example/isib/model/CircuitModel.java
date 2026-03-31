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
        
        // Расчёт эквивалентных сопротивлений параллельных блоков
        double rBlock1 = calculateParallelResistance(data.getR2(), data.getR3());
        double rBlock2 = calculateParallelResistance(data.getR5(), data.getR6());
        
        // Общее сопротивление цепи
        results.setTotalResistance(data.getR1() + rBlock1 + data.getR4() + rBlock2);
        
        // Общий ток по закону Ома (I = V/R), переводим в mA
        results.setTotalCurrent((data.getV1() / results.getTotalResistance()) * 1000);
        
        // Напряжения на элементах (U = I * R)
        double currentInAmps = results.getTotalCurrent() / 1000;
        results.setVoltageR1(currentInAmps * data.getR1());
        results.setVoltageBlock1(currentInAmps * rBlock1);
        results.setVoltageR4(currentInAmps * data.getR4());
        results.setVoltageBlock2(currentInAmps * rBlock2);
        
        // Токи через параллельные ветви (I = U/R), переводим в mA
        results.setCurrentR2((results.getVoltageBlock1() / data.getR2()) * 1000);
        results.setCurrentR3((results.getVoltageBlock1() / data.getR3()) * 1000);
        results.setCurrentR5((results.getVoltageBlock2() / data.getR5()) * 1000);
        results.setCurrentR6((results.getVoltageBlock2() / data.getR6()) * 1000);
        
        // Проверка законов Кирхгофа
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
     * Проверка первого закона Кирхгофа (KCL)
     * Сумма токов в узле равна нулю
     */
    private Boolean validateKCL(CircuitResults results) {
        double tolerance = 0.01; // Погрешность 1%
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
        double tolerance = 0.1; // Погрешность 0.1V
        double totalVoltage = results.getVoltageR1() + results.getVoltageBlock1() 
                            + results.getVoltageR4() + results.getVoltageBlock2();
        
        return Math.abs(data.getV1() - totalVoltage) < tolerance;
    }
}
