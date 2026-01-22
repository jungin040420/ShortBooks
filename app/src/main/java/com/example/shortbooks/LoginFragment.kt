package com.example.shortbooks

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        val dbHelper = DBHelper(requireContext())

        binding.btnLoginSubmit.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val pw = binding.etLoginPw.text.toString()

            if (dbHelper.checkUser(email, pw)) {
                val name = dbHelper.getUserName(email)
                // 세션 데이터 저장
                val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", true)
                    putString("user_email", email)
                    putString("user_name", name)
                    apply()
                }
                Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoJoin.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, JoinFragment()).addToBackStack(null).commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView();
        _binding = null
    }
}