package com.quick.app.feature.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.quick.app.core.design.component.MyEmptyView
import com.quick.app.core.model.User

@Composable
fun UserRoute(
    query: String,
    selected: Boolean,
    toUserDetail: (String) -> Unit,
    viewModel: UserViewModel = hiltViewModel()
): Unit {
    val datum by viewModel.datum.collectAsState()

    UserScreen(
        datum = datum,
        query = query,
        selected = selected,
        loadData = {
            viewModel.loadData(query)
        },
        toUserDetail = toUserDetail
    )

    LaunchedEffect(selected) {
        if (selected) {
            viewModel.loadData(query)
        }
    }
}

@Composable
fun UserScreen(
    datum: List<User>,
    query: String,
    selected: Boolean,
    loadData: () -> Unit,
    toUserDetail: (String) -> Unit
) {
    if (datum.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(datum) {
                ItemUser(
                    data = it,
                    modifier = Modifier.clickable {
                        toUserDetail(it.id!!)
                    })
            }

        }
    } else {
        MyEmptyView(
            onRetryClick = loadData,
        )
    }
}