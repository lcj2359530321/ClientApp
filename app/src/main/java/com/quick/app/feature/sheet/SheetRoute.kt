package com.quick.app.feature.sheet

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
import com.quick.app.core.model.Sheet

@Composable
fun SheetRoute(
    query: String,
    selected: Boolean,
    toSheetDetail: (String) -> Unit,
    viewModel: SheetViewModel = hiltViewModel()
) {
    val datum by viewModel.datum.collectAsState()

    SheetScreen(
        datum = datum,
        query = query,
        selected = selected,
        loadData = {
            viewModel.loadData(query)
        },
        toSheetDetail = toSheetDetail
    )

    LaunchedEffect(selected) {
        if (selected) {
            viewModel.loadData(query)
        }
    }
}

@Composable
fun SheetScreen(
    datum: List<Sheet>,
    query: String,
    selected: Boolean,
    loadData: () -> Unit,
    toSheetDetail: (String) -> Unit
) {
    if (datum.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(datum) {
                ItemSheet(
                    data = it,
                    modifier = Modifier.clickable {
                        toSheetDetail(it.id)
                    })
            }


        }
    } else {
        MyEmptyView(
            onRetryClick = loadData,
        )
    }
}
