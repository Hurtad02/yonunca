package com.example.yonunca_juegoparabeber.online.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.BaseApplication.Companion.getApplicationContext
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class ForgotPasswordFragment : BaseFragment() {
    private var _binding: FragmentForgotPasswordBinding? = null

    private val binding get() = _binding!!

    private var email by Delegates.notNull<String>()
    private lateinit var userEmail: EditText

    private lateinit var auth: FirebaseAuth

    override fun setListeners() {
        binding.resetPasswordButton.setOnClickListener {
            resetPasswordFun(requireView())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    fun resetPasswordFun(view: View){
        resetPassword()
        findNavController().navigate(R.id.forgotPasswordFragment_to_mainFragment)
    }

    private fun resetPassword(){
        email = userEmail.text.toString()
        if (!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(getApplicationContext(), "Email enviado a $email", Toast.LENGTH_LONG).show()
                    }
                    else Toast.makeText(getApplicationContext(), "No se encontró usuario con el correo $email", Toast.LENGTH_LONG).show()
                }
        } else Toast.makeText(getApplicationContext(), "Indica un email, por favor", Toast.LENGTH_SHORT).show()
    }

    override fun setObservers() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        userEmail = binding.email
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}