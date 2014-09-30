package com.myhealth.application.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.myhealth.application.R;

import java.sql.SQLException;

/**
 * Created by Akatchi on 22-9-2014.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME           = "myhealth.db";
    private static final int    DATABASE_VERSION        = 7;

    private Dao<BloodMeasure, Integer> bloodMeasureDAO  = null;
    private Dao<HeartMeasure, Integer> heartMeasureDAO  = null;
    private Dao<ECGMeasure, Integer> ecgMeasureDAO      = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs)
    {
        try
        {
            TableUtils.createTable(cs, BloodMeasure.class);
            TableUtils.createTable(cs, HeartMeasure.class);
            TableUtils.createTable(cs, ECGMeasure.class);
        }
        catch( Exception e ){ e.printStackTrace(); }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(cs, BloodMeasure.class, true);
            TableUtils.dropTable(cs, HeartMeasure.class, true);
            TableUtils.dropTable(cs, ECGMeasure.class, true);
            onCreate(db, cs);
        }
        catch( Exception e ){ e.printStackTrace(); }
    }

    public Dao<BloodMeasure, Integer> getBloodMeasureDAO() throws SQLException
    {
        if( bloodMeasureDAO == null )
        {
            bloodMeasureDAO = getDao(BloodMeasure.class);
        }

        return bloodMeasureDAO;
    }

    public Dao<HeartMeasure, Integer> getHeartMeasure() throws SQLException
    {
        if( heartMeasureDAO == null )
        {
            heartMeasureDAO = getDao(HeartMeasure.class);
        }

        return heartMeasureDAO;
    }

    public Dao<ECGMeasure, Integer> getECGMeasure() throws SQLException
    {
        if( ecgMeasureDAO == null )
        {
            ecgMeasureDAO = getDao(ECGMeasure.class);
        }

        return ecgMeasureDAO;
    }
}
