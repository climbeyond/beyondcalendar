package app.climbeyond.beyondcalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import app.climbeyond.beyondcalendar.ui.Accent
import app.climbeyond.beyondcalendar.ui.CalendarView
import app.climbeyond.beyondcalendar.ui.Fonts
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until


class BeyondCalendar(private val settings: Settings, val listener: Listener) {

    interface Listener {
        fun logMessage(message: String)
        fun onInitialized(date: LocalDate)
        fun onMonthSelected(date: LocalDate)
        fun onDateSelected(date: LocalDate)
        fun onHeaderTodayClicked()
    }

    internal val dateNow = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    internal val currentWeekStart = mutableStateOf(settings.weekStart)
    internal val currentSelectedDate = mutableStateOf(dateNow)

    internal var currentIsSelectedMonth = mutableStateOf(true)
    internal val currentYear = mutableStateOf(-1)
    internal val currentMonth = mutableStateOf(-1)
    internal var currentFirstDayOfMonth = mutableStateOf(-1)
    internal var currentDaysInMonth = mutableStateOf(-1)

    private val headerText = mutableStateOf("")

    internal val accents: MutableMap<Int, MutableList<Accent>> = mutableStateMapOf()

    init {
        setMonthView(settings.initDate, true)
    }

    fun setMonthView(date: LocalDate, isInit: Boolean = false) {
        LocalDate(date.year, date.monthNumber, 1).also {
            currentFirstDayOfMonth.value = it.dayOfWeek.ordinal + 1
            currentDaysInMonth.value = it.until(it.plus(1, DateTimeUnit.MONTH), DateTimeUnit.DAY)
        }

        currentIsSelectedMonth.value = (date.year == currentSelectedDate.value.year
                && date.monthNumber == currentSelectedDate.value.monthNumber)

        headerText.value = "${date.year}  ${date.month.name}"
        currentYear.value = date.year
        currentMonth.value = date.monthNumber

        if (!isInit) {
            listener.onMonthSelected(date)
        }
    }

    fun clearAccents() {
        accents.clear()
    }

    fun setAccents(addAccents: MutableMap<Int, MutableList<Accent>>) {
        accents.putAll(addAccents)
    }

    @Composable
    fun View() {
        // First time View composed trigger onInitialized with current month info that could
        // have changed if setMonthView has been called before View and not using Settings
        LaunchedEffect(Unit) {
            listener.onInitialized(LocalDate(currentYear.value, currentMonth.value, 1))
        }

        Column(
                Modifier
                    .widthIn(min = 341.dp)
                    .fillMaxWidth()
        ) {
            Text(
                    text = headerText.value,
                    Modifier
                        .height(48.dp)
                        .background(settings.colorBgHeader)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    fontFamily = Fonts.getFontFamily(),
                    fontWeight = FontWeight.Light,
            )

            CalendarView(this@BeyondCalendar).View()
        }
    }
}
