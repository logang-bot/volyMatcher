package com.restrusher.volymatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.restrusher.volymatcher.ui.navigation.VolyNavGraph
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VolyMatcherTheme {
                VolyNavGraph()
            }
        }
    }
}
