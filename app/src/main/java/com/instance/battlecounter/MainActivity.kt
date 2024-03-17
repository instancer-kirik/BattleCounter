package com.instance.battlecounter

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.instance.battlecounter.databinding.ActivityMainBinding
import com.instance.battlecounter.ui.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.instance.battlecounter.ui.theme.BattleCounterTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
@AndroidEntryPoint()
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContent {
                BattleCounterTheme(darkTheme = true) {

                Scaffold(

                    //topBar = { Toolbar(this@MainActivity) },
                    content = { padding ->
                        padding
//                        Column(
//                            modifier = Modifier
//                                .padding(padding)
//                        ) {
//
//                                Text(text = "a")


                            //val consentBox: CheckBox =findViewById(R.id.checkBox)
                            //val button: Button =findViewById(R.id.checkBox)
                            DestinationsNavHost(navGraph = NavGraphs.root)

//                            Text("bottom text")
//
//                        }

                        // AppModule_ProvideDbFactory.provideDb(DataBranchApp())


                    }
                )
            }
        }
    }
}

//        val navView: BottomNavigationView = binding.navView

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
