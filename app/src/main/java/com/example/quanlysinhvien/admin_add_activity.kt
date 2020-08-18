package com.example.quanlysinhvien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin_add_activity.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class admin_add_activity : AppCompatActivity() {

    private val firebaseDataActivities: CollectionReference = FirebaseFirestore.getInstance().collection("Activities")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_activity)

        admin_add_activity_btn.setOnClickListener {
            var name = input_activity_name.text.toString()
            var field = input_activity_field.text.toString()
            var time = input_activity_time.text.toString()
            var amount = input_activity_amount.text.toString().toInt()
            var empty = amount
            var request = input_activity_request.text.toString()
            var description = input_activity_description.text.toString()

            var activity = ActivityModal(name, field, time, amount, empty, request, description)

            firebaseDataActivities.add(activity)

            println("Activity added: $activity")

            disableButton(admin_add_activity_btn)

            var intent = Intent(it.context, adminHome::class.java)
            startActivity(intent)

        }

    }

    private fun disableButton(btn: Button){
        btn.text = "Đã thêm"
        btn.isEnabled = false
        btn.isClickable = false
        btn.setBackgroundColor(696969)
    }

}
