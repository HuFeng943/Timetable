package com.hufeng943.timetable.presentation.ui.components.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.ui.TimetableUi

@Composable
fun EditTimetableCard(
    onTimetableClick: (Long) -> Unit,
    timetable: TimetableUi,
    onTimetableLongClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    TitleCard(// TODO 课表未开始/正进行/已结束状态显示
        onClick = { onTimetableClick(timetable.timetableId) },
        onLongClick = { onTimetableLongClick(timetable.timetableId) },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (timetable.color != Color.Unspecified) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(timetable.color)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    timetable.displayName, maxLines = 1, modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE
                    )
                )
            }
        },
        subtitle = {
            Text(
                text = stringResource(
                    R.string.edit_timetable_number, timetable.allCourses.size
                ), maxLines = 1, modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE
                )
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}
