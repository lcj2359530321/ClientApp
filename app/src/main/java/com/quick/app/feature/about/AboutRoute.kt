package com.quick.app.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MyLoading
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.Space3XLargeHeight
import com.quick.app.core.design.theme.SpaceExtraMediumHeight
import com.quick.app.core.model.AboutModel
import com.quick.app.feature.profile.MySettingValue

@Composable
fun AboutRoute(
    finishPage: () -> Unit,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AboutScreen(
        finishPage = finishPage,
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    finishPage: () -> Unit = {},
    uiState: AboutUiState = AboutUiState.Loading,
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.about),
            )
        }
    ) { paddingValues ->
        when (uiState) {
            AboutUiState.Loading -> {
                MyLoading(
                )
            }

            is AboutUiState.Success -> {
                ContentView(
                    data = uiState.data,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }

    }
}

@Composable
fun ContentView(data: AboutModel, modifier: Modifier = Modifier) {
    val state = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        Space3XLargeHeight()
        Image(
            painter = painterResource(id = R.drawable.login_logo), contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Space3XLargeHeight()
        MySettingValue(
            title = R.string.current_version,
            value = stringResource(id = R.string.version_value, data.versionName, data.versionCode),
            modifier = Modifier
                .clickable {
                }
        )

        SpaceExtraMediumHeight()

        MySettingValue(
            title = R.string.function_introduction,
            value = "",
            modifier = Modifier
                .clickable {
                }
        )

        MySettingValue(
            title = R.string.about,
            value = "",
            modifier = Modifier
                .clickable {
                }
        )
    }
}



