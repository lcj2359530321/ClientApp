package com.quick.app.feature.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.model.User
import com.quick.app.feature.login.LoginUiState
import kotlinx.coroutines.delay

@Composable
fun RegisterRoute(
    finishPage: () -> Unit,
    finishAllLoginPages: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val data by viewModel.data.collectAsState()

    RegisterScreen(
        uiState = uiState,
        finishPage = finishPage,
        finishAllLoginPages = finishAllLoginPages,
        data = data,
        onValueChange = viewModel::onValueChange,
        onRegisterClick = viewModel::onRegisterClick,
        resetUiState = viewModel::resetUiState,
    )

    LaunchedEffect(key1 = uiState) {
        if (uiState == RegisterUiState.Success) {
            finishAllLoginPages()
            viewModel.resetUiState()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    uiState: RegisterUiState = RegisterUiState.None,
    finishPage: () -> Unit = {},
    toRegister: () -> Unit = {},
    data: User = User(),
    toSetPassword: () -> Unit = {},
    finishAllLoginPages: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    resetUiState: () -> Unit = {},
    onValueChange: (User) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.register),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            verticalArrangement = Arrangement.spacedBy(SpaceLarge),
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickableNoRipple {
                    keyboardController?.hide()
                }
                .padding(paddingValues)
                .padding(start = SpaceLarge, end = SpaceLarge, top = SpaceLarge)
        ) {
            TextField(
                value = data.nickname!!,
                onValueChange = {
                    onValueChange(data.copy(nickname = it))
                },
                label = { Text(text = stringResource(id = R.string.nickname)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_nickname)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            TextField(
                value = data.phone!!,
                onValueChange = {
                    onValueChange(data.copy(phone = it))
                },
                label = { Text(text = stringResource(id = R.string.phone)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_phone)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            TextField(
                value = data.email!!,
                onValueChange = {
                    onValueChange(data.copy(email = it))
                },
                label = { Text(text = stringResource(id = R.string.email)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = data.password!!,
                onValueChange = {
                    onValueChange(data.copy(password = it))
                },
                label = { Text(text = stringResource(id = R.string.password)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
            )

            TextField(
                value = data.confirmPassword!!,
                onValueChange = {
                    onValueChange(data.copy(confirmPassword = it))
                },
                label = { Text(text = stringResource(id = R.string.confirm_password)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_confirm_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.register_and_login))
            }
        }

        when (val uiState = uiState) {
            is RegisterUiState.ErrorRes -> {
                MySweetError(message = stringResource(id = uiState.data))
                LaunchedEffect(uiState) {
                    delay(2000) // 停留 2 秒钟后自动消失
                    resetUiState()
                }
            }
            is RegisterUiState.Error -> {
                MySweetError(message = uiState.exception.tipString ?: "未知错误")
                LaunchedEffect(uiState) {
                    delay(2000)
                    resetUiState()
                }
            }
            else -> {
                // None 或 Success 状态不展示 ErrorUI
            }
        }

    }
}