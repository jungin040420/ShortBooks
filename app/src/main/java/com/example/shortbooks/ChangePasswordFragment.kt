package com.example.shortbooks

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    // 바인딩 객체 선언
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰 바인딩 초기화
        _binding = FragmentChangePasswordBinding.bind(view)

        // DB 헬퍼 초기화
        val dbHelper = DBHelper(requireContext())

        // 로그인된 사용자 세션 정보(이메일) 추출
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", "") ?: ""

        // 비밀번호 변경 제출 버튼 클릭 이벤트
        binding.btnChangePwSubmit.setOnClickListener {
            // 입력 데이터 가공
            val currentPw = binding.etCurrentPw.text.toString()
            val newPw = binding.etNewPw.text.toString()
            val confirmPw = binding.etNewPwConfirm.text.toString()

            // 입력값 유효성 검증
            if (currentPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
                // 공백 필드 체크
                Toast.makeText(context, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!dbHelper.checkCurrentPassword(userEmail, currentPw)) {
                // 현재 비밀번호 일치 여부 확인
                Toast.makeText(context, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (newPw != confirmPw) {
                // 새 비밀번호 일치 여부 확인
                Toast.makeText(context, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (newPw.length < 8) {
                // 비밀번호 길이 제한 확인
                Toast.makeText(context, "비밀번호는 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                // DB 비밀번호 업데이트 실행
                if (dbHelper.updatePassword(userEmail, newPw)) {
                    // 변경 성공 및 이전 화면 복귀
                    Toast.makeText(context, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
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