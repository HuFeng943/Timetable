package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.AppConfig

@Composable
fun LanguageSelectPager(
    config: AppConfig, onLanguageSelect: (String?) -> Unit
) {
    val context = LocalContext.current

    val languageValues = context.resources.getStringArray(R.array.language_values).toList()
    val languageLabels = context.resources.getStringArray(R.array.language_labels).toList()
    val currentTag = config.languageTag

    val initialIndex = remember(languageValues, currentTag) {
        val index = languageValues.indexOfFirst { raw ->
            val tag = if (raw == "@null") null else raw
            tag == currentTag
        }
        if (index >= 0) index + 1 else 1
    }

    val scrollState = rememberScalingLazyListState(
        initialCenterItemIndex = initialIndex
    )
    ScreenScaffold(
        scrollState = scrollState
    ) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.settings_language))
                }
            }

            items(languageValues.size) { index ->
                val rawTag = languageValues[index]
                val label = languageLabels[index]

                val tag: String? = if (rawTag == "@null") null else rawTag

                // 判断当前选项是否被选中
                val isSelected = (tag == currentTag)

                RadioButton(
                    selected = isSelected, onSelect = {
                    onLanguageSelect(tag)
                    }, label = {
                    Text(text = label)
                }, icon = {
                    Icon(
                        imageVector = Icons.Default.Language, contentDescription = null
                    )
                    }, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
