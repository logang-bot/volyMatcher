package com.restrusher.volymatcher.data.di

import com.restrusher.volymatcher.data.repository.MatchRepositoryImpl
import com.restrusher.volymatcher.data.repository.PlayerRepositoryImpl
import com.restrusher.volymatcher.data.repository.TeamRepositoryImpl
import com.restrusher.volymatcher.domain.repository.MatchRepository
import com.restrusher.volymatcher.domain.repository.PlayerRepository
import com.restrusher.volymatcher.domain.repository.TeamRepository

/**
 * Manual dependency locator — replace with Hilt when DI is introduced.
 * ViewModels reference this object in their companion Factory to resolve dependencies.
 */
object RepositoryLocator {
    val playerRepository: PlayerRepository = PlayerRepositoryImpl()
    val matchRepository: MatchRepository = MatchRepositoryImpl()
    val teamRepository: TeamRepository = TeamRepositoryImpl()
}
