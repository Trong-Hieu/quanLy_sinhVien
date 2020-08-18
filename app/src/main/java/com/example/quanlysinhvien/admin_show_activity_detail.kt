package com.example.quanlysinhvien

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_admin_show_detail.*
import kotlinx.android.synthetic.main.student_joined_activity_row.view.*

class admin_show_activity_detail : AppCompatActivity() {

    private val firebaseDataActivities: CollectionReference = FirebaseFirestore.getInstance().collection("Activities")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_show_detail)

        var activity = intent.getParcelableExtra<ActivityModal>(Home.ACTIVITY_KEY)
        var activityId = intent.getStringExtra(Home.ACTIVITY_KEY2)
        println("this activity: $activity")
        println("this activity Id: $activityId")

        admin_activity_name.text = activity!!.name
        admin_activity_filed.text = activity!!.field
        admin_activity_time.text = activity!!.time
        admin_joined_number.text = (activity!!.amount - activity!!.empty).toString()

        fetchActivities()
    }

//    companion object{
//        val ACTIVITY_KEY = "ACTIVITY_KEY"
//        val ACTIVITY_KEY2 = "ACTIVITY_KEY2"
//    }

    private fun fetchActivities(){
        var adapter = GroupAdapter<GroupieViewHolder>()
        var activityId = intent.getStringExtra(adminHome.ACTIVITY_KEY2)
        firebaseDataActivities.document(activityId!!).collection("StudentJoined").get().addOnCompleteListener {
            it.result!!.forEach {
                println("This student id: " + it.id)
                var student = it.toObject(User::class.java)
                adapter.add(StudentJoinedActivityItem(student))
            }

//            adapter.setOnItemClickListener { item, view ->
//                var intent = Intent(view.context, admin_show_activity_detail::class.java)
//
//                var activityItem = item as StudentJoinedActivityItem
//                intent.putExtra(ACTIVITY_KEY, activityItem.activity)
//                startActivity(intent)
//
//            }
            student_joined_list_view.adapter = adapter
        }
    }
}

class StudentJoinedActivityItem(val student: User): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.student_joined_activity_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.student_joined_activity_name.text = student.name
    }

}
