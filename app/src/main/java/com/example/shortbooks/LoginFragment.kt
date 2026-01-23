package com.example.shortbooks

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    // 바인딩 객체 관리
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰 바인딩 초기화
        _binding = FragmentLoginBinding.bind(view)

        // DB 헬퍼 초기화
        val dbHelper = DBHelper(requireContext())

        // 로그인 버튼 클릭 이벤트
        binding.btnLoginSubmit.setOnClickListener {
            // 입력 데이터 가공
            val email = binding.etLoginEmail.text.toString()
            val pw = binding.etLoginPw.text.toString()

            // 계정 정보 일치 여부 확인
            if (dbHelper.checkUser(email, pw)) {
                // 사용자 이름 조회
                val name = dbHelper.getUserName(email)

                // 세션 데이터 저장 (SharedPreferences 활용)
                val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("is_logged_in", true) // 로그인 상태 저장
                    putString("user_email", email)   // 이메일 정보 저장
                    putString("user_name", name)     // 이름 정보 저장
                    apply()
                }

                // 로그인 완료 알림 및 이전 화면 전환
                Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                // 로그인 실패 처리
                Toast.makeText(context, "정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 텍스트 클릭 이벤트 (회원가입 화면 전환)
        binding.tvGoJoin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JoinFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    // 뷰 파괴 시 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView();
        _binding = null
    }
}