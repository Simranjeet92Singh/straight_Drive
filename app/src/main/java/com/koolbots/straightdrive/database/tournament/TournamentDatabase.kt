package com.koolbots.straightdrive.database.tournament

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koolbots.straightdrive.models.TournamentModel

@Database(entities = [TournamentModel::class],version=1, exportSchema = false)
abstract  class TournamentDatabase:RoomDatabase() {

    abstract fun tournamentDAO(): TournamentDAO
    companion object {
        private var instance: TournamentDatabase? = null
        @Synchronized
        fun getInstance(ctx: Context): TournamentDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, TournamentDatabase::class.java,
                    "tournament_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }}
}