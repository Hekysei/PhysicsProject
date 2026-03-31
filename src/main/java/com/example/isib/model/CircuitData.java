package com.example.isib.model;

public class CircuitData {
    private double v1 = 12.0;
    private double r1 = 100.0;
    private double r2 = 200.0;
    private double r3 = 200.0;
    private double r4 = 100.0;
    private double r5 = 500.0;
    private double r6 = 500.0;
    
    public CircuitData() {}
    
    public CircuitData(double v1, double r1, double r2, double r3, 
                       double r4, double r5, double r6) {
        this.v1 = v1;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
        this.r6 = r6;
    }
    
    public double getV1() { return v1; }
    public void setV1(double v1) { this.v1 = v1; }
    
    public double getR1() { return r1; }
    public void setR1(double r1) { this.r1 = r1; }
    
    public double getR2() { return r2; }
    public void setR2(double r2) { this.r2 = r2; }
    
    public double getR3() { return r3; }
    public void setR3(double r3) { this.r3 = r3; }
    
    public double getR4() { return r4; }
    public void setR4(double r4) { this.r4 = r4; }
    
    public double getR5() { return r5; }
    public void setR5(double r5) { this.r5 = r5; }
    
    public double getR6() { return r6; }
    public void setR6(double r6) { this.r6 = r6; }
}
