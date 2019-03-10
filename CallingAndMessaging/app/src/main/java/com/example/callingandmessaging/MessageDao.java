package com.example.callingandmessaging;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    public void addTimer(MessageTimeTable messageTimeTable);

    @Query("select * from MessageTimeTable")
    public List<MessageTimeTable> getAllTimers();

    @Delete
    public void deleteTimer(MessageTimeTable messageTimeTable);

    @Query("select * from MessageTimeTable where id LIKE :nid")
    public MessageTimeTable getMessageTimerById(int nid);
}
