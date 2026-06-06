import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.time.format.TextStyle
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun HorizontalDatePicker(
    selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit, modifier: Modifier = Modifier
) {
    val totalDaysCount = Int.MAX_VALUE
    val baseIndex = totalDaysCount / 2
    val todayEpochDays =
        remember { Clock.System.todayIn(TimeZone.currentSystemDefault()).toEpochDays() }

    // 计算索引
    val selectedIndex = remember(selectedDate, todayEpochDays) {
        ((baseIndex) + (selectedDate.toEpochDays() - todayEpochDays)).toInt()
    }

    val listState = rememberLazyListState()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val itemWidth = 46.dp // 单个日期组件的宽度

        val horizontalPadding = (maxWidth - itemWidth * 3) / 2
        val centerOffsetPx = with(density) { horizontalPadding.toPx() }

        LaunchedEffect(selectedIndex, centerOffsetPx) {
            if (centerOffsetPx > 0f) {
                // 传入负的 centerOffsetPx 把条目向右反向推到正中心
                listState.scrollToItem(selectedIndex, -centerOffsetPx.roundToInt())
            }
        }

        LazyRow(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(count = totalDaysCount) { index ->
                val date = remember(index, todayEpochDays) {
                    LocalDate.fromEpochDays(todayEpochDays + (index - baseIndex))
                }

                val isSelected = date == selectedDate
                val backgroundColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceContainer
                }
                val contentColor = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }

                Box(
                    modifier = Modifier
                        .size(width = itemWidth, height = 56.dp) // 保持 46.dp 宽度不变
                        .clip(RoundedCornerShape(16.dp))
                        .background(backgroundColor)
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.dayOfWeek.toDisplayString(TextStyle.SHORT),
                            style = MaterialTheme.typography.labelSmall,
                            color = contentColor.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = date.day.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}