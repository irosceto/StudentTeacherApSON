package com.example.student_teacherapplication.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//rastgele isim ve mesaj listesi
val names = listOf(
    "Berfin", "İrem", "Deniz", "Sinem", "Can", "Metin"
)
val friendMessages = listOf(
    "This teacher is very kind and helpful.",
    "I've been meaning to chat!",
    "Your lesson planning is very organized and understandable.\n This was very helpful for me.😋",
    "We really need to catch up!",
    "It's been too long!",
    "This teacher is not \npunctual at all\n! 😱",
    "We are grateful for your interest and support in our problems.",
    "A very rude teacher...",
    "The greatest teacher!",
    "This teacher patiently answered \nall the questions I asked.\n"
)

@Composable
fun MessagesScreen() {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(count = 20) {
            MessageItem(names.random(), friendMessages.random())
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MessageItem(name: String, message: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(text = name, style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 18.sp)
        Text(text = message, fontSize = 16.sp)
    }
}