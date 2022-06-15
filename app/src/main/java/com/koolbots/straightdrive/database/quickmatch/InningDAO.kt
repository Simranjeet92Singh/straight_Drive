package com.koolbots.straightdrive.database.quickmatch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.koolbots.straightdrive.models.Inning

@Dao
interface InningDAO {
    @Insert
    fun addInning(inning: Inning)

    @Query("select * from Inning where inning=:id")
    fun getInningg(id:Int):Inning

    @Query("delete from Inning where inning between :id1 and :id2")
    fun deleteRange(id1:Int,id2:Int)

}