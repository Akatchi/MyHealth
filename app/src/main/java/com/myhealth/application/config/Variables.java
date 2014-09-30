package com.myhealth.application.config;

/**
 * Variables stores the various variables for the application.
 * Created by Akatchi on 12-9-2014.
 */
public class Variables
{
    /**
     * Login URL for the application to log the user in.
     */
    public static final String LOGINURL             = "http://myhealth.santjuh.nl/applogin";
    public static final String GETDOCTORURL         = "http://myhealth.santjuh.nl/docter/";
    public static final String URINEPHOTOUPLOADURL  = "http://myhealth.santjuh.nl/urinetest/upload";
    public static final String LOGOUTURL            = "http://myhealth.santjuh.nl/applogout";
    public static final String SENDBLOODTEST        = "http://myhealth.santjuh.nl/app/bloodpressure/save";
    public static final String SENDHEARTRATE        = "http://myhealth.santjuh.nl/app/heartrate/save";
    public static final String SENDECGDATA          = "http://myhealth.santjuh.nl/app/ecg/save";
    public static final String GETPROFILEDATA       = "";
    public static final String SAVEPROFILEDATA      = "";

    /**
     * various codes for the application.
     */
    public static final int     CODE_SUCCESS = 200;
    public static final int     CODE_TOKEN_EXPIRED = 210;
    public static final int     CODE_VALIDATION_ERROR = 400;
    public static final int     CODE_INTERNAL_SERVER_ERROR = 500;
    public static final String  UUID = "ac43a820-3cc0-11e4-916c-0800200c9a66";
}
