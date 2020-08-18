package com.example.quanlysinhvien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_admin_show_student_detail.*
import kotlinx.android.synthetic.main.activity_row.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class admin_show_student_detail : AppCompatActivity() {

    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_show_student_detail)

        var student = intent.getParcelableExtra<User>(admin_manage_student.ACTIVITY_KEY)

        manage_student_name.text = student!!.name
        manage_student_birthday.text = student.dateOfBirth
        manage_student_gender.text = student.gender

        fetchActivities()

    }

    private fun fetchActivities(){
        var adapter = GroupAdapter<GroupieViewHolder>()
        var studenId = intent.getStringExtra(admin_manage_student.ACTIVITY_KEY2)
        println("manage student id: $studenId")
        firebaseData.document(studenId!!).collection("ActivitiesJoined").get().addOnCompleteListener {
            it.result!!.forEach {
                println("This activity id: " + it.id)
                var activityId = it.id
                var activity = it.toObject(ActivityModal::class.java)
                adapter.add(StudentActivityJoinedItem(activity, activityId))
            }

//            adapter.setOnItemClickListener { item, view ->
//                var intent = Intent(view.context, home_show_activity_detail::class.java)
//
//                var activityItem = item as HomeActivityItem
//                intent.putExtra(Home.ACTIVITY_KEY, activityItem.activity)
//                intent.putExtra(Home.ACTIVITY_KEY2, activityItem.activityId)
//                startActivity(intent)
            }
            manage_student_activityJoined.adapter = adapter
        }

}
class StudentActivityJoinedItem(val activity: ActivityModal, val activityId: String): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.activity_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.activity_name.text = activity.name
    }

}
