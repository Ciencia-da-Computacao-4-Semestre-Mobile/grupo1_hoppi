package com.grupo1.hoppi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R

// Set of Material typography styles to start with
val HoppiTitleFont = FontFamily(
    Font(R.font.lexend_semibold, FontWeight.SemiBold),
    Font(R.font.lexend_regular, FontWeight.Normal)
)

val HoppiBodyFont = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal)
)
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = HoppiTitleFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = HoppiTitleFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = HoppiBodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)