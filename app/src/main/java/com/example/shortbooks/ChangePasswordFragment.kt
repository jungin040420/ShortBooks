package com.example.shortbooks

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChangePasswordBinding.bind(view)

        val dbHelper = DBHelper(requireContext())
        // 로그인된 사용자 정보 호출 ( 세션 데이터 호출)
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", "") ?: ""

        binding.btnChangePwSubmit.setOnClickListener {
            val currentPw = binding.etCurrentPw.text.toString()
            val newPw = binding.etNewPw.text.toString()
            val confirmPw = binding.etNewPwConfirm.text.toString()

            // 1. 유효성 검사 ( 비밀번호 검증)
            if (currentPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
                Toast.makeText(context, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!dbHelper.checkCurrentPassword(userEmail, currentPw)) {
                Toast.makeText(context, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (newPw != confirmPw) {
                Toast.makeText(context, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (newPw.length < 8) {
                Toast.makeText(context, "비밀번호는 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 2. DB 업데이트 실행 ( DB 비밀번호 수정)
                if (dbHelper.updatePassword(userEmail, newPw)) {
                    Toast.makeText(context, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}