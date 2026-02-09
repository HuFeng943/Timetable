package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.DatePicker
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.screens.edit.common.ColorSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.common.NameEditScreen
import com.hufeng943.timetable.presentation.viewmodel.TableAction
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun EditTimetableScreen(
    timetable: Timetable? = null, onAction: (TableAction) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current

    val internalNavController = rememberSwipeDismissableNavController()
    val toDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val state = remember(timetable) { EditTimetableState(timetable, toDay) }

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {

            ScreenScaffold(scrollState = scrollState, edgeButton = {
                EdgeButton(
                    onClick = {
                        onAction(TableAction.Upsert(state.snapShot()))
                        navController.popBackStack()
                    }) {
                    Icon(
                        Icons.Default.Check, contentDescription = stringResource(R.string.check)
                    )
                }
            }) { contentPadding ->
                ScalingLazyColumn(
                    state = scrollState, contentPadding = contentPadding
                ) {
                    item {
                        ListHeader {
                            if (timetable == null) {
                                Text(stringResource(R.string.edit_timetable_add))
                            } else {
                                Text("编辑课表")
                            }
                        }
                    }

                    item {// 修改名称
                        TitleCard(
                            onClick = { internalNavController.navigate(InternalNavRoutes.SEMESTER_NAME) },
                            title = {
                                Text(
                                    stringResource(R.string.edit_timetable_name),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                        ) {
                            Text(
                                state.semesterName, style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    item {// 开始日期
                        TitleCard(
                            onClick = { internalNavController.navigate(InternalNavRoutes.START_DATE) },
                            onLongClick = { state.updateStartDate() }, // 默认重置为今天
                            subtitle = { if (state.semesterStartDate != toDay) Text("长按设置为当前日期") },
                            title = { Text(stringResource(R.string.edit_timetable_start)) },
                        ) {
                            Text(
                                state.semesterStartDate.toString(),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    item {// 结束日期
                        TitleCard(
                            onClick = { internalNavController.navigate(InternalNavRoutes.END_DATE) },
                            onLongClick = { state.updateEndDate() },
                            subtitle = { if (state.semesterEndDate != null) Text("长按设置为永不结束") },
                            title = { Text(stringResource(R.string.edit_timetable_end)) },
                        ) {
                            Text(
                                state.semesterEndDate?.toString() ?: "永不结束",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    item {// 颜色
                        TitleCard(
                            onClick = { internalNavController.navigate(InternalNavRoutes.COLOR) },
                            title = { Text(stringResource(R.string.edit_timetable_color)) },
                        ) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(// 圆形效果
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(
                                        40.dp
                                    )
                                    .background(
                                        state.semesterColor, MaterialTheme.shapes.medium
                                    )
                            )
                        }
                    }

                    if (timetable != null) { // 只有编辑时才显示
                        item {
                            FilledTonalButton(
                                onClick = {
                                    internalNavController.navigate(InternalNavRoutes.DELETE_CONFIRM)
                                }, modifier = Modifier.fillMaxWidth(), icon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }, colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    iconColor = MaterialTheme.colorScheme.onErrorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text("删除此课表")
                            }
                        }
                    }

                }
            }
        }

        composable(InternalNavRoutes.START_DATE) {
            DatePicker(
                onDatePicked = { newDate ->
                    state.updateStartDate(newDate.toKotlinLocalDate())
                    internalNavController.popBackStack()
                },
                initialDate = state.semesterStartDate.toJavaLocalDate(),
            )
        }

        composable(InternalNavRoutes.END_DATE) {
            DatePicker(
                onDatePicked = { newDate ->
                    state.updateEndDate(newDate.toKotlinLocalDate())
                    internalNavController.popBackStack()
                },
                initialDate = (state.semesterEndDate ?: state.semesterStartDate).toJavaLocalDate()
            )
        }

        composable(InternalNavRoutes.SEMESTER_NAME) {
            NameEditScreen(
                label = "输入课表名称", initialText = state.semesterName
            ) { newValue ->
                state.semesterName = newValue
                internalNavController.popBackStack() // 保存后滑回去
            }
        }

        composable(InternalNavRoutes.COLOR) {
            ColorSelectionScreen { color ->
                state.semesterColor = color
                internalNavController.popBackStack()
            }
        }

        composable(InternalNavRoutes.DELETE_CONFIRM) {
            DeleteConfirmScreen(detail = "课表 “${state.semesterName}”", onConfirm = {
                if (timetable != null) onAction(TableAction.Delete(timetable.timetableId))
                navController.popBackStack()
            }, onCancel = {
                internalNavController.popBackStack() // 只是关掉确认页，回到编辑页
            })
        }
    }
}


object InternalNavRoutes {
    const val MAIN = "main"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val SEMESTER_NAME = "semester_name"
    const val COLOR = "color"
    const val DELETE_CONFIRM = "delete_confirm"
}