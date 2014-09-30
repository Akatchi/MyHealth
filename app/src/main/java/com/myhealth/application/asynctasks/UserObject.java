package com.myhealth.application.asynctasks;

/**
 * Created by Akatchi on 30-9-2014.
 */
public class UserObject
{
    private String firstname, lastname;
    private int    code;

    public UserObject(){}

    public UserObject(String firstname, String lastname, int code)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.code = code;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
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
