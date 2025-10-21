package com.grupo1.hoppi.ui.screens.login.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.components.PawPrintsDecorationLocal

@Composable
fun RecoveryStep2Screen(
    onVerificationSuccess: () -> Unit,
    onRetry: () -> Unit
) {
    val codeLength = 4
    val code = remember { mutableStateListOf(*Array(codeLength) { "" }) }
    val focusRequesters = remember { List(codeLength) { FocusRequester() } }

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
            )

            Text(
                text = "Digite o código",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 40.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(codeLength) { index ->
                    CodeInputBox(
                        value = code[index],
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {
                                code[index] = newValue

                                if (newValue.isNotEmpty() && index < codeLength - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                } else if (newValue.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier.focusRequester(focusRequesters[index])
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (code.all { it.isNotEmpty() }) {
                        onVerificationSuccess()
                    }
                },
                modifier = Modifier.width(124.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF4C4B4B)
                )
            ) {
                Text("Validar", color = Color(0xFF4C4B4B))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Escolher outra validação",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { onRetry() }
            )
        }
    }
}

@Composable
fun CodeInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .width(50.dp)
            .height(50.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.White, RoundedCornerShape(8.dp)),

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                innerTextField()
            }
        }
    )
}