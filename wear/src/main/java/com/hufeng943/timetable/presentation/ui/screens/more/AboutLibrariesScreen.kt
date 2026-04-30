package com.hufeng943.timetable.presentation.ui.screens.more

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries

@Composable
fun AboutLibrariesScreen() {
    val scrollState = rememberScalingLazyListState()
    val context = LocalContext.current
    val libs by produceLibraries(R.raw.aboutlibraries)

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState, contentPadding = contentPadding, modifier = Modifier.fillMaxSize()
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

@SuppressLint("LocalContextGetResourceValueCall")
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
        if (library.openSource) {
            Text(
                text = stringResource(R.string.about_library_open_source),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }, onClick = {
        if (!library.website.isNullOrBlank()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(library.website)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("LibraryCard", "打开网址失败：$e")
                Toast.makeText(
                    context, context.getText(R.string.error_open_url_failed), Toast.LENGTH_SHORT
                ).show()
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
                        library.developers.joinToString(", ") { it.name.toString() }),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (library.licenses.isNotEmpty()) {
                Text(
                    text = library.licenses.joinToString(", ") { it.name },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}