package com.example.isib.model;

public class CircuitResults {
    private Double totalResistance;
    private Double totalCurrent;
    private Double voltageR1;
    private Double voltageBlock1;
    private Double voltageR4;
    private Double voltageBlock2;
    private Double currentR2;
    private Double currentR3;
    private Double currentR5;
    private Double currentR6;
    private Boolean kclValid;
    private Boolean kvlValid;
    
    public CircuitResults() {}
    
    public Double getTotalResistance() { return totalResistance; }
    public void setTotalResistance(Double totalResistance) { this.totalResistance = totalResistance; }
    
    public Double getTotalCurrent() { return totalCurrent; }
    public void setTotalCurrent(Double totalCurrent) { this.totalCurrent = totalCurrent; }
    
    public Double getVoltageR1() { return voltageR1; }
    public void setVoltageR1(Double voltageR1) { this.voltageR1 = voltageR1; }
    
    public Double getVoltageBlock1() { return voltageBlock1; }
    public void setVoltageBlock1(Double voltageBlock1) { this.voltageBlock1 = voltageBlock1; }
    
    public Double getVoltageR4() { return voltageR4; }
    public void setVoltageR4(Double voltageR4) { this.voltageR4 = voltageR4; }
    
    public Double getVoltageBlock2() { return voltageBlock2; }
    public void setVoltageBlock2(Double voltageBlock2) { this.voltageBlock2 = voltageBlock2; }
    
    public Double getCurrentR2() { return currentR2; }
    public void setCurrentR2(Double currentR2) { this.currentR2 = currentR2; }
    
    public Double getCurrentR3() { return currentR3; }
    public void setCurrentR3(Double currentR3) { this.currentR3 = currentR3; }
    
    public Double getCurrentR5() { return currentR5; }
    public void setCurrentR5(Double currentR5) { this.currentR5 = currentR5; }
    
    public Double getCurrentR6() { return currentR6; }
    public void setCurrentR6(Double currentR6) { this.currentR6 = currentR6; }
    
    public Boolean getKclValid() { return kclValid; }
    public void setKclValid(Boolean kclValid) { this.kclValid = kclValid; }
    
    public Boolean getKvlValid() { return kvlValid; }
    public void setKvlValid(Boolean kvlValid) { this.kvlValid = kvlValid; }
}
