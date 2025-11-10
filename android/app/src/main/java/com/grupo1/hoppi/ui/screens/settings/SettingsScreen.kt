package com.grupo1.hoppi.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R

val HoppiOrange = Color(0xFFEC8445)
val OrangeLight = Color(0xFFF7D9C7)
val GrayIcon = Color(0xFF424242)

@Composable
fun SettingsScreen(
    onEditInformationClick: () -> Unit,
    onEditEmailClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SettingsTopBar(onBack = onBack)
        SettingsContent(
            modifier = Modifier.padding(),
            onEditInformationClick = onEditInformationClick,
            onEditEmailClick = onEditEmailClick,
            onChangePasswordClick = onChangePasswordClick,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onAboutUsClick = onAboutUsClick,
            onLogoutClick = onLogoutClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onBack: () -> Unit) {
    TopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        modifier = Modifier
            .fillMaxWidth()
            .background(HoppiOrange),
        title = {
            Text(
                text = "Configurações",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    onEditInformationClick: () -> Unit,
    onEditEmailClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 10.dp)
    ) {
        SettingSectionHeader(title = "Conta")
        SettingItem(
            icon = Icons.Default.Person,
            title = "Nome de usuário",
            onClick = onEditInformationClick
        )
        SettingItem(
            icon = Icons.Default.Email,
            title = "E-mail",
            onClick = onEditEmailClick
        )

        SettingItem(
            icon = Icons.Default.Lock,
            title = "Alterar senha",
            onClick = onChangePasswordClick
        )

        Spacer(Modifier.height(10.dp))

        SettingSectionHeader(title = "Privacidade e segurança")
        SettingItem(
            icon = Icons.Default.Help,
            title = "Política de privacidade",
            onClick = onPrivacyPolicyClick
        )
        SettingItem(
            icon = Icons.Default.Info,
            title = "Sobre nós",
            onClick = onAboutUsClick
        )
        SettingItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Sair",
            onClick = onLogoutClick
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hoppi_watermark),
                contentDescription = "Hoppi Watermark",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
fun SettingSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = HoppiOrange,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrayIcon,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Ir para ${title}",
            tint = Color.LightGray
        )
    }
}