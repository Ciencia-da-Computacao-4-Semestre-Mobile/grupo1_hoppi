package com.grupo1.hoppi.ui.screens.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.ui.screens.settings.HoppiOrange

val HoppiOrange = Color(0xFFEC8445)
val GrayTextField = Color(0xFFE0E0E0)
val DarkText = Color(0xFF000000)

@Composable
fun EditInformationScreen(navController: NavController) {
    Scaffold(
        topBar = { EditInformationTopBar(navController = navController) },
        content = { paddingValues ->
            EditInformationContent(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                onSave = {
                    println("Dados salvos. Voltando...")
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditInformationTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        modifier = Modifier
            .fillMaxWidth()
            .background(HoppiOrange),
        title = {
            Text(
                text = "Suas informações",
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
fun EditInformationContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var newName by remember { mutableStateOf(TextFieldValue("")) }
    var newUsername by remember { mutableStateOf(TextFieldValue("")) }
    var newBirthdate by remember { mutableStateOf(TextFieldValue("")) }
    var newInstitution by remember { mutableStateOf(TextFieldValue("")) }

    val currentName = "Fulano de Tal"
    val currentUsername = "@fulan.tal"
    val currentBirthdate = "01/01/2000"
    val currentInstitution = "Universidade xxxxx"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Alterar dados",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkText,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        // Spacer(Modifier.height(30.dp))

        EditFieldGroup(
            label = "Nome *",
            currentValue = currentName,
            textFieldValue = newName,
            onValueChange = { newName = it },
            placeholder = "Nome Completo"
        )

        EditFieldGroup(
            label = "Nome de usuário *",
            currentValue = currentUsername,
            textFieldValue = newUsername,
            onValueChange = { newUsername = it },
            placeholder = "@novo_nome_de_usuario"
        )

        // --- Campo 2: Data de nascimento ---
        EditFieldGroup(
            label = "Data de nascimento *",
            currentValue = currentBirthdate,
            textFieldValue = newBirthdate,
            onValueChange = { newBirthdate = it },
            placeholder = "dd/mm/aaaa"
        )

        // --- Campo 3: Instituição de ensino ---
        EditFieldGroup(
            label = "Instituição de ensino *",
            currentValue = currentInstitution,
            textFieldValue = newInstitution,
            onValueChange = { newInstitution = it },
            placeholder = "Mudar instituição"
        )

        Spacer(Modifier.height(30.dp))

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

        Spacer(Modifier.height(5.dp))

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

@Composable
fun EditFieldGroup(
    label: String,
    currentValue: String,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontSize = 20.sp
        )

        Text(
            text = currentValue,
            color = DarkText.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        CustomTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            placeholder = placeholder,
        )
    }
}


@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = DarkText.copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = GrayTextField,
            focusedBorderColor = HoppiOrange,
            unfocusedBorderColor = Color(0xFF000000),
            focusedTextColor = Color(0xFF000000),
            unfocusedTextColor = Color(0xFF000000),
            cursorColor = HoppiOrange
        )
    )
}