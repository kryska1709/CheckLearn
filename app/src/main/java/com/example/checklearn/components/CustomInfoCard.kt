package com.example.checklearn.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.ui.theme.ContrastBlu

@Composable
fun CustomInfoCard(
    title: String,
    description: String
) {
    val expended = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxSize()
            .clickable{expended.value = !expended.value}
            .animateContentSize()
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(ContrastBlu),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                IconButton(
                    onClick = {expended.value = !expended.value}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_down),
                        contentDescription = null
                    )
                }
            }
            if(expended.value) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    color = Color.White
                )
            }
        }
    }
}