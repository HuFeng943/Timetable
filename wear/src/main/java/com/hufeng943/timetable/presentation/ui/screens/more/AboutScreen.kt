package com.hufeng943.timetable.presentation.ui.screens.more

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.Wearable
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun AboutScreen() {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current
    val context = LocalContext.current
    val remoteActivityHelper = remember { RemoteActivityHelper(context) }
    val scope = rememberCoroutineScope()
    val nodeClient = Wearable.getNodeClient(context)
    val versionName = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            Log.e("AboutScreen", "软件版本号获取失败：$e")
        }
    } ?: stringResource(R.string.unknown)
    val icon = remember {
        val drawable = context.packageManager.getApplicationIcon(context.packageName)
        drawable.toBitmap().asImageBitmap()
    }

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState, contentPadding = contentPadding, modifier = Modifier.fillMaxSize()
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.more_menu_about))
                }
            }

            item {
                TitleCard(title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = BitmapPainter(icon),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                }, modifier = Modifier.fillMaxWidth(), subtitle = {
                    Text(
                        text = stringResource(
                            R.string.about_version_label,
                            stringResource(R.string.info_separator),
                            versionName
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.about_description),
                    )
                }
            }
            item {
                var expanded by remember { mutableStateOf(true) }
                val rotation by animateFloatAsState(
                    targetValue = if (expanded) 180f else 0f,
                    label = "Rotation"
                )
                TitleCard(
                    onClick = { expanded = !expanded },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("更新内容", modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.rotate(rotation) // 箭头随状态旋转
                            )
                        }
                    },
                    subtitle = {
                        Text(if (expanded) "点击收起" else "点击展开查看详情")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 动画
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "TODO",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item {
                TitleCard(
                    title = { Text(stringResource(R.string.about_declaration_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    subtitle = {
                        Text(
                            text = stringResource(R.string.about_declaration_subtitle),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.basicMarquee(
                                iterations = Int.MAX_VALUE
                            )
                        )
                    }) {
                    Text(
                        text = stringResource(R.string.about_declaration_text).trimMargin(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            item {
                TitleCard(
                    onClick = {
                        navController.navigateSingle(NavRoutes.MORE_ABOUT_LIBRARIES)
                    },
                    title = { Text("开源协议") },
                    modifier = Modifier.fillMaxWidth(),
                    subtitle = {
                        Text(
                            text = "单击以查看使用到的第三方库",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }) {
                    Text(
                        text = "本项目采用 MIT License + Commons Clause 双协议\nMIT License 允许自由使用、修改和分发，但须保留版权声明；\nCommons Clause 附加限制：不得将本软件或其功能作为付费产品或服务的核心价值进行售卖\n详细条款请查阅项目中的 LICENSE 文件",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}