package dev.linmg.sudokufreecodecamp.ui.components



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.linmg.sudokufreecodecamp.R
import dev.linmg.sudokufreecodecamp.ui.mainTitle

@Composable
fun LoadingScreen() {
    Surface(
        color=MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxHeight(.8f)
            .fillMaxWidth()
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter= painterResource(id = R.drawable.ic_launcher_foreground),
                modifier= Modifier.size(128.dp),
                contentDescription = stringResource(id = R.string.logo_description)
            )
            LinearProgressIndicator(
                color= Color.LightGray,
                modifier = Modifier
                    .width(128.dp)
                    .padding(16.dp)
            )
            Text(
                text = stringResource(id = R.string.loading),
                style = mainTitle.copy(color=MaterialTheme.colors.secondary),
                modiifier=Modifier.wrapContentSize(),
            )
        }
    }
}