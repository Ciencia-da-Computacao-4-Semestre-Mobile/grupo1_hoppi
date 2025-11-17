package com.grupo1.hoppi.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grupo1.hoppi.R

private data class ProfileOption(val backgroundColor: Color, val imageRes: Int)

private val profileOptions = mapOf(
    1 to ProfileOption(Color(0xFFFFE6A8), R.drawable.profile1),
    2 to ProfileOption(Color(0xFFFED8B7), R.drawable.profile2),
    3 to ProfileOption(Color(0xFFFFE6A8), R.drawable.profile3),
    4 to ProfileOption(Color(0xFFAD754D), R.drawable.profile4),
    5 to ProfileOption(Color(0xFFDBD8D6), R.drawable.profile5),
    6 to ProfileOption(Color(0xFFDBD8D6), R.drawable.profile6),
    7 to ProfileOption(Color(0xFFF2B276), R.drawable.profile7),
)

@Composable
fun ProfileImage(
    option: Int,
    profileSize: Dp,
    backgroundSize: Dp? = null,
    modifier: Modifier = Modifier
) {
    val selectedOption = profileOptions[option] ?: profileOptions[5]!!

    Box(
        modifier = Modifier
            .size(backgroundSize ?: profileSize)
            .background(selectedOption.backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = selectedOption.imageRes),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(profileSize)
        )
    }
}