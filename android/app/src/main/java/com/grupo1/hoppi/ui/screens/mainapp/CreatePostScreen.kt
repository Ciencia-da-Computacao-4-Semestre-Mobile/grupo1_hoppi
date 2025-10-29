package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.grupo1.hoppi.R

val Laranja = Color(0xFFEC8445)
val Rosa = Color(0xFFA4485F)
val CinzaClaro = Color(0xFFA6A6A6)
val Branco = Color.White
val CinzaMuitoClaro = Color(0xFFE0E0E0)
val VerdeEstudo = Color(0xFF85D39F)
val AzulVenda = Color(0xFF9CBDC6)
val LaranjaInfo = Color(0xFFEDC8B1)
val TextColorEstudo = Color(0xFF00822B)
val TextColorVenda = Color(0xFF406B8D)
val TextColorInfo = Color(0xFFEC8445)

data class TagItem(
    val name: String,
    val color: Color,
    val iconRes: Int
)

@Composable
fun CreatePostScreen(navController: NavController) {
    var postText by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf<TagItem?>(null) }
    var showMenuTags by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val availableTags = listOf(
        TagItem("Estudo", VerdeEstudo, R.drawable.estudo_icon),
        TagItem("Venda", AzulVenda, R.drawable.venda_icon),
        TagItem("Info", LaranjaInfo, R.drawable.info_icon)
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = { CreatePostTopBar(onCloseClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Rosa, RoundedCornerShape(25.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        value = postText,
                        onValueChange = { if (it.length <= 1000) postText = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(vertical = 12.dp)
                                ) {
                                    if (postText.isEmpty()) {
                                        Text(
                                            text = "O que você está pensando?",
                                            color = CinzaClaro,
                                            fontSize = 16.sp,
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(250.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        AnimatedVisibility(
                            visible = showMenuTags,
                            modifier = Modifier
                                .offset(x = 5.dp, y = (-10).dp),
                            enter = expandVertically(expandFrom = Alignment.Bottom),
                            exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(IntrinsicSize.Max)
                                    .shadow(8.dp, RoundedCornerShape(10.dp))
                                    .background(Color.White, RoundedCornerShape(10.dp))
                                    .padding(12.dp, 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                availableTags.forEach { tag ->
                                    TagMenuItem(
                                        tag = tag,
                                        onClick = {
                                            selectedTag = it
                                            showMenuTags = false
                                        }
                                    )
                                    if (tag != availableTags.last()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                selectedTag?.let { tag ->
                                    TagDisplay(tag = tag, onDismiss = { selectedTag = null })
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                AdicionarTagButton(
                                    onClick = { showMenuTags = !showMenuTags }
                                )
                            }
                            Text(
                                text = "${postText.length}/1000 caracteres",
                                color = CinzaClaro,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostTopBar(onCloseClick: () -> Unit) {
    TopAppBar(
        title = { Text("", color = Branco, fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Branco)
            }
        },
        actions = {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Branco, contentColor = Laranja),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(5.dp)
            ) { Text("Publicar") }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Laranja)
    )
}

@Composable
fun TagDisplay(tag: TagItem, onDismiss: () -> Unit) {
    val textColor = when (tag.name) {
        "Estudo" -> TextColorEstudo
        "Venda" -> TextColorVenda
        "Info" -> TextColorInfo
        else -> Color.White
    }

    Row(
        modifier = Modifier
            .background(tag.color, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = tag.iconRes), contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(tag.name, color = textColor, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            Icons.Default.Close,
            contentDescription = "Remover Tag",
            tint = textColor,
            modifier = Modifier
                .size(16.dp)
                .clickable(onClick = onDismiss)
        )
    }
}

@Composable
fun TagMenuItem(tag: TagItem, onClick: (TagItem) -> Unit) {
    val textColor = when (tag.name) {
        "Estudo" -> TextColorEstudo
        "Venda" -> TextColorVenda
        "Info" -> TextColorInfo
        else -> Color.White
    }

    Row(
        modifier = Modifier
            .width(95.dp)
            .clickable { onClick(tag) }
            .background(tag.color.copy(alpha = 0.9f), RoundedCornerShape(5.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = tag.iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(tag.name, color = textColor, fontSize = 14.sp)
    }
}

@Composable
fun AdicionarTagButton(onClick: () -> Unit) {
    val buttonBackgroundColor = CinzaMuitoClaro.copy(alpha = 0.6f)

    Row(
        modifier = Modifier
            .background(buttonBackgroundColor, RoundedCornerShape(5.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Add, contentDescription = "Adicionar Tag", tint = CinzaClaro, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Adicionar tag", color = Color.DarkGray, fontSize = 14.sp)
    }
}