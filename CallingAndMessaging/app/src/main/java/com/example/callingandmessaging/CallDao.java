package com.example.callingandmessaging;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CallDao {

    @Insert
    public void addTimer(CallTimeTable callTimeTable);

    @Query("select * from CallTimeTable")
    public List<CallTimeTable> gerCallTimers();

    @Delete
    public void deleteTimer(CallTimeTable callTimeTable);
}
