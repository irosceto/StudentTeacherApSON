package com.example.student_teacherapplication.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_teacherapplication.components.CircularProgress
import com.example.student_teacherapplication.components.ErrorComponent
import com.example.student_teacherapplication.data.AppointmentModel
import com.example.student_teacherapplication.screens.HomeScreenState
import com.example.student_teacherapplication.screens.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun HomeScreen(viewModel: MainViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getUsersAppointments()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //viewmodel homescreen state değişimine göre ekran güncellenir
        when (val state = viewModel.homeScreenState) {
            is HomeScreenState.Content -> {
                HomeScreenContent(state.appointments)
            }

            HomeScreenState.Error -> ErrorComponent {
                viewModel.getUsersAppointments()
            }

            HomeScreenState.Loading -> CircularProgress()
        }
    }
}

@Composable
fun HomeScreenContent(appointments: List<AppointmentModel>) {
    if (appointments.isNotEmpty()) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            itemsIndexed(appointments) { index, item ->
                AppointmentItem(appointmentModel = item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        Text(text = "You dont have any appointment!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppointmentItem(appointmentModel: AppointmentModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Text(
            text = appointmentModel.teacherName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            Text(text = "Date/Time: ", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Text(
                text = getDate(appointmentModel.dateTime.toLong(), "dd/MM/YYYY HH:MM"),
                fontSize = 16.sp
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getDate(milliSeconds: Long, dateFormat: String?): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return SimpleDateFormat(dateFormat).format(calendar.time)
}