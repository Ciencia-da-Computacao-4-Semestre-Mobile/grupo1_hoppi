package com.grupo1.hoppi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grupo1.hoppi.R

@Composable
fun PawPrintsDecorationLocal(modifier: Modifier = Modifier) { // Note o 'Local'
    Image(
        painter = painterResource(id = R.drawable.patinhas),
        contentDescription = "Patas decorativas",
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.FillWidth
    )
}