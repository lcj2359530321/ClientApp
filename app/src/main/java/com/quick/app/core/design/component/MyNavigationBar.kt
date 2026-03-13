package com.quick.app.core.design.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDestination
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.design.theme.SpaceSmallHeight
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.feature.main.ToplevelDestination

/**
 * 底部导航
 */
@Composable
fun MyNavigationBar(
    destinations: List<ToplevelDestination>,//存储界面的基本信息
    currentDestination: String, //当前界面
    onNavigateToDestination: (Int) -> Unit,//回调
    modifier: Modifier = Modifier //给默认值，方便外界直接更改
): Unit {
    Row (
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .navigationBarsPadding()
    ){
        destinations.forEachIndexed{ index, destination ->

            val selected = destination.route == currentDestination
            val color =
                if(selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
                    .padding(vertical = SpaceExtraMedium)
                    .clickableNoRipple {
                        onNavigateToDestination(index)
                    }
            ) {
                Image(
                    painter = painterResource(id =
                        if(selected)
                            destination.selectedIcon
                        else
                            destination.unselectedIcon
                    ),
                    contentDescription = stringResource(id = destination.titleTextId),
                    modifier = Modifier.size(25.dp)
                )
                SpaceSmallHeight()
                Text(
                    text = stringResource(id = destination.titleTextId),
                    color = color,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}