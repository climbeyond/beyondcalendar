package app.climbeyond.beyondcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.climbeyond.beyondcalendar.BeyondCalendar
import app.climbeyond.beyondcalendar.helpers.Colors
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus


@OptIn(ExperimentalMaterialApi::class)
class CalendarView(private val calendar: BeyondCalendar) {

    private val grid: SnapshotStateMap<Int, String> = mutableStateMapOf(
            1 to "", 2 to "", 3 to "", 4 to "", 5 to "", 6 to "", 7 to "",
            8 to "", 9 to "", 10 to "", 11 to "", 12 to "", 13 to "", 14 to "",
            15 to "", 16 to "", 17 to "", 18 to "", 19 to "", 20 to "", 21 to "",
            22 to "", 23 to "", 24 to "", 25 to "", 26 to "", 27 to "", 28 to "",
            29 to "", 30 to "", 31 to "", 32 to "", 33 to "", 34 to "", 35 to "",
            36 to "", 37 to "", 38 to "", 39 to "", 40 to "", 41 to "", 42 to "",
    )

    private val weekdays = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    @Composable
    internal fun View() {
        val coroutine = rememberCoroutineScope()

        for (x in 1 .. calendar.currentDaysInMonth.value) {
            grid[x + calendar.currentFirstDayOfMonth.value - 1] = x.toString()
        }

        Row {
            Weekdays()
        }

        var dismissState: DismissState? = null

        dismissState = rememberDismissState(confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    calendar.setMonthView(
                            LocalDate(calendar.currentYear.value, calendar.currentMonth.value, 1)
                                .minus(1, DateTimeUnit.MONTH))
                    coroutine.launch {
                        dismissState?.animateTo(DismissValue.Default)
                    }
                    true
                }
                DismissValue.DismissedToStart -> {
                    calendar.setMonthView(
                            LocalDate(calendar.currentYear.value, calendar.currentMonth.value, 1)
                                .plus(1, DateTimeUnit.MONTH))
                    coroutine.launch {
                        dismissState?.animateTo(DismissValue.Default)
                    }
                    true
                }
                else -> false
            }
        })

        SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 1.dp),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { _ ->
                    FractionalThreshold(0.1f)
                },
                background = {},
                dismissContent = {
                    Column {
                        WeekRow(0)
                        WeekRow(1)
                        WeekRow(2)
                        WeekRow(3)
                        WeekRow(4)
                        WeekRow(5)
                    }
                }
        )
    }

    @Composable
    private fun RowScope.Weekdays() {
        var handleDay = calendar.currentWeekStart.value.ordinal
        var day = 1

        while (day <= 7) {
            Box(
                    Modifier
                        .weight(1f)
                        .height(48.dp),
                    Alignment.Center
            ) {
                Text(
                        weekdays[handleDay],
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Normal,
                        color = Colors.text_white)
            }
            handleDay++
            if (handleDay > 6) {
                handleDay = 0
            }
            day++
        }
    }

    @Composable
    private fun WeekRow(rowNo: Int) {
        val selectedDate = calendar.currentSelectedDate.value.dayOfMonth
        var day: String

        Row {
            // First
            day = grid[(rowNo * 7) + 1].orEmpty()
            DayBox(day, selectedDate)

            // Second
            day = grid[(rowNo * 7) + 2].orEmpty()
            DayBox(day, selectedDate)

            // Third
            day = grid[(rowNo * 7) + 3].orEmpty()
            DayBox(day, selectedDate)

            // Fourth
            day = grid[(rowNo * 7) + 4].orEmpty()
            DayBox(day, selectedDate)

            // Fifth
            day = grid[(rowNo * 7) + 5].orEmpty()
            DayBox(day, selectedDate)

            // Sixth
            day = grid[(rowNo * 7) + 6].orEmpty()
            DayBox(day, selectedDate)

            // Seventh
            day = grid[(rowNo * 7) + 7].orEmpty()
            DayBox(day, selectedDate)
        }
    }

    @Composable
    fun RowScope.DayBox(day: String, selectedDate: Int) {
        if (day.isNotEmpty()) {
            val dayNumber = day.toInt()

            var modifier = Modifier
                .padding(2.dp)
                .clickable {
                    val date = LocalDate(calendar.currentYear.value,
                            calendar.currentMonth.value, day.toInt())

                    calendar.currentSelectedDate.value = date
                    calendar.currentIsSelectedMonth.value = true
                    calendar.listener.onDateSelected(date)
                }
                .weight(1f)
                .height(48.dp)

            if (calendar.currentIsSelectedMonth.value && selectedDate == dayNumber) {
                modifier = modifier.background(Colors.bg_primary_dark,
                        shape = RoundedCornerShape(5.dp))
            }

            if (calendar.dateNow.monthNumber == calendar.currentMonth.value
                    && calendar.dateNow.year == calendar.currentYear.value
                    && calendar.dateNow.dayOfMonth == dayNumber) {
                modifier = modifier.border(1.dp, Colors.white, RoundedCornerShape(5.dp))
            }

            Box(
                    modifier = modifier,
                    Alignment.Center
            ) {
                TextNode(day)

                calendar.accents[dayNumber]?.let {
                    Row(
                            Modifier.padding(top = 30.dp)) {
                        it.forEach {
                            Box(
                                    Modifier
                                        .padding(2.dp)
                                        .background(it.color, RoundedCornerShape(3.dp))
                                        .size(6.dp)
                            )
                        }
                    }
                }
            }

        } else {
            Box(Modifier
                .padding(2.dp)
                .weight(1f)
                .height(48.dp)) {}
        }
    }

    @Composable
    fun TextNode(text: String) {
        Text(
                text,
                fontSize = 14.sp,
                fontStyle = FontStyle.Normal,
                color = Colors.text_white
        )
    }
}