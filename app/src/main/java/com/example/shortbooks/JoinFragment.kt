package com.example.shortbooks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentJoinBinding

class JoinFragment : Fragment(R.layout.fragment_join) {
    // 바인딩 객체 관리
    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰 바인딩 초기화
        _binding = FragmentJoinBinding.bind(view)

        // DB 헬퍼 초기화
        val dbHelper = DBHelper(requireContext())

        // 전체 동의 체크박스 제어 (일괄 변경)
        binding.cbAll.setOnClickListener {
            val state = binding.cbAll.isChecked
            binding.cbService.isChecked = state
            binding.cbPrivacy.isChecked = state
            binding.cbAge.isChecked = state
        }

        // 개별 체크박스 리스너 (전체 동의 상태 업데이트)
        val subListener = View.OnClickListener {
            binding.cbAll.isChecked = binding.cbService.isChecked &&
                    binding.cbPrivacy.isChecked &&
                    binding.cbAge.isChecked
        }
        binding.cbService.setOnClickListener(subListener)
        binding.cbPrivacy.setOnClickListener(subListener)
        binding.cbAge.setOnClickListener(subListener)

        // 가입 버튼 클릭 이벤트
        binding.btnJoinSubmit.setOnClickListener {
            // 입력 데이터 가공 및 동의 상태 확인
            val email = binding.etJoinEmail.text.toString().trim()
            val pw = binding.etJoinPw.text.toString().trim()
            val name = binding.etJoinNickname.text.toString().trim()
            val isChecked = binding.cbService.isChecked && binding.cbPrivacy.isChecked && binding.cbAge.isChecked

            // 입력값 유효성 검사 및 가입 처리
            if (email.isEmpty() || pw.isEmpty() || name.isEmpty()) {
                // 필수 정보 누락 확인
                Toast.makeText(context, "정보를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else if (pw.length < 8) {
                // 비밀번호 길이 조건 검사
                Toast.makeText(context, "비밀번호는 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if (!isChecked) {
                // 약관 동의 여부 확인
                Toast.makeText(context, "필수 항목에 동의하세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                // DB 유저 정보 삽입 실행
                if (dbHelper.insertUser(email, pw, name)) {
                    // 가입 성공 및 이전 화면 전환
                    Toast.makeText(context, "가입 성공", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    // 계정 중복 처리
                    Toast.makeText(context, "이미 존재하는 계정(이메일)입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 뷰 파괴 시 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}