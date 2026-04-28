package com.example.isib.kirchhoff.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KirchhoffCircuitResults {
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
    
    public KirchhoffCircuitResults() {}
}
