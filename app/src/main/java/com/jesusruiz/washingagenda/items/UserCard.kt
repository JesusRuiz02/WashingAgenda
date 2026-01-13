package com.jesusruiz.washingagenda.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesusruiz.washingagenda.R

@Composable
fun UserCard(name: String, department: String, building: String, hours: Int, onClick :  () -> Unit)
{
    Card(modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp,bottomEnd = 10.dp, bottomStart = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = name, color = White, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = department, color = White)
        }
        Row(modifier = Modifier.padding(10.dp)) {
            //
            Text(text = building, color = White)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onClick()}) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "",
                    tint = White)
            }
        }
    }
}