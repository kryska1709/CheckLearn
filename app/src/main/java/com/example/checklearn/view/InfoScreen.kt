package com.example.checklearn.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.ui.theme.BlueMainColor
import kotlinx.coroutines.launch

@Composable
fun InfoScreen() {

    val scope = rememberCoroutineScope()

    SideBarMenu { drawerState ->
        CustomScaffold(
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        ) {

        }
    }
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(BlueMainColor.copy(0.8f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

    }
}