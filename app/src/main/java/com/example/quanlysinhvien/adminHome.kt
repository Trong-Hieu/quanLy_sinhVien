package com.example.quanlysinhvien

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_row.view.*
import kotlinx.android.synthetic.main.fragment_admin_home.*
import kotlinx.android.synthetic.main.fragment_home.*


class adminHome : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")
    private val firebaseDataActivities: CollectionReference = FirebaseFirestore.getInstance().collection("Activities")
    private var user: User? = null
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        firebaseData.get().addOnCompleteListener {
            var result = it.result
            println("All users: " + result!!.toObjects(User::class.java))
        }

        admin_logOut_btn.setOnClickListener {
            firebaseAuth.signOut()
            navController!!.navigate(R.id.action_adminHome3_to_sign_in)
        }

        if (firebaseAuth.currentUser == null){
            // user not login, go to login page
            navController!!.navigate(R.id.action_adminHome3_to_sign_in)
        }


        fetchActivities()

        admin_add_btn.setOnClickListener {
            var intent = Intent(it.context, admin_add_activity::class.java)
            startActivity(intent)
        }

        admin_manage_btn.setOnClickListener {
            var intent = Intent(it.context, admin_manage_student::class.java)
            startActivity(intent)
        }


    }

    companion object{
        val ACTIVITY_KEY = "ACTIVITY_KEY"
        val ACTIVITY_KEY2 = "ACTIVITY_KEY2"
    }

    private fun fetchActivities(){
        var adapter = GroupAdapter<GroupieViewHolder>()
        firebaseDataActivities.get().addOnCompleteListener {
            it.result!!.forEach {
                println("This activity id: " + it.id)
                var activityId = it.id
                var activity = it.toObject(ActivityModal::class.java)
                adapter.add(AdminActivityItem(activity, activityId))
            }

            adapter.setOnItemClickListener { item, view ->
                var intent = Intent(view.context, admin_show_activity_detail::class.java)

                var activityItem = item as AdminActivityItem
                intent.putExtra(ACTIVITY_KEY, activityItem.activity)
                intent.putExtra(ACTIVITY_KEY2, activityItem.activityId)
                startActivity(intent)

            }
            activities_list_view.adapter = adapter
        }
    }


}

class AdminActivityItem(val activity: ActivityModal, val activityId: String): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.activity_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.activity_name.text = activity.name
    }

}
