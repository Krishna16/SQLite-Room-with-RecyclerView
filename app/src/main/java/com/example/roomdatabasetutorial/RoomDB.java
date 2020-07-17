package com.example.roomdatabasetutorial;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

//Add database entity
@Database(entities = {MainData.class}, version = 1, exportSchema = false)

public abstract class RoomDB extends RoomDatabase {
    //Create database instance
    private static RoomDB database;

    //Define database name
    private static final String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context){
        //Check condition
        if (database == null){
            //Initialize database
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    public abstract MainDao MainDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
