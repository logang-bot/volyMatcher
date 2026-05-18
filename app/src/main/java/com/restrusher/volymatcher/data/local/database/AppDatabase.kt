package com.restrusher.volymatcher.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.restrusher.volymatcher.data.local.dao.MatchDao
import com.restrusher.volymatcher.data.local.dao.PlayerDao
import com.restrusher.volymatcher.data.local.dao.TeamDao
import com.restrusher.volymatcher.data.local.entity.MatchEntity
import com.restrusher.volymatcher.data.local.entity.PlayerEntity
import com.restrusher.volymatcher.data.local.entity.TeamEntity

@Database(
    entities = [PlayerEntity::class, MatchEntity::class, TeamEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
    abstract fun teamDao(): TeamDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "voly_matcher.db").build()
    }
}
