package app.climbeyond.beyondcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import beyondcalendar.shared.generated.resources.Res
import beyondcalendar.shared.generated.resources.titillium_web_light
import beyondcalendar.shared.generated.resources.titillium_web_regular
import beyondcalendar.shared.generated.resources.titillium_web_semi_bold
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
                ),
                Font(
                        Res.font.titillium_web_semi_bold,
                        FontWeight.SemiBold,
                        FontStyle.Normal,
                ),
        ))
    }
}
