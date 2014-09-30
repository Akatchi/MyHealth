package com.myhealth.application.config;

/**
 * Created by Akatchi on 22-9-2014.
 */
public class HealthObject
{
    private static HealthObject instance = null;
    private int heartRate;
    private int bloodPressureHigh, bloodPressureLow;
    private int[] ECGData;

    private HealthObject(){}

    public static HealthObject getInstance()
    {
        if( instance == null ) { instance = new HealthObject(); }
        return instance;
    }

    public int getHeartRate()
    {
        return heartRate;
    }

    public void setHeartRate(int heartRate)
    {
        this.heartRate = heartRate;
    }

    public int getBloodPressureHigh()
    {
        return bloodPressureHigh;
    }

    public void setBloodPressureHigh(int bloodPressureHigh)
    {
        this.bloodPressureHigh = bloodPressureHigh;
    }

    public int getBloodPressureLow()
    {
        return bloodPressureLow;
    }

    public void setBloodPressureLow(int bloodPressureLow)
    {
        this.bloodPressureLow = bloodPressureLow;
    }

    public int[] getECGData()
    {
        return ECGData;
    }

    public void setECGData(int[] ECGData)
    {
        this.ECGData = ECGData;
    }
}
