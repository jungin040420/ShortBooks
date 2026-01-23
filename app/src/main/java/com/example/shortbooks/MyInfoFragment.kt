package com.example.shortbooks

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentMyInfoBinding

/**
 * 내 정보 프래그먼트: 로그인한 이메일 표시 및 화면 이동
 */
class MyInfoFragment : Fragment(R.layout.fragment_my_info) {

    // 바인딩 객체 관리
    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰 바인딩 초기화
        _binding = FragmentMyInfoBinding.bind(view)

        // 사용자 세션 정보(이메일) 추출
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("user_email", "로그인 정보 없음")

        // UI 텍스트 업데이트 (이메일 표시)
        binding.tvUserEmail.text = "$savedEmail (인증됨)"

        // 비밀번호 변경 화면 전환 (프래그먼트 교체)
        binding.menuChangePw.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChangePasswordFragment())
                .addToBackStack(null) // 이전 화면 복구 설정
                .commit()
        }
    }

    // 뷰 파괴 시 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}