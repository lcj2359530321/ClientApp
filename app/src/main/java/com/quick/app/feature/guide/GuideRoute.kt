package com.quick.app.feature.guide

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.quick.app.R
import com.quick.app.core.design.component.HorizontalPagerIndicator
import com.quick.app.core.design.theme.LocalArrowColor
import com.quick.app.core.design.theme.Space3XLarge
import com.quick.app.core.design.theme.Space3XLargeWidth
import com.quick.app.core.design.theme.Space4XLarge

@Composable
fun GuideRoute(
    toMain: () -> Unit,
    toLogin: () -> Unit,
    viewModel: GuideViewModel = hiltViewModel()
) {
    val datum by viewModel.datum.collectAsState()

    GuideScreen(
        datum = datum,
        toMain = toMain,
        toLogin = toLogin,
        setNotShowGuide = viewModel::setNotShowGuide,
    )

    //enabled为true表示拦截
    BackHandler(enabled = true) {
        //如果拦截了，会执行这里
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(
    datum: List<Int> = listOf(),
    toLogin: () -> Unit = {},
    toMain: () -> Unit = {},
    setNotShowGuide: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        SplashBannerView(
            datum = datum,
            modifier = Modifier.weight(1f)
        )

        ControlView(
            toLogin = {
                setNotShowGuide()
                toLogin()
            },
            toMain = {
                setNotShowGuide()
                toMain()
            },
            modifier = Modifier
        )
    }
}

@Composable
fun ControlView(
    toLogin: () -> Unit = {},
    toMain: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Space3XLarge),
    ) {
        Button(
            onClick = toLogin,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = stringResource(id = R.string.login_or_register))
        }

        Space3XLargeWidth()

        OutlinedButton(
            onClick = toMain,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = stringResource(id = R.string.experience_now))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplashBannerView(datum: List<Int>, modifier: Modifier) {
    val pagerState = rememberPagerState { datum.size }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            val item = datum[page]
            Image(
                painter = painterResource(id = item),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = datum.size,
            modifier = Modifier.padding(bottom = Space4XLarge),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = LocalArrowColor.current,
        )
    }
}