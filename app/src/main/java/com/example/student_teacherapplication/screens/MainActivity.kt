// MainActivity.kt
package com.example.student_teacherapplication.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.student_teacherapplication.components.LoadingDialog
import com.example.student_teacherapplication.navigation.MainNavGraph
import com.example.student_teacherapplication.ui.theme.StudentTeacherApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel by viewModels()
            val navController = rememberNavController()
            val context = LocalContext.current

                //direkt en üst seviye navigasyon componentini buraya tanımladık
            StudentTeacherApplicationTheme(content = {
                MainNavGraph(navController = navController, viewModel = viewModel)
            })

            //viewmodelda bulunan show dialog true olduğunda ekranın üstüne bu loading componenti gösteirliyor bu sayede bu component teklendi
            if (viewModel.showLoadingDialog) {
                LoadingDialog()
            }

            //viewmodelda bulunan message parametresi eğer doldurulursa toast olarak ekranda gösteriliyor bu sayede  herhangi bir ekranda mesaj gösterilmek istendiğinde bu parametrenin tetiklenmesi yeterli
            viewModel.globalMessage?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.globalMessage = null
            }
        }
    }
}


