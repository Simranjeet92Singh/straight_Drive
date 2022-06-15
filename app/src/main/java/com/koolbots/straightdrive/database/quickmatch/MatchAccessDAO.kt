package com.koolbots.straightdrive.database.quickmatch

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.koolbots.straightdrive.models.Match
import java.util.*
import kotlin.collections.ArrayList

@Dao
interface MatchAccessDAO {
    @Insert
    suspend fun addMatch(match: Match?)

    @Query("select * from `Match`")
    suspend fun getAllMatch():List<Match>

    @Query("select * from `Match` where `key`=:key")
    suspend fun getMatch(key:Int):Match

    @Delete
    suspend fun delete(match: Match?)

    @Query("delete from `match`")
    suspend fun deleteAll()

}