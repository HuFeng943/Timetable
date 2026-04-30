package com.hufeng943.timetable.presentation.ui.screens.more

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R

@Composable
fun AboutScreen() {
    val scrollState = rememberScalingLazyListState()
    val context = LocalContext.current
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
                TitleCard(
                    onClick = {//TODO
                    },
                    title = { Text(stringResource(R.string.about_declaration_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    subtitle = {
                        Text(
                            text = stringResource(R.string.about_declaration_subtitle),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }) {
                    Text(
                        text = stringResource(R.string.about_declaration_text).trimMargin(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}