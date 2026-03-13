package com.quick.app.feature.inputidentity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.feature.inputcode.InputCodePageData

@Composable
fun InputIdentityRoute(
    finishPage: () -> Unit,
    toInputCode: (InputCodePageData) -> Unit,
    viewModel: InputIdentityViewModel = hiltViewModel()
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val username by viewModel.username.collectAsStateWithLifecycle()

    InputIdentityScreen(
        finishPage = finishPage,
        title = title,
        username = username,
        onUsernameChange = viewModel::onUsernameChange,
        onPrimaryClick = viewModel::onPrimaryClick,
    )

    LaunchedEffect(viewModel.toInputCode.value) {
        if (viewModel.toInputCode.value.username.isNotBlank()) {
            toInputCode(viewModel.toInputCode.value)
            viewModel.clearToInputCode()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputIdentityScreen(
    finishPage: () -> Unit = {},
    title: Int = R.string.code_login,
    username: String = "",
    onUsernameChange: (String) -> Unit = {},
    onPrimaryClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = title),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .padding(start = SpaceLarge, end = SpaceLarge, top = SpaceLarge)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(SpaceLarge)
        ) {
            TextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text(text = stringResource(id = R.string.enter_phone_or_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()

            )

            Button(
                enabled = username.isNotBlank(),
                onClick = onPrimaryClick,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.send_code))
            }

        }
    }
}
