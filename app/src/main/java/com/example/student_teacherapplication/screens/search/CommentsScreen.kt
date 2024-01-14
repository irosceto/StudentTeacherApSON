package com.example.student_teacherapplication.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_teacherapplication.R
import com.example.student_teacherapplication.components.ProfileImage
import com.example.student_teacherapplication.screens.MainViewModel


val names = listOf(
    "İrem", "Berfin", "Sinan", "Sinem", "Beril", "Metin"
)
val friendMessages = listOf(
    "This teacher is very kind and helpful.",
    "Thank you for helping me reduce my stress.",
    "Your lesson planning is very organized and understandable.\n This was very helpful for me.😋",
    "We really need to catch up!",
    "You gave me a lot of support regarding the exams.",
    "This teacher is not \npunctual at all\n! 😱",
    "We are grateful for your interest and support in our problems.",
    "A very rude teacher...",
    "The greatest teacher!!",
    "This teacher patiently answered \nall the questions I asked.\n"
)

@Composable
fun CommentsScreen(viewModel: MainViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.userprofile),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = viewModel.selectedTeacher?.get("name").toString(), color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(64.dp))

        for (i in 0 until 10) {
            CommentItem(names.random(), friendMessages.random())

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CommentItem(name: String, message: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(text = name, style = TextStyle(fontWeight = FontWeight.Bold))
        Text(text = message)
    }
}