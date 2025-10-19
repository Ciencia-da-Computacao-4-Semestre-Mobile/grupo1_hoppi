package com.grupo1.hoppi.ui.screens.Login

import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.components.DateTextField
import com.grupo1.hoppi.ui.components.LoginTextField
import com.grupo1.hoppi.ui.components.PasswordTextField
import java.util.Calendar
import java.util.Date

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visiblePassword by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val initialYear = calendar.get(Calendar.YEAR)
    val initialMonth = calendar.get(Calendar.MONTH)
    val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                birthDate = "$dayOfMonth/${month + 1}/$year"
            }, initialYear, initialMonth, initialDay
        ).apply {
            datePicker.maxDate = Date().time
        }
    }

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
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
            )

            Text(
                text = "Nome Completo *",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            LoginTextField(
                label = "Digite seu nome completo",
                placeholder = "Digite seu nome completo",
                value = name,
                onValueChange = { name = it },
                leadingIcon = Icons.Filled.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Data de Nascimento *",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            DateTextField(
                label = "Selecione sua data de nascimento",
                placeholder = "Selecione sua data de nascimento",
                value = birthDate,
                leadingIcon = Icons.Filled.CalendarMonth,
                onShowDatePicker = {
                    datePickerDialog.show()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Instituição de Ensino *",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            LoginTextField(
                label = "Digite sua instituição de ensino",
                placeholder = "Digite sua instituição de ensino",
                value = institution,
                onValueChange = { institution = it },
                leadingIcon = Icons.Filled.School,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "E-mail *",
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
                text = "Senha *",
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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Sua senha deve ter, no mínimo",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "8 caracteres",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "1 letra maiúscula",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "1 número",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp
                ),
                modifier = Modifier.align(Alignment.Start)
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
                    "Cadastrar",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 3.dp))
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Já tem cadastro?",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    onLoginClick()
                }
            )

            Spacer(modifier = Modifier.height(85.dp))
        }
    }
}