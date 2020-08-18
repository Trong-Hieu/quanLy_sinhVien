package com.example.quanlysinhvien

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_row.view.*
import kotlinx.android.synthetic.main.fragment_admin_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync

class Home : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")
    private val firebaseDataActivities: CollectionReference = FirebaseFirestore.getInstance().collection("Activities")
    private var navController: NavController? = null
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        if (firebaseAuth.currentUser == null){
            // user not login, go to login page
            navController!!.navigate(R.id.action_home2_to_sign_in)
        }
        else {
            firebaseData.document(firebaseAuth.currentUser!!.uid).get().addOnCompleteListener {
                var result = it.result
//            println("result: $result")
//            println("this bitch result: " + result!!.toObject(User::class.java))

                user = result!!.toObject(User::class.java)!!
                println("home bitch user: $user")
                if (user!!.bitchAdmin == "true"){
                    navController!!.navigate(R.id.action_home2_to_adminHome3)
                }
                else{
                    homeBtn_userInfo.text = user!!.name.toString()
                }


            }

            fetchActivities()
        }

        homeBtn_userInfo.setOnClickListener {
            var intent = Intent(it.context, UserInfor::class.java)
            startActivity(intent)
        }
        homeBtn_logOut.setOnClickListener {
            firebaseAuth.signOut()
            navController!!.navigate(R.id.action_home2_to_sign_in)
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
            user_activities_list_view.adapter = adapter
        }
    }

}
class HomeActivityItem(val activity: ActivityModal, val activityId: String): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.activity_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.activity_name.text = activity.name
    }

}