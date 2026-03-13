package com.quick.app.feature.inputcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quick.app.R
import com.quick.app.core.design.component.ComposePinInputStyle
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MyPinInput
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.Space3XLarge
import com.quick.app.core.design.theme.Space4XLarge
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.body6XLargeBold
import com.quick.app.feature.login.LoginUiState
import com.quick.app.feature.login.LoginViewModel
import com.quick.app.feature.setpassword.SetPasswordPageData

@Composable
fun InputCodeRoute(
    finishPage: () -> Unit,
    toSetPassword: (SetPasswordPageData) -> Unit,
    finishAllLoginPages: () -> Unit,
    viewModel: InputCodeViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val sendTargetTitle by viewModel.sendTargetTitle.collectAsStateWithLifecycle()
    val sendTitle by viewModel.sendTitle.collectAsStateWithLifecycle()
    val sendEnable by viewModel.sendEnable.collectAsStateWithLifecycle()
    val code by viewModel.code.collectAsStateWithLifecycle()
    val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    InputCodeScreen(
        finishPage = finishPage,
        sendTargetTitle = sendTargetTitle,
        code = code,
        sendTitle = sendTitle,
        sendEnable = sendEnable,
        onSendClick = viewModel::onSendClick,
        onCodeChange = viewModel::onCodeChange,
        onCodeInputComplete = viewModel::onCodeInputComplete,
    )

    when (val loginUiState = loginUiState) {
        is LoginUiState.ErrorRes -> {
            MySweetError(
                message = stringResource(id = loginUiState.data),
            )
        }

        is LoginUiState.Error -> {
            MySweetError(
                message = loginUiState.exception.tipString!!,
            )
        }

        else -> {

        }
    }

    if (loginUiState != LoginUiState.None) {
        loginViewModel.resetUiState()
    }

    LaunchedEffect(viewModel.codeLogin.value) {
        if (viewModel.codeLogin.value.code.isNotBlank()) {
            loginViewModel.login(viewModel.codeLogin.value)
        }
    }

    LaunchedEffect(viewModel.toSetPassword.value) {
        if (viewModel.toSetPassword.value.username.isNotBlank()) {
            toSetPassword(viewModel.toSetPassword.value)
            viewModel.clearToSetPassword()
        }
    }

    val isSuccess = loginUiState is LoginUiState.Success
    LaunchedEffect(key1 = isSuccess) {
        if (isSuccess) {
            finishAllLoginPages()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputCodeScreen(
    finishPage: () -> Unit = {},
    sendTargetTitle: String = "",
    code: String = "",
    sendTitle: String = "",
    sendEnable: Boolean = false,
    onSendClick: () -> Unit = {},
    onCodeChange: (String) -> Unit = {},
    onCodeInputComplete: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
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
                .padding(Space3XLarge)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(SpaceLarge)
        ) {
            Text(
                text = stringResource(id = R.string.verification_code),
                style = MaterialTheme.typography.body6XLargeBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = sendTargetTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = SpaceMedium)
            )

            MyPinInput(
                value = code,
                fontColor = MaterialTheme.colorScheme.onSurface,
                cellBorderColor = MaterialTheme.colorScheme.outline,
                focusedCellBorderColor = MaterialTheme.colorScheme.primary,
                onValueChange = onCodeChange,
                onPinEntered = onCodeInputComplete,

                style = ComposePinInputStyle.BOX,
                maxSize = 6,
                cellPadding = SpaceMedium,
                cellSize = Space4XLarge,
                modifier = Modifier.padding(top = Space4XLarge)
            )

            TextButton(
                onClick = onSendClick,
                enabled = sendEnable,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = sendTitle)
            }
        }
    }
}
