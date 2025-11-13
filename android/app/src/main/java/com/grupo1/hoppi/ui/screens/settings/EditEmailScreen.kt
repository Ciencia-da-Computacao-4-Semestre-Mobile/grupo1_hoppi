package com.grupo1.hoppi.ui.screens.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

const val MOCK_CODE = "1234"

@Composable
fun EditEmailScreen(navController: NavController) {
    Scaffold(
        topBar = { EditEmailTopBar(navController = navController) },
        content = { paddingValues ->
            EditEmailContent(
                modifier = Modifier.padding(paddingValues),
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmailTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "E-mail",
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
fun EditEmailContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current // Objeto para controlar o teclado

    var newEmail by remember { mutableStateOf(TextFieldValue("")) }
    var currentEmailState by remember { mutableStateOf("fulanodetal@gmail.com") }
    var showVerificationCode by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val currentEmail = currentEmailState

    val onSendEmailAttempt: () -> Unit = {
        if (newEmail.text.isNotEmpty() && newEmail.text != currentEmail) {
            keyboardController?.hide()
            println("Código enviado para: ${newEmail.text}")
            showVerificationCode = true
        } else {
            println("Aviso: Insira um novo e-mail válido.")
        }
    }

    val onCodeValidation: () -> Unit = {
        if (verificationCode == MOCK_CODE) {
            keyboardController?.hide()
            currentEmailState = newEmail.text
            newEmail = TextFieldValue("")
            verificationCode = ""
            showVerificationCode = false
            showSuccessDialog = true
        } else {
            println("Erro: Código de verificação inválido.")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        EditFieldGroup(
            label = "E-mail *",
            currentValue = currentEmail,
            textFieldValue = newEmail,
            onValueChange = { newEmail = it },
            placeholder = "seuemail@email.com",
        )

        Button(
            onClick = onSendEmailAttempt,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Mudar e-mail",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = DarkText
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text("Cancelar", fontSize = 18.sp, fontWeight = FontWeight.Normal)
        }

        if (showVerificationCode) {
            Spacer(Modifier.height(32.dp))
            VerificationCodeInput(
                code = verificationCode,
                onCodeChange = { verificationCode = it }
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onCodeValidation,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
                shape = RoundedCornerShape(8.dp),
                enabled = verificationCode.length == 4
            ) {
                Text(
                    "Validar",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Sucesso!") },
            text = { Text("Seu e-mail foi alterado para $currentEmail.") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    onSave()
                }) {
                    Text("OK")
                }
            }
        )
    }
}


@Composable
fun VerificationCodeInput(code: String, onCodeChange: (String) -> Unit) {
    val codeLength = 4

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Digite o código",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 24.dp),
            color = Color(0xFF000000)
        )

        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                if (newValue.length <= codeLength) {
                    onCodeChange(newValue.filter { it.isDigit() })
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(codeLength) { index ->
                        CodeBox(
                            digit = code.getOrNull(index)?.toString() ?: "",
                            isFocused = index == code.length
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun CodeBox(digit: String, isFocused: Boolean) {
    val borderColor = if (isFocused) HoppiOrange else GrayTextField

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
    }
}