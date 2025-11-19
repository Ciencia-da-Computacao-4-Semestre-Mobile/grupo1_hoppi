package com.grupo1.hoppi.ui.screens.signup

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.components.login.DateTextField
import com.grupo1.hoppi.ui.components.login.LoginTextField
import com.grupo1.hoppi.ui.components.login.PasswordTextField
import java.util.Calendar
import java.util.Date

@Composable
fun SignUpStep1Screen(
    onContinue: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val initialYear = calendar.get(Calendar.YEAR)
    val initialMonth = calendar.get(Calendar.MONTH)
    val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                viewModel.onBirthDateChange("$dayOfMonth/${month + 1}/$year")
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
                value = state.name,
                onValueChange = viewModel::onNameChange,
                leadingIcon = Icons.Filled.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nome de Usuário *",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            LoginTextField(
                label = "Digite seu nome de usuário",
                placeholder = "Digite seu nome de usuário",
                value = state.username,
                onValueChange = viewModel::onUsernameChange,
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
                value = state.birthDate,
                leadingIcon = Icons.Filled.CalendarMonth,
                onShowDatePicker = {
                    datePickerDialog.show()
                },
                modifier = Modifier.testTag("BirthDateInput")
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
                value = state.institution,
                onValueChange = viewModel::onInstitutionChange,
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
                label = "Digite seu -",
                placeholder = "Digite seu e-mail",
                value = state.email,
                onValueChange = viewModel::onEmailChange,
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
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                leadingIcon = Icons.Filled.Lock,
                visiblePassword = state.isPasswordVisible,
                onToggleVisibility = viewModel::onTogglePasswordVisibility
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

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
                    .clickable { viewModel.onToggleTerms(!state.acceptedTerms) }
            ) {
                Checkbox(
                    checked = state.acceptedTerms,
                    onCheckedChange = viewModel::onToggleTerms,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        checkmarkColor = Color(0xFFC84B00),
                        uncheckedColor = Color.White
                    ),
                    modifier = Modifier.testTag("AcceptTerms")
                )
                Text(
                    text = "Aceito os termos de uso *",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Text(
                text = "Política de Privacidade e Termos de Uso",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline,
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 30.dp)
                    .clickable {  }
            )

            Button(
                onClick = {
                    if (state.isButtonEnabled) {
                        onContinue()
                    }
                },
                Modifier
                    .width(124.dp)
                    .testTag("SignUpButton"),
                enabled = state.isButtonEnabled,
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
                modifier = Modifier
                    .clickable { onLoginClick() }
                    .testTag("AlreadyRegistered")
            )

            Spacer(modifier = Modifier.height(85.dp))
        }
    }
}