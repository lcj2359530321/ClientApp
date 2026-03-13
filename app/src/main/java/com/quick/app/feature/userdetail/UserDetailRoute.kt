package com.quick.app.feature.userdetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun UserDetailRoute(
    finishPage: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel(),
): Unit {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrderDetailScreen(
        finishPage = finishPage,
        uiState = uiState,
    )
}

@Composable
fun OrderDetailScreen(
    finishPage: () -> Unit,
    uiState: UserDetailUiState
) {
    when (val uiState = uiState) {
        is UserDetailUiState.Success -> {
            ContentView(
                finishPage = finishPage,
                data = uiState.user,
            )
        }

        else -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContentView(
    finishPage: () -> Unit,
    data: User,
) {

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = ""
            )
        },
    ) { paddingValues ->
        Text(data.nickname ?: data.id!!, modifier = Modifier.padding(paddingValues))
    }
}
