package com.koolbots.straightdrive.database.quickmatch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koolbots.straightdrive.models.Match

@Database(entities = [Match::class], version = 10,exportSchema = false)
abstract class GlobalDatabase : RoomDatabase() {
    abstract fun matchAccessDAO(): MatchAccessDAO
    companion object {
        private var instance: GlobalDatabase? = null
        @Synchronized
        fun getInstance(ctx: Context): GlobalDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, GlobalDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }}
}