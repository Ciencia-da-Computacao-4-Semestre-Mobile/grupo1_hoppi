package com.grupo1.hoppi.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatHour(
    isoString: String,
    forceZoneId: ZoneId? = null
): String {
    return try {
        val odt = OffsetDateTime.parse(isoString)
        val zone = forceZoneId ?: ZoneId.systemDefault()
        val zdt = odt.toInstant().atZone(zone)
        zdt.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        isoString
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(
    isoString: String,
    forceZoneId: ZoneId? = null
): String {
    return try {
        val odt = OffsetDateTime.parse(isoString)
        val zone = forceZoneId ?: ZoneId.systemDefault()
        val zdt = odt.toInstant().atZone(zone)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("pt", "BR"))
        zdt.format(formatter)
    } catch (e: Exception) {
        isoString
    }
}
