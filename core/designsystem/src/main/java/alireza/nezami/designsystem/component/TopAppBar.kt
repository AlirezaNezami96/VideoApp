@file:OptIn(ExperimentalMaterial3Api::class)

package alireza.nezami.designsystem.component

import alireza.nezami.designsystem.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
        modifier: Modifier = Modifier,
        @StringRes titleRes: Int,
        navigationIcon: ImageVector,
        navigationIconContentDescription: String? = null,
        @DrawableRes actionIcon: Int,
        actionIconContentDescription: String? = null,
        colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
        onNavigationClick: () -> Unit = {},
        onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = titleRes), color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    painter = painterResource(actionIcon),
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = colors,
        modifier = modifier.heightIn(max = 64.dp),
    )
}

/**
 * Top app bar without any action, display only title
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
        modifier: Modifier = Modifier,
        @StringRes titleRes: Int,
        colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        colors = colors,
        modifier = modifier.testTag("topAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun TopAppBarPreview() {
    TopAppBar(
        titleRes = android.R.string.untitled,
        navigationIcon = Icons.Rounded.ArrowBack,
        navigationIconContentDescription = "Navigation icon",
        actionIcon = R.drawable.ic_bookmark_filled,
        actionIconContentDescription = "Action icon",
    )
}
