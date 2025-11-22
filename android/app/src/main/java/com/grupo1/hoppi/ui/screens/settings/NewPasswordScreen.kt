package com.grupo1.hoppi.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.ui.screens.settings.HoppiOrange
import com.grupo1.hoppi.ui.screens.settings.account.DarkText
import com.grupo1.hoppi.ui.screens.settings.account.EditFieldGroup

@Composable
fun NewPasswordScreen(
    navController: NavController,
    onFinishFlow: () -> Unit
) {
    Scaffold(
        topBar = { NewPasswordTopBar(navController = navController) },
        content = { paddingValues ->
            NewPasswordContent(
                modifier = Modifier.padding(paddingValues),
                onSave = onFinishFlow,
                onCancel = { navController.popBackStack() }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        modifier = Modifier
            .fillMaxWidth()
            .background(HoppiOrange),
        title = {
            Text(
                text = "Alterar senha",
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
fun NewPasswordContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        EditFieldGroup(
            label = "Digite sua nova senha",
            textFieldValue = newPassword,
            onValueChange = { newPassword = it },
            placeholder = "Digite sua senha",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                text = "Sua senha deve ter",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                color = DarkText,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))

            val requirementStyle = MaterialTheme.typography.bodySmall.copy(
                color = DarkText.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 16.sp
            )

            Text(text = "No mínimo 8 caracteres", style = requirementStyle)
            Text(text = "1 letra maiúscula", style = requirementStyle)
            Text(text = "No mínimo 1 número", style = requirementStyle)
        }

        Spacer(Modifier.height(24.dp))

        EditFieldGroup(
            label = "Digite novamente",
            textFieldValue = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Digite sua senha",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .size(210.dp, 40.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Alterar senha", color = Color.White, style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier
                .size(210.dp, 40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEEEEE),
                contentColor = DarkText
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Cancelar", fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}
