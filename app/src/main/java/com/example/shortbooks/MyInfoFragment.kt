package com.example.shortbooks

import android.content.Context // Context 임포트
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentMyInfoBinding

/**
 * 내 정보 프래그먼트: 로그인한 이메일 표시 및 화면 이동
 */
class MyInfoFragment : Fragment(R.layout.fragment_my_info) {

    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyInfoBinding.bind(view)

        // 1. 저장된 이메일 정보 호출
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("user_email", "로그인 정보 없음") // 저장된 값이 없으면 기본값 출력

        // 2. 화면에 실제 이메일 반영
        binding.tvUserEmail.text = "$savedEmail (인증됨)"

        // 3. 비밀번호 변경 메뉴 이동
        binding.menuChangePw.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChangePasswordFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}