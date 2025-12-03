package com.grupo1.hoppi.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.ui.screens.settings.HoppiOrange
import com.grupo1.hoppi.ui.screens.settings.account.VerificationCodeInput
import com.grupo1.hoppi.ui.screens.settings.account.DarkText
import com.grupo1.hoppi.ui.screens.mainapp.settings.SettingsDestinations

fun validateRecoveryCode(
    inputCode: String,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val MOCK_VALID_CODE = "1111"

    if (inputCode == MOCK_VALID_CODE) {
        onSuccess()
    } else {
        onError()
    }
}
@Composable
fun PasswordRecoveryScreen(
    navController: NavController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { PasswordRecoveryTopBar(navController = navController) },
        content = { paddingValues ->
            PasswordRecoveryContent(
                modifier = Modifier.padding(paddingValues),

                onValidateClick = { code ->
                    validateRecoveryCode(
                        inputCode = code,
                        onSuccess = {
                            navController.navigate(SettingsDestinations.NEW_PASSWORD_ROUTE)
                        },
                        onError = {
                            Toast.makeText(context, "Código incorreto!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },

                onChangeEmailClick = { navController.popBackStack() },
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        modifier = Modifier
            .fillMaxWidth()
            .background(HoppiOrange),
        title = {
            Text(
                text = "Alterar Senha",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HoppiOrange,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun PasswordRecoveryContent(
    modifier: Modifier = Modifier,
    onValidateClick: (String) -> Unit,
    onChangeEmailClick: () -> Unit
) {
    var verificationCode by remember { mutableStateOf("") }
    val colorLink = Color(0xFFD9534F)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        EmailDisplayCard(
            email = "Ful**@**.com",
            onChangeEmailClick = onChangeEmailClick,
            linkTextColor = colorLink
        )

        Spacer(Modifier.height(48.dp))

        VerificationCodeInput(
            code = verificationCode,
            onCodeChange = { verificationCode = it }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onValidateClick(verificationCode) },
            enabled = verificationCode.length == 4,
            modifier = Modifier.size(210.dp, 40.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                "Validar",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun EmailDisplayCard(
    email: String,
    onChangeEmailClick: () -> Unit,
    linkTextColor: Color
) {
    val cardBackgroundColor = Color(0xFFF8F8F8)
    val cardBorderColor = Color.Black.copy(alpha = 0.22f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        border = BorderStroke(1.dp, cardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enviar código para o email",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = email,
                        color = DarkText.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Esse não é o seu e-mail?",
                color = linkTextColor,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onChangeEmailClick() }
            )
        }
    }
}
