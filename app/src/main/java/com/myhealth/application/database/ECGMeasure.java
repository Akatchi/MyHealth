package com.myhealth.application.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.Arrays;

/**
 * Created by Akatchi on 23-9-2014.
 */
public class ECGMeasure
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private long measureDate;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private int[] ECGdata;

    @DatabaseField
    private String username;

    @DatabaseField
    private String klachten;

    public ECGMeasure(){}

    public ECGMeasure(int[] ECGdata, String username, String klachten)
    {
        this.measureDate    = System.currentTimeMillis();
        this.ECGdata        = ECGdata;
        this.username       = username;
        this.klachten       = klachten;
    }

    public int getId()
    {
        return id;
    }

    public long getMeasureDate()
    {
        return measureDate;
    }

    public void setMeasureDate(long date)
    {
        this.measureDate = date;
    }

    public int[] getECGdata()
    {
        return ECGdata;
    }

    public void setECGdata(int[] ECGdata)
    {
        this.ECGdata = ECGdata;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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
        return "ECGMeasure{" +
                "id=" + id +
                ", heartrate=" + Arrays.toString(ECGdata) +
                ", measureDate=" + measureDate +
                ", username='" + username + '\'' +
                ", klachten='" + klachten + '\'' +
                '}';

    }
}
