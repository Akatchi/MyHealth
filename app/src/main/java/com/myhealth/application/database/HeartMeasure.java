package com.myhealth.application.database;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Akatchi on 23-9-2014.
 */
public class HeartMeasure
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int heartrate;

    @DatabaseField
    private long measureDate;

    @DatabaseField
    private String username;

    @DatabaseField
    private String klachten;

    public HeartMeasure(){}

    public HeartMeasure(int heartrate, String username, String klachten)
    {
        this.heartrate      = heartrate;
        this.measureDate    = System.currentTimeMillis();
        this.username       = username;
        this.klachten       = klachten;
    }

    public int getId()
    {
        return id;
    }

    public int getHeartrate()
    {
        return heartrate;
    }

    public void setHeartrate(int heartrate)
    {
        this.heartrate = heartrate;
    }

    public long getMeasureDate()
    {
        return measureDate;
    }

    public void setMeasureDate(long date)
    {
        this.measureDate = date;
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
}
