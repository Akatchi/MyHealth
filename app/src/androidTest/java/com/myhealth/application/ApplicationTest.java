package com.myhealth.application;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.myhealth.application.activities.HeartActivity;
import com.myhealth.application.asynctasks.LoginTask;
import com.myhealth.application.asynctasks.ResponseObject;
import com.myhealth.application.config.Variables;
import com.myhealth.application.database.BloodMeasure;
import com.myhealth.application.database.DatabaseHelper;
import com.myhealth.application.database.ECGMeasure;
import com.myhealth.application.database.HeartMeasure;



/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

/*    public void testcaseexample() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);
    }*/

    public void testBloodMessureHigh(){
        BloodMeasure blood = new BloodMeasure();
        blood.setBloodPressureHigh(90);
        assertEquals(90,blood.getBloodPressureHigh());
    }


    public void testECGKlacht(){
        ECGMeasure ecg = new ECGMeasure();
        String waarde = "er zijn geen klachten.";
        ecg.setKlachten(waarde);
        assertEquals(waarde,ecg.getKlachten());
    }

    public void testECGtime(){
        ECGMeasure ecg = new ECGMeasure();
        long waarde = System.currentTimeMillis();
        ecg.setMeasureDate(waarde);
        assertEquals(waarde,ecg.getMeasureDate());
    }

    public void testECGMeasureusername(){
        ECGMeasure ecg = new ECGMeasure();
        String waarde = "test@test.nl";
        ecg.setUsername(waarde);
        assertEquals(waarde,ecg.getUsername());
    }

    public void testECGdata(){
        ECGMeasure ecg = new ECGMeasure();
        int[] waarden = new int[]{9,5,7,3,5,1};
        ecg.setECGdata(waarden);
        assertEquals(waarden, ecg.getECGdata());
    }

    public void testECTtostring(){
        ECGMeasure ecg = new ECGMeasure();
        String waarde = "er zijn geen klachten.";
        ecg.setKlachten(waarde);
        long time = System.currentTimeMillis();
        ecg.setMeasureDate(time);
        String username = "test@test.nl";
        ecg.setUsername(username);
        int[] waarden = new int[]{9,5,7,3,5,1};
        ecg.setECGdata(waarden);
        Log.d("app", ecg.toString());
        assertEquals("ECGMeasure{id=0, heartrate=[9, 5, 7, 3, 5, 1], measureDate="+time+", username='test@test.nl', klachten='er zijn geen klachten.'}", ecg.toString());
    }


    public void testBloodMessurelow(){
        BloodMeasure blood = new BloodMeasure();
        blood.setBloodPressureLow(30);
        assertEquals(30,blood.getBloodPressureLow());
    }


    public void testBloodMessureklacht(){
        BloodMeasure blood = new BloodMeasure();
        String waarde = "er zijn geen klachten.";
        blood.setKlachten(waarde);
        assertEquals(waarde,blood.getKlachten());
    }

    public void testBloodMessuretime(){
        BloodMeasure blood = new BloodMeasure();
        long waarde = System.currentTimeMillis();
        blood.setMeasureDate(waarde);
        assertEquals(waarde,blood.getMeasureDate());
    }

    public void testBloodMessureuser(){
        BloodMeasure blood = new BloodMeasure();
        String waarde = "test@test.nl";
        blood.setUsername(waarde);
        assertEquals(waarde,blood.getUsername());
    }

    public void testBloodMessureheartrate(){
        BloodMeasure blood = new BloodMeasure();
        int waarde = 76;
        blood.setHeartrate(waarde);
        assertEquals(waarde,blood.getHeartrate());
    }


    public void testBloodMeasure(){
        BloodMeasure blood = new BloodMeasure();
        blood.setBloodPressureHigh(80);
        blood.setBloodPressureLow(20);
        blood.setHeartrate(50);
        blood.setKlachten("duizelig");
        blood.setUsername("test@test.nl");
        blood.setMeasureDate(20141001);
        blood.toString();
        assertEquals("BloodMeasure{id=0, bloodPressureHigh=80, bloodPressureLow=20, heartrate=50, measureDate=20141001, username='test@test.nl', klachten='duizelig'}", blood.toString());
    }

    public void testSetHeartmessureUsername(){
        HeartMeasure heart = new HeartMeasure();
        String waarde = "test@test.nl";
        heart.setUsername(waarde);
        assertEquals(waarde, heart.getUsername());
    }

    public void testSetHeartmessureKlacht(){
        HeartMeasure heart = new HeartMeasure();
        String waarde = "klacht die ervaren wordt.";
        heart.setKlachten(waarde);
        assertEquals(waarde, heart.getKlachten());
    }

    public void testSetHeartmessureheartrate(){
        HeartMeasure heart = new HeartMeasure();
        int waarde = 87;
        heart.setHeartrate(waarde);
        assertEquals(waarde, heart.getHeartrate());
    }

    public void testSetHeartmessureTime(){
        HeartMeasure heart = new HeartMeasure();
        long waarde = System.currentTimeMillis();
        heart.setMeasureDate(waarde);
        assertEquals(waarde, heart.getMeasureDate());
    }

    public void testheartmessure(){
        HeartMeasure heart = new HeartMeasure();
        long time = System.currentTimeMillis();
        heart.setMeasureDate(time);
        heart.setUsername("test@test.nl");
        heart.setKlachten("geen");
        heart.setHeartrate(76);
        assertEquals("HeartMessure{id=0, heartrate=76, measureDate="+time+", username='test@test.nl', klachten='geen'}",heart.toString());
    }

//    public void tester() throws Exception{
//        SettingsActivity set = new SettingsActivity();
//        assertEquals(true, set.testje());
//    }


    public void logintest() {
        ResponseObject response = null;
        try {
            response = new LoginTask("test@test.nl", "test").execute(new String[]{Variables.LOGINURL}).get();
        } catch (Exception e) {

        }

        assertEquals(Variables.CODE_SUCCESS,response.getCode());
    }

    public void testDB(){
        DatabaseHelper dbHelper = null;
        HeartMeasure measure = new HeartMeasure(1, "test@test.nl", "pijn");
        Dao<HeartMeasure, Integer> heartDao = null;
        boolean exists = false;
        try{
            HeartActivity activ = new HeartActivity();
            dbHelper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
            heartDao = dbHelper.getHeartMeasure();

            heartDao.create(measure);

            exists = heartDao.idExists(measure.getId());

        }
        catch (Exception e){}

        assertEquals(true, exists);

    }

}