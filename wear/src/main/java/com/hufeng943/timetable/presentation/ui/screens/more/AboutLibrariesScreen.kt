package com.hufeng943.timetable.presentation.ui.screens.more

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import kotlinx.coroutines.launch

@Composable
fun AboutLibrariesScreen() {
    val scrollState = rememberScalingLazyListState()
    val context = LocalContext.current
    val libs by produceLibraries(R.raw.aboutlibraries)
    val scope = rememberCoroutineScope()

    ScreenScaffold(
        scrollState = scrollState, edgeButton = {
            EdgeButton(onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.check)
                )
            }
        }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.about_library))
                }
            }

            libs?.let {
                items(it.libraries) { library ->
                    LibraryCard(library = library)
                }
            }
        }
    }
}

@Composable
fun LibraryCard(library: Library, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    TitleCard(title = {
        Text(
            text = library.name,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE
            )
        )
    }, subtitle = {
        Column() {
            if (library.openSource) {
                Text(
                    text = stringResource(R.string.about_library_open_source),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            if (!library.website.isNullOrBlank()) {
                Text(
                    text = library.website.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE
                    )
                )

            }
        }
    }) {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            library.artifactVersion?.let {
                Text(
                    text = stringResource(R.string.about_library_version, it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (library.developers.isNotEmpty()) {
                Text(
                    text = stringResource(
                        R.string.about_library_developers,
                        library.developers.joinToString(stringResource(R.string.list_separator)) { it.name.toString() }),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (library.licenses.isNotEmpty()) {
                Text(
                    text = library.licenses.joinToString(stringResource(R.string.list_separator)) { it.name },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}