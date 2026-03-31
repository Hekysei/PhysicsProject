package com.example.isib.model;

public class CircuitResults {
    private double totalResistance;
    private double totalCurrent;
    private double voltageR1;
    private double voltageBlock1;
    private double voltageR4;
    private double voltageBlock2;
    private double currentR2;
    private double currentR3;
    private double currentR5;
    private double currentR6;
    private Boolean kclValid;
    private Boolean kvlValid;
    
    public CircuitResults() {}
    
    public double getTotalResistance() { return totalResistance; }
    public void setTotalResistance(double totalResistance) { this.totalResistance = totalResistance; }
    
    public double getTotalCurrent() { return totalCurrent; }
    public void setTotalCurrent(double totalCurrent) { this.totalCurrent = totalCurrent; }
    
    public double getVoltageR1() { return voltageR1; }
    public void setVoltageR1(double voltageR1) { this.voltageR1 = voltageR1; }
    
    public double getVoltageBlock1() { return voltageBlock1; }
    public void setVoltageBlock1(double voltageBlock1) { this.voltageBlock1 = voltageBlock1; }
    
    public double getVoltageR4() { return voltageR4; }
    public void setVoltageR4(double voltageR4) { this.voltageR4 = voltageR4; }
    
    public double getVoltageBlock2() { return voltageBlock2; }
    public void setVoltageBlock2(double voltageBlock2) { this.voltageBlock2 = voltageBlock2; }
    
    public double getCurrentR2() { return currentR2; }
    public void setCurrentR2(double currentR2) { this.currentR2 = currentR2; }
    
    public double getCurrentR3() { return currentR3; }
    public void setCurrentR3(double currentR3) { this.currentR3 = currentR3; }
    
    public double getCurrentR5() { return currentR5; }
    public void setCurrentR5(double currentR5) { this.currentR5 = currentR5; }
    
    public double getCurrentR6() { return currentR6; }
    public void setCurrentR6(double currentR6) { this.currentR6 = currentR6; }
    
    public Boolean getKclValid() { return kclValid; }
    public void setKclValid(Boolean kclValid) { this.kclValid = kclValid; }
    
    public Boolean getKvlValid() { return kvlValid; }
    public void setKvlValid(Boolean kvlValid) { this.kvlValid = kvlValid; }
}
