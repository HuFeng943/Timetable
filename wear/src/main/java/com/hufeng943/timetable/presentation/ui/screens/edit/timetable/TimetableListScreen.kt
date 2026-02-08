package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.presentation.ui.NavRoutes.editTimetable
import com.hufeng943.timetable.shared.model.Timetable

@Composable
fun TimetableListScreen(
    timetables: List<Timetable>
) {
    val navController = LocalNavController.current
    val scrollState = rememberScalingLazyListState()
    AppScaffold {
        ScreenScaffold(scrollState = scrollState, edgeButton = {
            EdgeButton(
                onClick = { navController.navigate(NavRoutes.EDIT_TIMETABLE) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.edit_timetable_add)
                )
            }
        }) {
            ScalingLazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    ListHeader {
                        Text(stringResource(R.string.edit_timetable_title))
                    }
                }
                if (timetables.isEmpty()) {
                    item {
                        Text(stringResource(R.string.edit_timetable_empty))
                    }
                } else {
                    items(timetables, key = { it.timetableId }) { timetable ->
                        TitleCard(// TODO 课表未开始/正进行/已结束状态显示
                            onClick = { navController.navigate(editTimetable(timetable.timetableId)) },
                            title = { Text(timetable.semesterName) },
                            subtitle = {
                                Text(
                                    text = stringResource(
                                        R.string.edit_timetable_number, timetable.allCourses.size
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}