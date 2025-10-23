package com.grupo1.hoppi.ui.screens.login.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.components.login.PawPrintsDecorationLocal
import com.grupo1.hoppi.ui.components.login.RecoveryOptionCard

@Composable
fun RecoveryStep1Screen(
    onCodeSent: () -> Unit,
    onBack: () -> Unit
) {
    val gradientColors = listOf(
        Color(0xFFEC8445),
        Color(0xFFD1621F),
        Color(0xFFC84B00)
    )

    val cardBackgroundColor = Color.White

    val verticalGradientBrush = Brush.verticalGradient(
        colors = gradientColors
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(verticalGradientBrush)
    ) {
        PawPrintsDecorationLocal(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(85.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_em_pe),
                contentDescription = "Logo Hoppi: coelho e texto",
                modifier = Modifier
                    .size(260.dp),
            )

            Text(
                text = "Se tem estudante, tem Hoppi",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = "Como deseja recuperar a senha?",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 40.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                shape = RoundedCornerShape(12.dp),
                color = cardBackgroundColor,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp, horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enviar código para o email",
                        color = Color.Black.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    RecoveryOptionCard(
                        value = "Ful*****@*****.com",
                        backgroundColor = Color.White,
                        onClick = onCodeSent
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Enviar código para o número",
                        color = Color.Black.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    RecoveryOptionCard(
                        value = "(11) 32**-***57",
                        backgroundColor = Color.White,
                        onClick = onCodeSent
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}