package com.koolbots.straightdrive.database.tournament

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel

@Dao
interface TournamentDAO {
    @Insert
    suspend fun addTournament(TournamentModel: TournamentModel?)

    @Query("select * from `TournamentModel`")
    suspend fun getAllTournament():List<TournamentModel>

    @Query("select * from `TournamentModel` where `key`=:key")
    suspend fun getTournament(key:Int): TournamentModel

    @Delete
    suspend fun delete(TournamentModel: TournamentModel?)

    @Query("delete from `TournamentModel`")
    suspend fun deleteAll()
}