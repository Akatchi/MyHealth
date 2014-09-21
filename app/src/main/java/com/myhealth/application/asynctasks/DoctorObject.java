package com.myhealth.application.asynctasks;

/**
 * Created by Vasco on 21-9-2014.
 */
public class DoctorObject
{
    private String email, telnr, naam, kliniek;
    private int    code;

    public DoctorObject(String email, String telnr, String naam, String kliniek, int code)
    {
        this.email      = email;
        this.telnr      = telnr;
        this.naam       = naam;
        this.kliniek    = kliniek;
        this.code       = code;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTelnr()
    {
        return telnr;
    }

    public void setTelnr(String telnr)
    {
        this.telnr = telnr;
    }

    public String getNaam()
    {
        return naam;
    }

    public void setNaam(String naam)
    {
        this.naam = naam;
    }

    public String getKliniek()
    {
        return kliniek;
    }

    public void setKliniek(String kliniek)
    {
        this.kliniek = kliniek;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
}
