package com.restrusher.volymatcher.data.di

import android.app.Application
import com.restrusher.volymatcher.data.local.database.AppDatabase
import com.restrusher.volymatcher.data.local.mapper.toEntity
import com.restrusher.volymatcher.data.repository.MatchRepositoryImpl
import com.restrusher.volymatcher.data.repository.PlayerRepositoryImpl
import com.restrusher.volymatcher.data.repository.TeamRepositoryImpl
import com.restrusher.volymatcher.domain.repository.MatchRepository
import com.restrusher.volymatcher.domain.repository.PlayerRepository
import com.restrusher.volymatcher.domain.repository.TeamRepository
import com.restrusher.volymatcher.data.source.SampleDataSource

/**
 * Manual dependency locator — replace with Hilt when DI is introduced.
 * Call init(app) from Application.onCreate() before accessing any repository.
 */
object RepositoryLocator {
    private lateinit var db: AppDatabase

    fun init(app: Application) {
        db = AppDatabase.create(app)
    }

    suspend fun seedIfEmpty() {
        if (db.playerDao().getAll().isEmpty()) {
            db.playerDao().upsertAll(SampleDataSource.players.map { it.toEntity() })
            db.matchDao().upsertAll(SampleDataSource.matches.map { it.toEntity() })
            db.teamDao().upsertAll(SampleDataSource.teams().map { it.toEntity() })
        }
    }

    val playerRepository: PlayerRepository by lazy { PlayerRepositoryImpl(db.playerDao()) }
    val matchRepository: MatchRepository by lazy { MatchRepositoryImpl(db.matchDao()) }
    val teamRepository: TeamRepository by lazy { TeamRepositoryImpl(db.teamDao(), db.playerDao()) }
}
