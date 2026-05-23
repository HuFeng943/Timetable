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
import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi


@Composable
fun EditCourseCard(
    course: CourseUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TitleCard(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier.fillMaxWidth(),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (course.color != Color.Unspecified) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(course.color)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    course.displayName, maxLines = 1, modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE
                    )
                )
            }
        },
        subtitle = {
            // 组合地点和教师信息
            val info = listOfNotNull(
                course.location,
                course.teacher,
                stringResource(R.string.edit_course_number, course.timeSlots.size)
            ).joinToString(stringResource(R.string.info_separator))
            Text(
                info, maxLines = 1, modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE
                )
            )
        })
}
