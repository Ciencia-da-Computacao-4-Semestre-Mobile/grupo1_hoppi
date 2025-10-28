package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
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
    val menuHeightPx = remember { mutableStateOf(0) }
    val density = LocalDensity.current

    val availableTags = listOf(
        TagItem("Estudo", Color(0xFFB4E4C5), R.drawable.estudo_icon),
        TagItem("Venda", Color(0xFFC8D8F5), R.drawable.venda_icon),
        TagItem("Info", Color(0xFFFFE2CC), R.drawable.info_icon)
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            CreatePostTopBar(onCloseClick = { navController.popBackStack() })
        }
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
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
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
                            .weight(1f)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                if (postText.isEmpty()) {
                                    Text(
                                        text = "O que você está pensando?",
                                        color = CinzaClaro,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Black, thickness = 1.dp)
                Spacer(modifier = Modifier.weight(1f))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedVisibility(
                        visible = showMenuTags,
                        enter = expandVertically(expandFrom = Alignment.Bottom),
                        exit = shrinkVertically(shrinkTowards = Alignment.Bottom),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .offset {
                                val offsetDp = with(density) { (menuHeightPx.value.toDp() + 8.dp).roundToPx() }
                                IntOffset(0, -offsetDp)
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .width(150.dp)
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .shadow(4.dp, RoundedCornerShape(10.dp))
                                .onSizeChanged { size -> menuHeightPx.value = size.height }
                                .padding(vertical = 8.dp)
                        ) {
                            availableTags.forEach { tag ->
                                TagMenuItem(
                                    tag = tag,
                                    onClick = {
                                        selectedTag = it
                                        showMenuTags = false
                                    }
                                )
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
                            AdicionarTagButton(onClick = { showMenuTags = !showMenuTags })
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
    Row(
        modifier = Modifier
            .background(tag.color, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = tag.iconRes), contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(tag.name, color = Color.Black, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            Icons.Default.Close,
            contentDescription = "Remover Tag",
            tint = Color.Black,
            modifier = Modifier
                .size(16.dp)
                .clickable(onClick = onDismiss)
        )
    }
}

@Composable
fun TagMenuItem(tag: TagItem, onClick: (TagItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(tag) }
            .background(tag.color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = tag.iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(tag.name, color = Color.Black, fontSize = 15.sp)
    }
}

@Composable
fun AdicionarTagButton(onClick: () -> Unit) {
    val buttonBackgroundColor = CinzaMuitoClaro.copy(alpha = 0.6f)
    Row(
        modifier = Modifier
            .background(buttonBackgroundColor, RoundedCornerShape(5.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Add, contentDescription = "Adicionar Tag", tint = CinzaClaro, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Adicionar tag", color = Color.DarkGray, fontSize = 14.sp)
    }
}
