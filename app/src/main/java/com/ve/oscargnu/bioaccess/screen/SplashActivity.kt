package com.ve.oscargnu.bioaccess.screen.splash


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ve.oscargnu.bioaccess.MainActivity
import com.ve.oscargnu.bioaccess.R
import com.ve.oscargnu.bioaccess.ui.theme.BioAccessTheme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }
        navigateToMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BioAccessTheme {
                SplashScreen {
                    checkAndRequestNotificationPermission()
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                navigateToMain()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}

@Composable
fun SplashScreen(navigateToMain: () -> Unit) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        delay(2000)
        navigateToMain()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_screen),
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.letras_screen),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(900.dp)
                .align(Alignment.Center)
                .alpha(alpha.value),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Developed by @oscargnu",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}


@Composable
fun SplashScreenStatic() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondo_screen),
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Image(
            painter = painterResource(id = R.drawable.letras_screen),
            contentDescription = "Nombre de la aplicación",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
    }
}


@Preview(
    showBackground = true,
    name = "Splash Screen",
    widthDp = 360,
    heightDp = 640
)
@Composable
fun SplashScreenPreview() {
    BioAccessTheme {
        SplashScreenStatic()
    }
}