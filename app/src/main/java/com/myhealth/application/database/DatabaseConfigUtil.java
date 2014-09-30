package com.myhealth.application.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;

/**
 * Created by Akatchi on 22-9-2014.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil
{
    private static final Class<?>[] classes = new Class[]{BloodMeasure.class, HeartMeasure.class, ECGMeasure.class};

    public static void main(String[] args) throws Exception
    {
        writeConfigFile(new File("C:/Users/Akatchi/SourceTree/Github/MyHealth/app/src/main/res/raw/ormlite_config.txt"), classes);
    }
}
