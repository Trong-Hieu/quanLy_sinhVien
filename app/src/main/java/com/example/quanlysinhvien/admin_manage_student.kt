package com.example.quanlysinhvien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_admin_manage_student.*
import kotlinx.android.synthetic.main.activity_admin_show_detail.*
import kotlinx.android.synthetic.main.student_joined_activity_row.view.*

class admin_manage_student : AppCompatActivity() {

    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_student)

        fetchActivities()

    }

        companion object{
        val ACTIVITY_KEY = "ACTIVITY_KEY"
        val ACTIVITY_KEY2 = "ACTIVITY_KEY2"
    }

    private fun fetchActivities(){
        var adapter = GroupAdapter<GroupieViewHolder>()
        firebaseData.get().addOnCompleteListener {
            it.result!!.forEach {
                println("manage students id: " + it.id)
                var studentId = it.id
                var student = it.toObject(User::class.java)
                adapter.add(StudentManageItem(student, studentId))
            }

            adapter.setOnItemClickListener { item, view ->
                var intent = Intent(view.context, admin_show_student_detail::class.java)

                var manageStudentItem = item as StudentManageItem
                intent.putExtra(ACTIVITY_KEY, manageStudentItem.student)
                intent.putExtra(ACTIVITY_KEY2, manageStudentItem.studentId)
                startActivity(intent)

            }
            admin_manage_list_view_student.adapter = adapter
        }
    }
}


class StudentManageItem(val student: User, var studentId: String): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.student_joined_activity_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.student_joined_activity_name.text = student.name
    }

}
