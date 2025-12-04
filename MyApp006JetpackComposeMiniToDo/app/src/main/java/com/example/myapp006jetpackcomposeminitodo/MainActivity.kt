package com.example.myapp006jetpackcomposeminitodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp006jetpackcomposeminitodo.ui.theme.MyApp006JetpackComposeMiniToDoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp006JetpackComposeMiniToDoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    // ÚKOL – přidaný Top Bar se zarovnání na střed
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text("Moje To-Do")              // Text uprostřed
                            }
                        )
                    }


                ) { innerPadding ->
                    ToDoScreen(
                        modifier = Modifier
                            .padding(innerPadding) // systémový padding (status bar apod.)
                    )
                }
            }
        }
    }
}
