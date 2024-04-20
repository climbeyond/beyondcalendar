package app.climbeyond.beyondcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import climbeyond.beyondcalendar.generated.resources.Res
import climbeyond.beyondcalendar.generated.resources.titillium_web_light
import climbeyond.beyondcalendar.generated.resources.titillium_web_regular
import org.jetbrains.compose.resources.Font


object Fonts {
    @Composable
    fun getFontFamily(): FontFamily {
        return FontFamily(listOf(
                Font(
                        Res.font.titillium_web_light,
                        FontWeight.Light,
                        FontStyle.Normal,
                ),
                Font(
                        Res.font.titillium_web_regular,
                        FontWeight.Normal,
                        FontStyle.Normal,
                )
        ))
    }
}
