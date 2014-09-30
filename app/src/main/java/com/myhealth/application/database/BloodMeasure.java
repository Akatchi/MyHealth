package com.myhealth.application.database;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Akatchi on 22-9-2014.
 */
public class BloodMeasure
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int bloodPressureHigh;

    @DatabaseField
    private int bloodPressureLow;

    @DatabaseField
    private int heartrate;

    @DatabaseField
    private long measureDate;

    @DatabaseField
    private String username;

    @DatabaseField
    private String klachten;

    public BloodMeasure(){}

    public BloodMeasure(int bloodPressureHigh, int bloodPressureLow, int heartrate, String klachten, String username)
    {
        this.bloodPressureHigh  = bloodPressureHigh;
        this.bloodPressureLow   = bloodPressureLow;
        this.heartrate          = heartrate;
        this.measureDate        = System.currentTimeMillis();
        this.klachten           = klachten;
        this.username           = username;
    }

    public int getId()
    {
        return id;
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

    public long getMeasureDate()
    {
        return measureDate;
    }

    public void setMeasureDate(long measureDate)
    {
        this.measureDate = measureDate;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getHeartrate()
    {
        return heartrate;
    }

    public void setHeartrate(int heartrate)
    {
        this.heartrate = heartrate;
    }

    public String getKlachten()
    {
        return klachten;
    }

    public void setKlachten(String klachten)
    {
        this.klachten = klachten;
    }

    @Override
    public String toString()
    {
        return "BloodMeasure{" +
                "id=" + id +
                ", bloodPressureHigh=" + bloodPressureHigh +
                ", bloodPressureLow=" + bloodPressureLow +
                ", heartrate=" + heartrate +
                ", measureDate=" + measureDate +
                ", username='" + username + '\'' +
                ", klachten='" + klachten + '\'' +
                '}';
    }
}
