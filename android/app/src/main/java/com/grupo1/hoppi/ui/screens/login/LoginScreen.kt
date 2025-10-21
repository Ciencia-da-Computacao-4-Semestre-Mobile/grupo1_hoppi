package com.grupo1.hoppi.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.ui.components.LoginTextField
import com.grupo1.hoppi.ui.components.PasswordTextField
import com.grupo1.hoppi.ui.components.PawPrintsDecorationLocal
import com.grupo1.hoppi.R

@Composable
fun LoginScreen(
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }

    val gradientColors = listOf(
        Color(0xFFEC8445),
        Color(0xFFD1621F),
        Color(0xFFC84B00)
    )

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
                    .padding(bottom = 40.dp)
            )

            Text(
                text = "E-mail",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            LoginTextField(
                label = "Digite seu email",
                placeholder = "Digite seu email",
                value = email,
                onValueChange = { email = it },
                leadingIcon = Icons.Filled.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Senha",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            PasswordTextField(
                label = "Digite sua senha",
                placeholder = "Digite sua senha",
                value = password,
                onValueChange = { password = it },
                leadingIcon = Icons.Filled.Lock,
                visiblePassword = visiblePassword,
                onToggleVisibility = { visiblePassword = !visiblePassword }
            )

            Text(
                text = "Esqueceu a senha?",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.End)
                    .clickable {
                        onForgotPasswordClick()
                    }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { /* TODO: Lógica de Login */ },
                Modifier.width(124.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF4C4B4B)
                )
            ) {
                Text(
                    "Entrar",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 3.dp))
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Não tem cadastro?",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    onSignUpClick()
                }
            )


        }
    }
}