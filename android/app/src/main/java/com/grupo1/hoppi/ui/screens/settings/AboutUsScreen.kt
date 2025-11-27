package com.grupo1.hoppi.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    navController: NavController
) {

    val paragrafo1 = "O Hoppi nasceu com um propósito simples: aproximar estudantes. " +
            "Sabemos como pode ser difícil criar conexões, divulgar projetos, pedir ajuda " +
            "ou até encontrar pessoas com interesses parecidos dentro do ambiente acadêmico. " +
            "Pensando nisso, criamos um espaço acolhedor, seguro e feito especialmente para " +
            "quem vive a rotina estudantil."

    val paragrafo2 = "Aqui, você pode compartilhar experiências, trocar ideias, divulgar seus projetos, " +
            "conhecer novos colegas e construir uma rede que te acompanha dentro e fora da sala " +
            "de aula. Queremos incentivar a colaboração, a criatividade e o crescimento pessoal " +
            "e profissional de cada usuário."

    val paragrafo3 = "O Hoppi é mais do que um aplicativo — é uma comunidade. Um lugar onde estudantes, " +
            "professores e apaixonados por conhecimento podem conversar, aprender juntos e criar " +
            "oportunidades reais."

    val paragrafo4 = "Se tem estudante, tem conexão. E se tem conexão, tem Hoppi."

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets,
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Sobre Nós",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
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
    ) { paddingInterno ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingInterno)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = paragrafo1,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Justify,
                    color = Color(0xFF000000),
                    lineHeight = 20.sp
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = paragrafo2,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Justify,
                    color = Color(0xFF000000),
                    lineHeight = 20.sp
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = paragrafo3,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Justify,
                    color = Color(0xFF000000),
                    lineHeight = 20.sp
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = paragrafo4,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Justify,
                    color = Color(0xFF000000),
                    lineHeight = 20.sp
                )
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item {
                Image(
                    painter = painterResource(id = R.drawable.hoppi_orange_logo),
                    contentDescription = "Logo Hoppi",
                    modifier = Modifier.size(170.dp)
                )
            }
        }
    }
}


