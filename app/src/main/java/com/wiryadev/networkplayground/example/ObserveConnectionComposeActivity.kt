package com.wiryadev.networkplayground.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wiryadev.networkplayground.connection.Connection
import com.wiryadev.networkplayground.connection.asString
import com.wiryadev.networkplayground.connection.rememberFlowConnectionObserver
import com.wiryadev.networkplayground.connection.rememberLiveDataConnectionObserver
import com.wiryadev.networkplayground.connection.rememberRxConnectionObserver
import com.wiryadev.networkplayground.example.ui.theme.NetworkPlaygroundTheme

class ObserveConnectionComposeActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val flowConnectionObserver = rememberFlowConnectionObserver()
            val livedataConnectionObserver = rememberLiveDataConnectionObserver()
            val rxConnectionObserver = rememberRxConnectionObserver()

            NetworkPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                val connectionFlow by flowConnectionObserver.connection.collectAsState()
                val connectionLiveData by livedataConnectionObserver.connection.observeAsState(Connection.Lost)
                val connectionRx by rxConnectionObserver.connection.subscribeAsState(Connection.Lost)

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Compose")
                            }
                        )
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                    ) {
                        ConnectionCard(
                            label = "LiveData",
                            value = connectionLiveData.asString(),
                            color = Color(0xFF32DE84)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ConnectionCard(
                            label = "Flow",
                            value = connectionFlow.asString(),
                            color = Color(0xFF7F52FF)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ConnectionCard(
                            label = "ReactiveX",
                            value = connectionRx.asString(),
                            color = Color(0xFFB7178B)
                        )
                    }

                }
            }
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, ObserveConnectionComposeActivity::class.java))
        }
    }
}

@Composable
fun ConnectionCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text(text = label, style = MaterialTheme.typography.titleLarge)
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}