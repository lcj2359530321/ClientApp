package com.quick.app.feature.setpassword

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SetPasswordRoute(
    finishPage: () -> Unit,
    finishAllLoginPages: () -> Unit,
    viewModel: SetPasswordViewModel = hiltViewModel(),
): Unit {
    val password by viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()

    SetPasswordScreen(
        finishPage = finishPage,
        password = password,
        confirmPassword = confirmPassword,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onPrimaryClick = viewModel::onPrimaryClick,

        )

    LaunchedEffect(viewModel.finishAllLoginPages.value) {
        if (viewModel.finishAllLoginPages.value) {
            finishAllLoginPages()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPasswordScreen(
    finishPage: () -> Unit = {},
    password: String = "",
    confirmPassword: String = "",
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onPrimaryClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                titleText = stringResource(id = R.string.set_password)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .padding(SpaceLarge)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(SpaceLarge)
        ) {
            TextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text(text = stringResource(id = R.string.enter_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text(text = stringResource(id = R.string.enter_confirm_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                enabled = password.isNotBlank() && confirmPassword.isNotBlank(),
                onClick = onPrimaryClick,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }

        }
    }

}
