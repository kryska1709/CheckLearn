package com.example.checklearn.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.SideBarMenu
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
}