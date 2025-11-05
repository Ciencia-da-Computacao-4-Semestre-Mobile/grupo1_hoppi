package com.grupo1.hoppi.ui.screens.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
val DarkText = Color(0xFF424242)

@Composable
fun EditInformationScreen(navController: NavController) {
    Scaffold(
        topBar = { EditInformationTopBar(navController = navController) },
        content = { paddingValues ->
            EditInformationContent(
                modifier = Modifier.padding(paddingValues),
                onSave = {
                    // Lógica de salvar dados
                    println("Dados salvos. Voltando...")
                    navController.popBackStack()
                },
                onCancel = {
                    // Lógica de cancelar e voltar
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
    // Campos de estado editáveis
    var newName by remember { mutableStateOf(TextFieldValue("")) }
    var newUsername by remember { mutableStateOf(TextFieldValue("")) }
    var newBirthdate by remember { mutableStateOf(TextFieldValue("")) }
    var newInstitution by remember { mutableStateOf(TextFieldValue("")) }

    // Valores atuais (Mocks)
    val currentName = "Fulano de Tal"
    val currentUsername = "@fulan.tal"
    val currentBirthdate = "01/01/2000"
    val currentInstitution = "Universidade xxxxx"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Aumenta o espaçamento entre grupos
    ) {
        // Título da Seção
        Text(
            text = "Alterar dados",
            style = MaterialTheme.typography.titleLarge,
            color = DarkText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(4.dp))

        EditFieldGroup(
            label = "Nome *",
            currentValue = currentName,
            textFieldValue = newName,
            onValueChange = { newName = it },
            placeholder = "Nome Completo"
        )

        // --- Campo 1: Nome de usuário ---
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

        Spacer(Modifier.height(32.dp))

        // --- Botão Salvar ---
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Salvar alterações", color = Color.White, fontSize = 18.sp)
        }

        // --- Botão Cancelar ---
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
        // 1. Rótulo principal (Nome do Campo)
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontSize = 18.sp
        )

        // 2. Valor Atual (Mock)
        Text(
            text = currentValue,
            color = DarkText.copy(alpha = 0.6f),
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        // 3. Campo de Input (Para a nova alteração)
        CustomTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            placeholder = placeholder
        )
    }
}


@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String
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
            cursorColor = HoppiOrange
        )
    )
}

// Removidas as funções FormLabel e CustomReadOnlyField, que não são mais usadas