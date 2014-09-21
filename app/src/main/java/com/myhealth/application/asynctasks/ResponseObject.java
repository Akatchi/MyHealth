package com.myhealth.application.asynctasks;

/**
 * Created by Akatchi on 13-9-2014.
 */
public class ResponseObject
{
    private String  message;
    private String  token;
    private int     code;

    public ResponseObject()
    {
        super();
    }

    public ResponseObject(String message, String token, int code)
    {
        this.message = message;
        this.token   = token;
        this.code    = code;
    }

    public int getCode()                    { return code; }
    public String getMessage()              { return message; }
    public String getToken()                { return token; }

    public void setCode(int code)           { this.code = code; }
    public void setMessage(String message)  { this.message = message; }
    public void setToken(String token)      { this.token = token; }
}
