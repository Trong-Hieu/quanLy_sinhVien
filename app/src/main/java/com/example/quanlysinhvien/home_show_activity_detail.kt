package com.example.quanlysinhvien

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home_show_detail.*

class home_show_activity_detail : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")
    private val firebaseDataActivities: CollectionReference = FirebaseFirestore.getInstance().collection("Activities")
//    private var user: User? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_show_detail)

        var activity = intent.getParcelableExtra<ActivityModal>(Home.ACTIVITY_KEY)
        var activityId = intent.getStringExtra(Home.ACTIVITY_KEY2)
        println("this activity: $activity")
        println("this activity Id: $activityId")

        activity_name.text = activity!!.name
        activity_filed.text = activity.field
        activity_time.text = activity.time
        activity_amount.text = activity.amount.toString()
        activity_empty.text = activity.empty.toString()
        activity_request.text = activity.request
        activity_description.text = activity.description

        println("this current user: " + firebaseAuth.currentUser!!.uid)

        disableButton(cacelJoin_btn)

        firebaseData.document(firebaseAuth.currentUser!!.uid).get().addOnCompleteListener {
            var result = it.result
            var user = result!!.toObject(User::class.java)

            firebaseData.document(firebaseAuth.currentUser!!.uid).collection("ActivitiesJoined")
                .get().addOnCompleteListener {
                    it.result!!.forEach {

                        if (it.id == activityId){  // Nếu user đã đăng ký activity thì disable btn đk
                            ableButton(cacelJoin_btn)
                            cacelJoin_btn.text = "Hủy Đăng ký"
                            disableButton(home_joinActivity_btn)
                        }
                        else{ // Nếu user chưa đk activity

                            home_joinActivity_btn.setOnClickListener { // click vào btn đk
                                // ghi activity vào firebase của user
                                activity.empty--
                                firebaseData.document(firebaseAuth.currentUser!!.uid).collection("ActivitiesJoined")
                                    .document(activityId!!).set(activity)

                                // ghi user vào firebase của activity
                                firebaseDataActivities.document(activityId).collection("StudentJoined").document(
                                    firebaseAuth.currentUser!!.uid).set(user!!)

                                activity_empty.text = (activity.empty--).toString()
                                firebaseDataActivities.document(activityId).update("empty", activity.empty--)

                                disableButton(home_joinActivity_btn)
                                ableButton(cacelJoin_btn)
                            }
                        }
                    }
                }

                home_joinActivity_btn.setOnClickListener { // click vào btn đk
                    // ghi activity vào firebase của user
                    activity.empty--
                    firebaseData.document(firebaseAuth.currentUser!!.uid).collection("ActivitiesJoined")
                        .document(activityId!!).set(activity)

                    // ghi user vào firebase của activity
                    firebaseDataActivities.document(activityId).collection("StudentJoined").document(
                        firebaseAuth.currentUser!!.uid).set(user!!)

                    activity_empty.text = (activity.empty--).toString()
                    firebaseDataActivities.document(activityId).update("empty", activity.empty--)

                    disableButton(home_joinActivity_btn)
                }

                cacelJoin_btn.setOnClickListener {
                    firebaseData.document(firebaseAuth.currentUser!!.uid).collection("ActivitiesJoined")
                        .document(activityId!!).delete()

                    // ghi user vào firebase của activity
                    firebaseDataActivities.document(activityId).collection("StudentJoined").document(
                        firebaseAuth.currentUser!!.uid).delete()

                    activity_empty.text = (activity.empty++).toString()
                    firebaseDataActivities.document(activityId).update("empty", activity.empty++)

                    disableButton(cacelJoin_btn)
                    ableButton(home_joinActivity_btn)

                }

            }
//            var ActivitiesJoined: List<ActivityModal>  = user?.ActivitiesJoined!!

//            ActivitiesJoined.plus(activity)
//
//            println("Activities Joined: $ActivitiesJoined")
//
//            firebaseData.document(firebaseAuth.currentUser!!.uid).update("ActivitiesJoined", FieldValue.arrayUnion(ActivitiesJoined))



    }

    private fun disableButton(btn: Button){
        btn.isEnabled = false
        btn.isClickable = false
        btn.setBackgroundColor(696969)
    }
    private fun ableButton(btn: Button){
        btn.isEnabled = true
        btn.isClickable = true
        btn.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }


}
