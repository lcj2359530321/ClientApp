package com.quick.app.feature.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MyLoading
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceExtraMediumWidth
import com.quick.app.core.design.theme.SpaceExtraOuter
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.supportsDynamicTheming

@Composable
fun SettingRoute(
    finishPage: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingScreen(
        finishPage = finishPage,
        uiState = uiState,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    finishPage: () -> Unit,
    uiState: SettingUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.setting),
            )
        }
    ) { paddingValues ->
        val state = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(state)
                .padding(SpaceOuter)
        ) {
            when (uiState) {
                SettingUiState.Loading -> {
                    MyLoading()
                }

                is SettingUiState.Success -> {
                    ContentView(
                        setting = uiState.setting,
                        supportDynamicColor = supportDynamicColor,
                        onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                    )
                }
            }
        }
    }
}

@Composable
fun ContentView(
    setting: UserEditableSetting,
    supportDynamicColor: Boolean,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit
) {
    SettingsSectionTitle(text = stringResource(R.string.feature_settings_theme))

    AnimatedVisibility(
        supportDynamicColor
    ) {
        Column {
            SettingsSectionTitle(text = stringResource(R.string.feature_settings_dynamic_color_preference))
            Column(Modifier.selectableGroup()) {
                SettingsThemeChooserRow(
                    text = stringResource(R.string.feature_settings_dynamic_color_yes),
                    selected = setting.useDynamicColor,
                    onClick = { onChangeDynamicColorPreference(true) },
                )
                SettingsThemeChooserRow(
                    text = stringResource(R.string.feature_settings_dynamic_color_no),
                    selected = !setting.useDynamicColor,
                    onClick = { onChangeDynamicColorPreference(false) },
                )
            }
        }
    }
}

@Composable
fun SettingsThemeChooserRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(SpaceExtraOuter),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        SpaceExtraMediumWidth()
        Text(text)
    }
}

@Composable
fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = SpaceOuter, bottom = SpaceExtraMedium),
    )
}


