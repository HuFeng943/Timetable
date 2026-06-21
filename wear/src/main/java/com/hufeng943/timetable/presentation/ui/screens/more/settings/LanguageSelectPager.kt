package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
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

    val scrollState = rememberTransformingLazyColumnState(
        initialAnchorItemIndex = initialIndex
    )
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(
        scrollState = scrollState
    ) { contentPadding ->
        TransformingLazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ListHeaderDefaults.minimumTopListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
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
                            imageVector = Icons.Rounded.Language, contentDescription = null
                        )
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
        }
    }
}