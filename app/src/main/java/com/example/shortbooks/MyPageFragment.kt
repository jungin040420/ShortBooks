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
    // 바인딩 객체 관리
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMypageBinding.bind(view)

        // 사용자 정보 및 로그인 세션 호출
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val userName = sharedPref.getString("user_name", "로그인이 필요합니다")

        // 로그인 상태별 UI 텍스트 설정
        binding.tvUserName.text = if (isLoggedIn) "${userName} 님" else "로그인이 필요합니다"
        binding.btnLogout.text = if (isLoggedIn) "로그아웃" else "로그인"

        // 로그아웃/로그인 버튼 클릭 이벤트
        binding.btnLogout.setOnClickListener {
            if (isLoggedIn) {
                // 세션 초기화 및 마이페이지 새로고침
                sharedPref.edit().clear().apply()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container, MyPageFragment()).commit()
            } else {
                // 로그인 화면으로 이동
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container, LoginFragment()).addToBackStack(null).commit()
            }
        }

        // 설정(내 정보) 메뉴 클릭 리스너
        binding.menuSettings.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, MyInfoFragment()).addToBackStack(null).commit()
        }

        // 언어 선택 메뉴 클릭 리스너
        binding.menuLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }

        // 캘린더 초기화 실행
        initCalendar()
    }

    // 캘린더 뷰 설정 및 데이터 바인딩
    private fun initCalendar() {
        val currentMonth = YearMonth.now()
        val dbHelper = DBHelper(requireContext())

        // 상단 날짜 헤더 초기화
        binding.tvCalendarHeader.text = "${currentMonth.year}년 ${currentMonth.monthValue}월"

        // 날짜별 데이터 바인딩 설정
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()

                if (day.position == DayPosition.MonthDate) {
                    // 현재 월의 날짜 표시 설정
                    container.textView.alpha = 1.0f
                    val dateString = day.date.toString()

                    // DB 기록 존재 여부 확인 및 배경색(출석 표시) 적용
                    if (dbHelper.hasDataAtDate(dateString)) {
                        container.textView.setBackgroundResource(R.drawable.bg_calendar_attendance)
                        container.textView.setTextColor(Color.WHITE)
                    } else {
                        container.textView.background = null
                        container.textView.setTextColor(Color.BLACK)
                    }
                } else {
                    // 이전/다음 월 날짜 흐리게 표시
                    container.textView.alpha = 0.3f
                    container.textView.background = null
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        // 캘린더 범위 및 초기 위치 설정
        binding.calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), DayOfWeek.SUNDAY)
        binding.calendarView.scrollToMonth(currentMonth)

        // 월 스크롤 시 헤더 날짜 텍스트 동기화
        binding.calendarView.monthScrollListener = { month ->
            val year = month.yearMonth.year
            val monthValue = month.yearMonth.monthValue
            binding.tvCalendarHeader.text = "${year}년 ${monthValue}월"
        }
    }

    // 언어 선택 바텀시트 다이얼로그 표시
    private fun showLanguageSelectionDialog() {
        val bottomSheet = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_language_selection, null)
        bottomSheet.setContentView(dialogView)

        // 다이얼로그 내 UI 요소 참조
        val btnKorean = dialogView.findViewById<View>(R.id.btnKorean)
        val btnEnglish = dialogView.findViewById<View>(R.id.btnEnglish)
        val btnJapanese = dialogView.findViewById<View>(R.id.btnJapanese)
        val tvKorean = dialogView.findViewById<TextView>(R.id.tvKorean)
        val tvEnglish = dialogView.findViewById<TextView>(R.id.tvEnglish)
        val tvJapanese = dialogView.findViewById<TextView>(R.id.tvJapanese)

        // 선택 언어에 따른 UI 상태 업데이트 함수
        fun updateSelection(selected: String) {
            val selectedBg = R.drawable.bg_language_selected
            val unselectedBg = R.drawable.bg_language_unselected

            btnKorean.setBackgroundResource(if (selected == "ko") selectedBg else unselectedBg)
            tvKorean.setTextColor(if (selected == "ko") Color.WHITE else Color.BLACK)

            btnEnglish.setBackgroundResource(if (selected == "en") selectedBg else unselectedBg)
            tvEnglish.setTextColor(if (selected == "en") Color.WHITE else Color.BLACK)

            btnJapanese.setBackgroundResource(if (selected == "ja") selectedBg else unselectedBg)
            tvJapanese.setTextColor(if (selected == "ja") Color.WHITE else Color.BLACK)
        }

        // 언어별 클릭 이벤트 연결
        btnKorean.setOnClickListener { updateSelection("ko") }
        btnEnglish.setOnClickListener { updateSelection("en") }
        btnJapanese.setOnClickListener { updateSelection("ja") }

        bottomSheet.show()
    }

    // 뷰 파괴 시 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 개별 날짜 뷰 홀더 관리
class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
}