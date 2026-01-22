package com.example.shortbooks

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shortbooks.databinding.FragmentMypageBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.YearMonth

class MyPageFragment : Fragment(R.layout.fragment_mypage) {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMypageBinding.bind(view)

        // 사용자 정보 설정 (세션 정보 반영)
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val userName = sharedPref.getString("user_name", "로그인이 필요합니다")

        binding.tvUserName.text = if (isLoggedIn) "${userName} 님" else "로그인이 필요합니다"
        binding.btnLogout.text = if (isLoggedIn) "로그아웃" else "로그인"

        binding.btnLogout.setOnClickListener {
            if (isLoggedIn) {
                sharedPref.edit().clear().apply()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container, MyPageFragment()).commit()
            } else {
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container, LoginFragment()).addToBackStack(null).commit()
            }
        }

        initCalendar()
    }

    private fun initCalendar() {
        val currentMonth = YearMonth.now()
        val dbHelper = DBHelper(requireContext()) // [명사형 주석: DB 헬퍼 초기화]

        // 초기 헤더 설정
        binding.tvCalendarHeader.text = "${currentMonth.year}년 ${currentMonth.monthValue}월"

        // 날짜 바인딩 설정
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                // 1. 기본 날짜 텍스트 설정
                container.textView.text = day.date.dayOfMonth.toString()

                // 2. 이번 달 날짜인 경우에만 로직 적용
                if (day.position == DayPosition.MonthDate) {
                    container.textView.alpha = 1.0f

                    // 3. DB 확인 (yyyy-MM-dd 형식 문자열로 변환)
                    val dateString = day.date.toString()

                    if (dbHelper.hasDataAtDate(dateString)) {
                        // [명사형 주석: 데이터 존재 시 배경 및 글자색 변경]
                        container.textView.setBackgroundResource(R.drawable.bg_calendar_attendance)
                        container.textView.setTextColor(Color.WHITE) // 흰색 글자로 가독성 향상
                    } else {
                        // [명사형 주석: 데이터 없을 시 기본 상태]
                        container.textView.background = null
                        container.textView.setTextColor(Color.BLACK)
                    }
                } else {
                    // [명사형 주석: 다른 달 날짜 투명 처리]
                    container.textView.alpha = 0.3f
                    container.textView.background = null
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        // 달력 범위 및 시작 위치 설정
        binding.calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), DayOfWeek.SUNDAY)
        binding.calendarView.scrollToMonth(currentMonth)

        // 스크롤 시 상단 연/월 업데이트
        binding.calendarView.monthScrollListener = { month ->
            binding.tvCalendarHeader.text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월"
        }
    }

    // 내부 컨테이너 클래스 (뷰 홀더)
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.calendarDayText) // XML 아이디 일치 확인
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}