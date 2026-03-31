package com.example.isib.model;

public class CircuitData {
    private Double v1 = 12.0;
    private Double r1 = 100.0;
    private Double r2 = 200.0;
    private Double r3 = 200.0;
    private Double r4 = 100.0;
    private Double r5 = 500.0;
    private Double r6 = 500.0;
    
    public CircuitData() {}
    
    public CircuitData(Double v1, Double r1, Double r2, Double r3, 
                       Double r4, Double r5, Double r6) {
        this.v1 = v1;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
        this.r6 = r6;
    }
    
    public Double getV1() { return v1; }
    public void setV1(Double v1) { this.v1 = v1; }
    
    public Double getR1() { return r1; }
    public void setR1(Double r1) { this.r1 = r1; }
    
    public Double getR2() { return r2; }
    public void setR2(Double r2) { this.r2 = r2; }
    
    public Double getR3() { return r3; }
    public void setR3(Double r3) { this.r3 = r3; }
    
    public Double getR4() { return r4; }
    public void setR4(Double r4) { this.r4 = r4; }
    
    public Double getR5() { return r5; }
    public void setR5(Double r5) { this.r5 = r5; }
    
    public Double getR6() { return r6; }
    public void setR6(Double r6) { this.r6 = r6; }
}
