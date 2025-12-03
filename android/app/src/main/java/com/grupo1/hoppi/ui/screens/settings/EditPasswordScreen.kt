package com.grupo1.hoppi.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.ui.screens.settings.account.DarkText
import com.grupo1.hoppi.ui.screens.settings.account.EditFieldGroup

@Composable
fun EditPasswordScreen(
    navController: NavController,
    onForgottenPasswordClick: () -> Unit
) {
    Scaffold(
        topBar = { EditPasswordTopBar(navController = navController) },
        content = { paddingValues ->
            EditPasswordContent(
                modifier = Modifier.padding(paddingValues),
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
                onForgottenPasswordClick = onForgottenPasswordClick
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordTopBar(navController: NavController) {
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
fun EditPasswordContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onForgottenPasswordClick: () -> Unit
) {

    var oldPassword by remember { mutableStateOf(TextFieldValue("")) }
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }

    val colorError = Color(0xFFD9534F)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Alterar Senha",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkText,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(24.dp))

        EditFieldGroup(
            label = "Sua senha atual *",
            textFieldValue = oldPassword,
            onValueChange = { oldPassword = it },
            placeholder = "xxxxxxxxx"
        )

        Spacer(Modifier.height(18.dp))

        Text(
            text = "Esqueceu a senha?",
            color = colorError,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp)
                .clickable() { onForgottenPasswordClick() }
        )

        Spacer(Modifier.height(30.dp))

        EditFieldGroup(
            label = "Sua senha nova *",
            textFieldValue = newPassword,
            onValueChange = { newPassword = it },
            placeholder = "xxxxxxxxx"
        )

        Spacer(Modifier.height(14.dp))

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = "Sua senha deve ter",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = DarkText,
                fontSize = 16.sp
            )
            Text(
                text = "No mínimo 8 caracteres",
                style = MaterialTheme.typography.bodySmall,
                color = DarkText.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Text(
                text = "1 letra maiúscula",
                style = MaterialTheme.typography.bodySmall,
                color = DarkText.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Text(
                text = "No mínimo 1 número",
                style = MaterialTheme.typography.bodySmall,
                color = DarkText.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(Modifier.height(40.dp))

        EditFieldGroup(
            label = "Confirme a senha *",
            textFieldValue = newPassword,
            onValueChange = { newPassword = it },
            placeholder = "xxxxxxxxx"
        )

        Spacer(Modifier.height(70.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(210.dp, 40.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Salvar alterações", color = Color.White, style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp)
        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(210.dp, 40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = DarkText
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Cancelar", fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal)
        }
    }
}
