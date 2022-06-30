package com.koolbots.straightdrive.database.tournament

import androidx.room.*
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel


@Dao
interface TournamentDAO {
    @Insert
    suspend fun addTournament(tournament: TournamentModel?)

    @Query("select * from `TournamentModel`")
    suspend fun getAllMatch():List<TournamentModel>

    @Query("select * from `TournamentModel` where `key`=:key")
    suspend fun getMatch(key:Int): TournamentModel

    @Delete
    suspend fun delete(tournamentModel: TournamentModel?)

    @Query("delete from `TournamentModel`")
    suspend fun deleteAll()

    @Update
    suspend fun update(tournamentModel:TournamentModel?)
}