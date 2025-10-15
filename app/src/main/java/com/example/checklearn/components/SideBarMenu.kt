package com.example.checklearn.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.checklearn.R
import com.example.checklearn.model.SideBarModel
import com.example.checklearn.navigation.LocalNavigator
import com.example.checklearn.navigation.Routes
import com.example.checklearn.ui.theme.MyGray
import kotlinx.coroutines.launch

@Composable
fun SideBarMenu(
    action: @Composable (DrawerState) -> Unit
) {
    val navigator = LocalNavigator.current
    val scope = rememberCoroutineScope()
    val routes = listOf(
        SideBarModel("Камера", imageId = R.drawable.camera, Routes.CAMERA),
        SideBarModel ("История", imageId = R.drawable.history, Routes.HISTORY)
    )
    val configuration = LocalConfiguration.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val interactionSource = remember { MutableInteractionSource() }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, //для открытия жестом
        drawerContent = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.Transparent)
                    .clickable(interactionSource, indication = null){
                        scope.launch {
                            drawerState.close()
                        }
                    }
            ){
                Column(
                    modifier = Modifier.width((configuration.screenWidthDp * 0.65).dp)
                        .fillMaxHeight()
                        .background(MyGray)
                ) {
                    Image(
                        painter = painterResource(R.drawable.blabla),
                        contentDescription = null
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 20.dp, top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(routes){
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable{navigator.navigate(it.route)}
                            ){
                                Text(text = it.title,
                                    fontSize = 16.sp)
                                Icon(painter = painterResource(it.imageId),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(22.dp))
                            }
                        }
                    }
                }
            }
        }
    ) { action(drawerState) }
}