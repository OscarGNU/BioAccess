package com.ve.oscargnu.bioaccess

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ve.oscargnu.bioaccess.ui.theme.BioAccessTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioAccessTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background){
                    Auth()
                }
            }
        }
        setupAuth()
    }

    private var canAuthenticate = false
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private fun setupAuth(){

        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS){

            canAuthenticate = true
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authentication using the biometric sensor")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
        }

    }
    private  fun authenticate(auth: (auth: Boolean) -> Unit){
        if (canAuthenticate){
            BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                object: BiometricPrompt.AuthenticationCallback(){

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        auth (true)
                    }

                }).authenticate(promptInfo)

        }else {
            auth (true)
        }

    }
    @Composable
    fun Auth() {
        var auth by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.background(if (auth) Color.Green else Color.Red)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text( if (auth) "You are logged in " else {
                "You need to authenticate yourself"
            }, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (auth) {
                        auth = false
                    } else
                        authenticate {
                            auth = it
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {Text(if (auth) "Close" else "Authenticate") }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        BioAccessTheme {
            Auth()
        }
    }
}