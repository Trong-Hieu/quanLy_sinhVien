package com.example.quanlysinhvien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_row.view.*
import kotlinx.android.synthetic.main.activity_user_infor.*
import kotlinx.android.synthetic.main.fragment_home.*

class UserInfor : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")
    private val firebaseDataActivities: CollectionReference = FirebaseFirestore.getInstance().collection("Activities")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_infor)

        firebaseData.document(firebaseAuth.currentUser!!.uid).get().addOnCompleteListener {
            var result = it.result
            var user = result!!.toObject(User::class.java)

            studentName.text = user!!.name
            studentGender.text = user.gender
            studentBirthday.text = user.dateOfBirth

            fetchActivities()

        }

    }

    companion object{
        val ACTIVITY_KEY = "ACTIVITY_KEY"
        val ACTIVITY_KEY2 = "ACTIVITY_KEY2"
    }

    private fun fetchActivities(){
        var adapter = GroupAdapter<GroupieViewHolder>()
        firebaseData.document(firebaseAuth.currentUser!!.uid).collection("ActivitiesJoined").get().addOnCompleteListener {
            it.result!!.forEach {
                println("This activity id: " + it.id)
                var activityId = it.id
                var activity = it.toObject(ActivityModal::class.java)
                adapter.add(HomeActivityItem(activity, activityId))
            }

            adapter.setOnItemClickListener { item, view ->
                var intent = Intent(view.context, home_show_activity_detail::class.java)

                var activityItem = item as HomeActivityItem
                intent.putExtra(ACTIVITY_KEY, activityItem.activity)
                intent.putExtra(ACTIVITY_KEY2, activityItem.activityId)
                startActivity(intent)

//                navController!!.navigate(R.id.action_home2_to_user_activity_detail)
            }
            student_ActivitiesJoint_list_view.adapter = adapter
        }
    }
}

