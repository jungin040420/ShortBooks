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
            val email = binding.etJoinEmail.text.toString().trim()
            val pw = binding.etJoinPw.text.toString().trim()
            val name = binding.etJoinNickname.text.toString().trim() // 닉네임 가져오기
            val isChecked = binding.cbService.isChecked && binding.cbPrivacy.isChecked && binding.cbAge.isChecked

            // 1. 필수 정보 입력 확인
            if (email.isEmpty() || pw.isEmpty() || name.isEmpty()) {
                Toast.makeText(context, "정보를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            // 2. 비밀번호 8자 이상 조건 추가 (요청하신 부분)
            else if (pw.length < 8) { // [명사형 주석: 비밀번호 길이 유효성 검사]
                Toast.makeText(context, "비밀번호는 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
            // 3. 필수 약관 동의 확인
            else if (!isChecked) {
                Toast.makeText(context, "필수 항목에 동의하세요.", Toast.LENGTH_SHORT).show()
            }
            // 4. 모든 조건 만족 시 DB 저장
            else {
                if (dbHelper.insertUser(email, pw, name)) {
                    Toast.makeText(context, "가입 성공", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "이미 존재하는 계정(이메일)입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}