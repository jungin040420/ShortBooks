package com.example.shortbooks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentJoinBinding

class JoinFragment : Fragment(R.layout.fragment_join) {
    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentJoinBinding.bind(view)

        val dbHelper = DBHelper(requireContext())

        // 전체 동의 클릭 시 일괄 변경
        binding.cbAll.setOnClickListener {
            val state = binding.cbAll.isChecked
            binding.cbService.isChecked = state
            binding.cbPrivacy.isChecked = state
            binding.cbAge.isChecked = state
        }

        // 개별 항목 클릭 시 전체 동의 상태 반영
        val subListener = View.OnClickListener {
            binding.cbAll.isChecked = binding.cbService.isChecked &&
                    binding.cbPrivacy.isChecked &&
                    binding.cbAge.isChecked
        }
        binding.cbService.setOnClickListener(subListener)
        binding.cbPrivacy.setOnClickListener(subListener)
        binding.cbAge.setOnClickListener(subListener)

        // 가입 버튼 클릭 로직
        binding.btnJoinSubmit.setOnClickListener {
            val email = binding.etJoinEmail.text.toString()
            val pw = binding.etJoinPw.text.toString()
            val name = binding.etJoinNickname.text.toString()
            val isChecked = binding.cbService.isChecked && binding.cbPrivacy.isChecked && binding.cbAge.isChecked

            if (email.isEmpty() || pw.isEmpty()) {
                Toast.makeText(context, "정보를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (!isChecked) {
                Toast.makeText(context, "필수 항목에 동의하세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (dbHelper.insertUser(email, pw, name)) {
                    Toast.makeText(context, "가입 성공", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "이미 존재하는 계정", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}