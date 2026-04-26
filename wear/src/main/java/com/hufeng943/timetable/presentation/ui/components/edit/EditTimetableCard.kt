package com.hufeng943.timetable.presentation.ui.components.edit

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.displayName
import com.hufeng943.timetable.shared.model.Timetable

@Composable
fun EditTimetableCard(
    onTimetableClick: (Long) -> Unit,
    timetable: Timetable,
    onTimetableLongClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    TitleCard(// TODO 课表未开始/正进行/已结束状态显示
        onClick = { onTimetableClick(timetable.timetableId) },
        onLongClick = { onTimetableLongClick(timetable.timetableId) },
        title = { Text(timetable.displayName) },
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