package com.example.student_teacherapplication.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_teacherapplication.components.CircularProgress
import com.example.student_teacherapplication.components.CustomDropdownMenu
import com.example.student_teacherapplication.components.ErrorComponent
import com.example.student_teacherapplication.data.LessonModel
import com.example.student_teacherapplication.data.TeacherModel
import com.example.student_teacherapplication.screens.HomeScreenState
import com.example.student_teacherapplication.screens.MainViewModel
import com.example.student_teacherapplication.screens.SearchScreenState
import com.example.student_teacherapplication.screens.home.HomeScreenContent

@Composable
fun SearchScreen(viewModel: MainViewModel, navigateToAppointment: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.getLessons()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = viewModel.searchScreenState) {
            is SearchScreenState.Content -> {
                SearchScreenContent(
                    lessons = state.lessons,
                    teachers = state.teachers,
                    getLessonsTeacher = { teacherIds ->
                        viewModel.getTeachers(teacherIds)
                    },
                    navigateToAppointment = {
                        viewModel.selectedTeacher = viewModel.teachers?.documents?.get(it)
                        navigateToAppointment.invoke()
                    }
                )
            }

            SearchScreenState.Error -> ErrorComponent {
                viewModel.getLessons()
            }

            SearchScreenState.Loading -> CircularProgress()
        }
    }
}

@Composable
fun SearchScreenContent(
    lessons: List<LessonModel>?,
    teachers: List<TeacherModel>?,
    getLessonsTeacher: (List<String>) -> Unit,
    navigateToAppointment: (Int) -> Unit,
) {
    var selectedLesson: LessonModel? by remember { mutableStateOf(null) }
    var selectedTeacher: TeacherModel? by remember { mutableStateOf(null) }
    var showDetailButtons by remember { mutableStateOf(false) }

    LaunchedEffect(selectedLesson) {
        if (selectedLesson != null && selectedLesson?.teachers?.isNotEmpty() == true) {
            getLessonsTeacher.invoke(selectedLesson!!.teachers)
            selectedTeacher = null
        }
    }

    LaunchedEffect(selectedTeacher) {
        showDetailButtons = if (selectedTeacher != null) {
            true
        } else {
            false
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomDropdownMenu(
            placeHolder = "Please Select Lesson",
            selectedItem = selectedLesson?.name.orEmpty(),
            onItemSelect = {
                selectedLesson = lessons?.get(it)
            },
            items = lessons?.map { it.name }.orEmpty()
        )
        if (teachers.isNullOrEmpty().not()) {
            Spacer(modifier = Modifier.height(8.dp))

            CustomDropdownMenu(
                placeHolder = "Please Select Teacher",
                selectedItem = selectedTeacher?.name.orEmpty(),
                onItemSelect = {
                    selectedTeacher = teachers?.get(it)
                },
                items = teachers?.map { it.name }.orEmpty()
            )
        }

        if (showDetailButtons) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                navigateToAppointment.invoke(
                    teachers?.indexOf(selectedTeacher) ?: 0
                )
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Make an appointment", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Video call", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Chat", fontSize = 16.sp)
            }
        }
    }
}