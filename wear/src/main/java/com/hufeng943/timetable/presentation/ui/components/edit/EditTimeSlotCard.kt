package com.hufeng943.timetable.presentation.ui.components.edit

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.presentation.ui.components.TimeText
import java.time.format.TextStyle

@Composable
fun EditTimeSlotCard(
    timeSlot: TimeSlotUi, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val locale = LocalLocale.current.platformLocale

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 区域 1 时间
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(top = 3.dp),
                horizontalAlignment = Alignment.End, // 右对齐
            ) {
                TimeText(time = timeSlot.startTime)
                Spacer(modifier = Modifier.height(2.dp))
                TimeText(time = timeSlot.endTime)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Top) {
                val dayText = remember(timeSlot.dayOfWeek, locale) {
                    timeSlot.dayOfWeek?.toDisplayString(
                        TextStyle.SHORT_STANDALONE
                    )
                } ?: stringResource(R.string.unknown)
                val info = listOfNotNull(
                    timeSlot.recurrence.toDisplayString(), dayText
                ).joinToString(stringResource(R.string.info_separator))
                Text(
                    text = info,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE
                    )
                )

                timeSlot.remark?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
