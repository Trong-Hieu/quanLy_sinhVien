package com.example.quanlysinhvien

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_in.*


class sign_in : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseData: CollectionReference = FirebaseFirestore.getInstance().collection("StudentsInfo")
    private var navController: NavController? = null
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup navController
        navController = Navigation.findNavController(view)

        inform.text = ""


        btnSignIn.setOnClickListener {
            var email = inputEmail.text.toString();
            var password = inputPassword.text.toString();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    // correct account, go to home page
                    println("this bitch id: " + firebaseAuth.uid)
                    firebaseData.document(firebaseAuth.uid!!).get().addOnCompleteListener {
                        var result = it.result
                        println("result: $result")
                        println("this bitch result: " + result!!.toObject(User::class.java))
                        user = result.toObject(User::class.java)

                        if (user!!.bitchAdmin == "true"){
                            navController!!.navigate(R.id.action_sign_in_to_adminHome3)
                        }
                        else{
                            println("this bitch user: $user")
                            navController!!.navigate(R.id.action_sign_in_to_home2)
                        }




                    }
//                    println("this bitch user: $user")
//                    navController!!.navigate(R.id.action_sign_in_to_home2)
//                    inform.text = user!!.name.toString()
                }
                else {
                    Toast.makeText(activity?.baseContext, "invalid account", Toast.LENGTH_LONG).show()
                    inform.text = "sign in fail";
                }
            }
        }

        btnSignUp.setOnClickListener{
            navController!!.navigate(R.id.action_sign_in_to_sign_up)
        }
    }
}
