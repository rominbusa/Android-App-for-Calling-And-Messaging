package com.example.callingandmessaging;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {CallTimeTable.class},version = 1)
public abstract class CallTimerDatabase extends RoomDatabase {
    public abstract CallDao callDao();
}
