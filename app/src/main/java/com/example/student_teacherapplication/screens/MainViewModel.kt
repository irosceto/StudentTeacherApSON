package com.example.student_teacherapplication.screens

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.student_teacherapplication.data.AppointmentModel
import com.example.student_teacherapplication.data.LessonModel
import com.example.student_teacherapplication.data.TeacherModel
import com.example.student_teacherapplication.data.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar


class MainViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()


    var showLoadingDialog by mutableStateOf(false)
    var globalMessage: String? by mutableStateOf(null)


    var homeScreenState: HomeScreenState by mutableStateOf(HomeScreenState.Loading)
    var profileScreenState: ProfileScreenState by mutableStateOf(ProfileScreenState.Loading)
    var searchScreenState: SearchScreenState by mutableStateOf(SearchScreenState.Loading)


    var lessons: List<LessonModel>? = null
    var appointments: List<AppointmentModel>? = null
    var teachers: QuerySnapshot? = null
    var selectedTeacher: DocumentSnapshot? = null


    var showDates by mutableStateOf(false)
    val dates: ArrayList<Long> = arrayListOf()

    private fun setDates() {
        dates.clear()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        dates.add(calendar.timeInMillis)
        for (index in 0 until 5) {
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.add(Calendar.HOUR_OF_DAY, 2)
            dates.add(calendar.timeInMillis)
        }
        showDates = true
    }


    fun updateProfilePicture(picture: Uri) {
        showLoadingDialog = true
        try {
            val storageRef = storage.getReference("images/" + auth.uid)
            storageRef.putFile(picture).addOnCompleteListener {
                if (it.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener {
                        saveUserImage(it.toString())
                    }
                } else {
                    globalMessage = "Error while updating profile picture"
                }
                showLoadingDialog = false
            }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }
    }


    fun makeReservation(dateTime: Long) {
        showDates = false
        showLoadingDialog = true
        try {
            val reservation = hashMapOf(
                "teacherId" to selectedTeacher?.id,
                "teacherName" to selectedTeacher?.get("name"),
                "dateTime" to dateTime.toString(),
                "userId" to auth.currentUser?.uid
            )
            firestore.collection("appointments").add(reservation)
                .addOnSuccessListener {
                    getTeachersAppointments()
                }
                .addOnFailureListener { e ->
                    showLoadingDialog = false
                    globalMessage = "Error while making reservation"
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }
    }


    fun getUsersAppointments() {
        homeScreenState = HomeScreenState.Loading
        try {
            FirebaseFirestore.getInstance().collection("appointments")
                .whereEqualTo("userId", auth.currentUser?.uid).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        homeScreenState = if (it.result.isEmpty) {
                            HomeScreenState.Content(appointments = listOf())
                        } else {
                            appointments = it.result.toObjects(AppointmentModel::class.java)
                            HomeScreenState.Content(appointments = appointments.orEmpty())
                        }
                    } else {
                        homeScreenState = HomeScreenState.Error
                    }
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            homeScreenState = HomeScreenState.Error
        }
    }


    fun getTeachersAppointments() {
        showDates = false
        try {
            FirebaseFirestore.getInstance().collection("appointments")
                .whereEqualTo("teacherId", selectedTeacher?.id).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.isEmpty.not()) {
                            appointments = it.result.toObjects(AppointmentModel::class.java)
                        }
                        setDates()
                    } else {
                        globalMessage = "Error while loading the dates"
                    }
                    showLoadingDialog = false
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }
    }


    fun getLessons() {
        lessons = null
        teachers = null
        searchScreenState = SearchScreenState.Loading
        try {
            FirebaseFirestore.getInstance().collection("lessons").get().addOnCompleteListener {
                searchScreenState = if (it.isSuccessful) {
                    lessons = it.result?.toObjects(LessonModel::class.java)
                    SearchScreenState.Content(
                        lessons = lessons,
                        teachers = teachers?.toObjects(TeacherModel::class.java),
                    )
                } else {
                    SearchScreenState.Error
                }
            }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            searchScreenState = SearchScreenState.Error
        }
    }


    fun getTeachers(teacherIds: List<String>) {
        showLoadingDialog = true
        try {
            FirebaseFirestore.getInstance().collection("teachers").whereIn(
                FieldPath.documentId(), teacherIds
            ).get().addOnCompleteListener {
                searchScreenState = if (it.isSuccessful) {
                    teachers = it.result
                    SearchScreenState.Content(
                        lessons = lessons,
                        teachers = teachers?.toObjects(TeacherModel::class.java),
                    )
                } else {
                    globalMessage = "Error while fetching teacher"
                    SearchScreenState.Error
                }
                showLoadingDialog = false
            }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
            searchScreenState = SearchScreenState.Error
        }
    }


    fun getUser() {
        profileScreenState = ProfileScreenState.Loading
        try {
            firestore.collection("users").document(auth.uid.orEmpty()).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result.toObject(UserModel::class.java)?.let { user ->
                            profileScreenState = ProfileScreenState.Content(user)
                        }
                    } else {
                        profileScreenState = ProfileScreenState.Error
                    }
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            profileScreenState = ProfileScreenState.Error
        }
    }


    fun signIn(
        email: String?,
        password: String?,
        onComplete: () -> Unit,
    ) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            globalMessage = "Email and password cant be empty"
            return
        }

        showLoadingDialog = true
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete.invoke()
                } else {
                    globalMessage = "Error while sign in"
                }
                showLoadingDialog = false
            }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }

    }

    // şifremi unuttum ekranında reset password isteği atan kod
    fun sendResetPasswordMail(
        email: String?,
        onComplete: () -> Unit,
    ) {
        showLoadingDialog = true
        if (email.isNullOrEmpty()) {
            globalMessage = "Email cant be empty"
            return
        }
        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    globalMessage = if (task.isSuccessful) {
                        onComplete.invoke()
                        "Reset password mail sent to email address"
                    } else {
                        "Error while sending mail"
                    }
                    showLoadingDialog = false
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }

    }

    //kayıt ol ekranında kullanıcının register isteğini atan kod
    fun registerUser(
        email: String,
        password: String,
        name: String,
        lastname: String,
        onComplete: () -> Unit
    ) {
        showLoadingDialog = true
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserDetails(it.uid, name, lastname, email, onComplete)
                    }
                } else {
                    globalMessage = "Error while registration"
                    showLoadingDialog = false
                }
            }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }
    }

    // kayıt işlemi tamamlandıktan sonra kullanıcıyı firestore a kaydeden kod
    private fun saveUserDetails(
        userId: String,
        name: String,
        lastname: String,
        email: String,
        onComplete: () -> Unit
    ) {
        val user = hashMapOf(
            "name" to name,
            "lastname" to lastname,
            "email" to email,
        )
        try {
            firestore.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener {
                    onComplete.invoke()
                    globalMessage = "User registered successfully!"
                    showLoadingDialog = false
                }
                .addOnFailureListener { e ->
                    globalMessage = "Error while saving user info"
                    showLoadingDialog = false
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
            showLoadingDialog = false
        }
    }


    private fun saveUserImage(url: String) {
        val user = mapOf(
            "pictureUrl" to url,
        )
        try {
            firestore.collection("users").document(auth.uid.toString())
                .update(user)
                .addOnSuccessListener {
                    getUser()
                }
                .addOnFailureListener { e ->
                    globalMessage = "Error while saving user image"
                }
        } catch (e: Exception) {
            globalMessage = "An unexpected error was encountered"
        }
    }


    fun signOut() {
        auth.signOut()
    }
}

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    object Error : HomeScreenState()
    class Content(val appointments: List<AppointmentModel>) : HomeScreenState()
}


sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object Error : SearchScreenState()
    class Content(val lessons: List<LessonModel>?, val teachers: List<TeacherModel>?) :
        SearchScreenState()
}

sealed class ProfileScreenState {
    object Loading : ProfileScreenState()
    object Error : ProfileScreenState()
    class Content(val user: UserModel) : ProfileScreenState()
}

