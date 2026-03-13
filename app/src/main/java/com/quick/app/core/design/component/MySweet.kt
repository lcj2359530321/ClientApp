package com.quick.app.core.design.component

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess

@Composable
fun MySweetSuccess(message: String): Unit {
    SweetSuccess(
        message = message,
        duration = Toast.LENGTH_SHORT,
        padding = PaddingValues(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    )
}

@Composable
fun MySweetError(message: String): Unit {
    SweetToastUtil.SweetError(
        message = message,
        duration = Toast.LENGTH_SHORT,
        padding = PaddingValues(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    )
}