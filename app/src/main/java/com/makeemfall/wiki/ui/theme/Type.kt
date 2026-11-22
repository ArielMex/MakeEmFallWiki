package com.makeemfall.wiki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.makeemfall.wiki.R

/** Tipografía de títulos y botones del juego (igual que en el Figma). */
val Tomorrow = FontFamily(
    Font(R.font.tomorrow_extrabold_italic, FontWeight.ExtraBold, FontStyle.Italic),
)

/** Tipografía de textos del juego. */
val BarlowCondensed = FontFamily(
    Font(R.font.barlow_condensed_medium, FontWeight.Medium),
    Font(R.font.barlow_condensed_semibold, FontWeight.SemiBold),
    Font(R.font.barlow_condensed_bold_italic, FontWeight.Bold, FontStyle.Italic),
)

private fun titulo(size: Int) = TextStyle(
    fontFamily = Tomorrow, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic, fontSize = size.sp,
)

val Typography = Typography(
    displayLarge = titulo(44),
    headlineLarge = titulo(32),
    headlineMedium = titulo(26),
    titleLarge = titulo(22),
    titleMedium = titulo(18),
    bodyLarge = TextStyle(fontFamily = BarlowCondensed, fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = BarlowCondensed, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 21.sp),
    labelLarge = TextStyle(fontFamily = BarlowCondensed, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontSize = 16.sp),
    labelMedium = TextStyle(fontFamily = BarlowCondensed, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
)
