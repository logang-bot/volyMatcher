package com.restrusher.volymatcher

import android.app.Application
import com.restrusher.volymatcher.data.di.RepositoryLocator

class VolyMatcherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryLocator.init(this)
    }
}
