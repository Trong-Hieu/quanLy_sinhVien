package com.example.quanlysinhvien

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.fragment_sign_up.*

class sign_up : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpInform.text = ""

        var name = signUpInputName.text.toString()
        var dateOfBirth = signUpDateofBirth.text.toString()

        var gender: String = ""

        signUpGender.setOnCheckedChangeListener{group, i ->
            if (i == R.id.signUpMale){
                gender = signUpMale.text.toString()
                signUpInform.text = signUpMale.text.toString()
            }
            else if (i == R.id.signUpFemale){
                gender = signUpFemale.text.toString()
                signUpInform.text = signUpFemale.text.toString()
            }
        }



        signUpBtn.setOnClickListener{

            var name = signUpInputName.text.toString()
            var dateOfBirth = signUpDateofBirth.text.toString()
            var email = signUpInputEmail.text.toString()
            var password = signUpInputPassword.text.toString()
            var BitchAdmin = "false"
            var ActivitiesJoined: List<ActivityModal> = emptyList()
//            var activitiesJoined: List<String> = List(1){"this bitch"}
//            activitiesJoined.plus("this bitch 2")

            var user: User = User(name,dateOfBirth, gender, email, password, BitchAdmin, ActivitiesJoined)

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){

                    firebaseData.document(firebaseAuth.uid.toString()).set(user)

                    Toast.makeText(activity?.baseContext, "sign up success", Toast.LENGTH_LONG).show()

                    signUpInform.text = "sign up success"
                }
                else{
                    signUpInform.text = "sign up fail"

                    Toast.makeText(activity?.baseContext, "sign up fail", Toast.LENGTH_LONG).show()

                }
            }
            println(email)
            println(password)
        }


    }

}
