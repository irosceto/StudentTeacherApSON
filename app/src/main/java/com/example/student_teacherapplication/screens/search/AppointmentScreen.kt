package com.example.student_teacherapplication.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_teacherapplication.R
import com.example.student_teacherapplication.screens.MainViewModel
import java.text.SimpleDateFormat

@Composable
fun AppointmentScreen(viewModel: MainViewModel, navigateToComments: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.getTeachersAppointments()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.resim),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = viewModel.selectedTeacher?.get("name").toString(), color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )


        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (viewModel.showDates) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                ) {
                    itemsIndexed(viewModel.dates) { index, item ->
                        val isReservated =
                            viewModel.appointments?.any { it.dateTime.toLong() == item } ?: false
                        Column(
                            Modifier
                                .padding(4.dp)
                                .clickable(enabled = isReservated.not()) {
                                    viewModel.makeReservation(
                                        dateTime = item
                                    )
                                }
                                .background(if (isReservated) Color.Red else Color.White)
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = SimpleDateFormat.getInstance().format(item))
                        }
                    }
                }
            }
        }

        Button(onClick = {}, Modifier.fillMaxWidth()) {
            Text(text = "Choose appropriate time for the meeting", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = navigateToComments, Modifier.fillMaxWidth()) {
            Text(text = "Comments", fontSize = 16.sp)
        }

    }
}