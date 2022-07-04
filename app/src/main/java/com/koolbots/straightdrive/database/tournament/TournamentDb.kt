package com.koolbots.straightdrive.database.tournament

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koolbots.straightdrive.database.quickmatch.GlobalDatabase
import com.koolbots.straightdrive.database.quickmatch.MatchAccessDAO
import com.koolbots.straightdrive.models.Match
import com.koolbots.straightdrive.models.TournamentModel


@Database(entities = [TournamentModel::class], version =12,exportSchema = false)
abstract class TournamentDb : RoomDatabase() {
    abstract fun tournamentDAO(): TournamentDAO
    companion object {
        private var instance: TournamentDb? = null
        @Synchronized
        fun getInstance(ctx: Context): TournamentDb {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, TournamentDb::class.java,
                    "TournamentModel")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }}
}