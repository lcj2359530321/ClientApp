package com.quick.app.feature.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quick.app.R
import com.quick.app.core.config.Config
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.theme.SpaceExtraSmall2
import com.quick.app.core.design.theme.SpaceLarge
import kotlinx.coroutines.delay

@Composable
fun LoginRoute(
    finishPage: () -> Unit,
    toRegister: () -> Unit,
    toSetPassword: () -> Unit,
    finishAllLoginPages: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(
        uiState = uiState,
        finishPage = finishPage,
        toRegister = toRegister,
        toSetPassword = toSetPassword,
        finishAllLoginPages = finishAllLoginPages,
        loginClick = viewModel::onLoginClick,
        resetUiState = viewModel::resetUiState,
    )


    LaunchedEffect(key1 = uiState) {
        if (uiState is LoginUiState.Success) {
            finishAllLoginPages()
            viewModel.resetUiState()
        }
    }

//    LaunchedEffect(key1 = uiState) {
//        when(val uiState = uiState){
//            is LoginUiState.ErrorRes -> {
//                Toast.makeText(context, uiState.data, Toast.LENGTH_LONG).show()
//            }
//            is LoginUiState.Error -> {
//                Toast.makeText(context, uiState.exception.tipString, Toast.LENGTH_LONG).show()
//            }
//            is LoginUiState.Success -> {
//               finishAllLoginPages()
//            }
//            else -> {
//
//            }
//        }
//        viewModel.resetUiState()
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState = LoginUiState.None,
    finishPage: () -> Unit = {},
    toRegister: () -> Unit = {},
    toSetPassword: () -> Unit = {},
    finishAllLoginPages: () -> Unit = {},
    loginClick: (String, String) -> Unit = { _, _ -> },
    resetUiState: () -> Unit = {},
) {

    var username by rememberSaveable {
        mutableStateOf(
            if (Config.DEBUG) "13141111222" else ""
        )
    }
    var password by rememberSaveable {
        mutableStateOf(
            if (Config.DEBUG) "ixueaedu" else ""
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.login),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(SpaceLarge),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .padding(start = SpaceLarge, end = SpaceLarge, top = SpaceLarge)
        ) {
            UserTextField(
                value = username,
                onValueChanged = { username = it },
            )

            PasswordTextField(
                value = password,
                onValueChanged = { password = it },
                loginClick = {
                    keyboardController?.hide()
                    loginClick(username, password)
                },
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    loginClick(username, password)
                },
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.login))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                TextButton(
                    onClick = toRegister,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.register),
                    )
                }

                TextButton(
                    onClick = toSetPassword,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.forgot_password),
                    )
                }
            }
        }
        when (val uiState = uiState) {
            is LoginUiState.ErrorRes -> {
                MySweetError(message = stringResource(id = uiState.data))
                LaunchedEffect(uiState) {
                    delay(2000) // 停留 2 秒钟后自动消失
                    resetUiState()
                }
            }
            is LoginUiState.Error -> {
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
//        when(uiState){
//            is LoginUiState.ErrorRes -> {
//                MySweetError(
//                    message = stringResource(id = uiState.data),
//                )
//            }
//            is LoginUiState.Error -> {
//                MySweetError(
//                    message = uiState.exception.tipString!!,
//                )
//            }
//
//            else -> {
//
//            }
//        }
//
//        if (uiState != LoginUiState.None) {
//            resetUiState()
//        }
    }
}

@Composable
fun PasswordTextField(
    value: kotlin.String,
    onValueChanged: (kotlin.String) -> kotlin.Unit,
    loginClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.input_password),
                contentDescription = null
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(SpaceExtraSmall2)
                    )
                }
            }
        },
        value = value,
        onValueChange = onValueChanged,
        label = { Text(stringResource(id = R.string.password)) },
        placeholder = {
            Text(text = stringResource(id = R.string.enter_password))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                loginClick()
            }
        ),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun UserTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var focused by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.input_username),
                contentDescription = null
            )
        },
        trailingIcon = {
            if (focused && value.isNotEmpty()) {
                IconButton(onClick = {
                    onValueChanged("")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(SpaceExtraSmall2)
                    )
                }
            }
        },
        value = value,
        onValueChange = onValueChanged,
        label = { Text(stringResource(id = R.string.phone_or_email)) },
        placeholder = {
            Text(text = stringResource(id = R.string.enter_phone_or_email))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                focused = it.isFocused
            }
    )
}