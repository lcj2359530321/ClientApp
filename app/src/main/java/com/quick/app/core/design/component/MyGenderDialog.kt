package com.quick.app.core.design.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.quick.app.R
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.Space3XLargeHeight
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.bodyXLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGenderDialog(
    title: String = stringResource(id = R.string.select_gender),
    value: Int = 0,
    onSelectChange: (Int) -> Unit = {},
    onDismissRequest: () -> Unit = {},
): Unit {
    val genderTitles = stringArrayResource(id = R.array.gender_titles)
    val genderValues = integerArrayResource(id = R.array.gender_values)

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceLarge),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyXLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier,
                )

                Space3XLargeHeight()

                genderValues.forEachIndexed { index, item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (item == value),
                                onClick = { onSelectChange(item) }
                            )
                            .padding(vertical = SpaceExtraMedium)

                    ) {
                        RadioButton(
                            selected = (item == value),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = genderTitles[index],
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = SpaceOuter)
                        )
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun MyGenderDialogPreview() {
    MyAppTheme {
        MyGenderDialog(

        )
    }
}
