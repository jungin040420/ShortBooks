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

        // [명사형 주석: 유저 정보 및 로그아웃 설정]
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

        // [명사형 주석: 메뉴 클릭 리스너]
        binding.menuSettings.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, MyInfoFragment()).addToBackStack(null).commit()
        }

        binding.menuLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }

        initCalendar()
    }

    private fun initCalendar() {
        val currentMonth = YearMonth.now()
        val dbHelper = DBHelper(requireContext()) // [명사형 주석: DB 헬퍼 초기화]

        // 초기 헤더 설정
        binding.tvCalendarHeader.text = "${currentMonth.year}년 ${currentMonth.monthValue}월"

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()

                if (day.position == DayPosition.MonthDate) {
                    container.textView.alpha = 1.0f
                    val dateString = day.date.toString()

                    // [명사형 주석: DB 조회 및 데이터 존재 시 배경 설정]
                    if (dbHelper.hasDataAtDate(dateString)) {
                        container.textView.setBackgroundResource(R.drawable.bg_calendar_attendance)
                        container.textView.setTextColor(Color.WHITE)
                    } else {
                        container.textView.background = null
                        container.textView.setTextColor(Color.BLACK)
                    }
                } else {
                    container.textView.alpha = 0.3f
                    container.textView.background = null
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        // 달력 범위 및 시작 위치 설정
        binding.calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), DayOfWeek.SUNDAY)
        binding.calendarView.scrollToMonth(currentMonth)

        // [명사형 주석: 달력 스크롤 시 상단 연/월 텍스트 동기화]
        binding.calendarView.monthScrollListener = { month ->
            val year = month.yearMonth.year
            val monthValue = month.yearMonth.monthValue
            binding.tvCalendarHeader.text = "${year}년 ${monthValue}월"
        }
    }

    private fun showLanguageSelectionDialog() {
        val bottomSheet = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_language_selection, null)
        bottomSheet.setContentView(dialogView)

        // [명사형 주석: 언어 버튼 참조 - findViewById 오타 수정]
        val btnKorean = dialogView.findViewById<View>(R.id.btnKorean)
        val btnEnglish = dialogView.findViewById<View>(R.id.btnEnglish)
        val btnJapanese = dialogView.findViewById<View>(R.id.btnJapanese)
        val tvKorean = dialogView.findViewById<TextView>(R.id.tvKorean)
        val tvEnglish = dialogView.findViewById<TextView>(R.id.tvEnglish)
        val tvJapanese = dialogView.findViewById<TextView>(R.id.tvJapanese)

        // [명사형 주석: 선택 상태 업데이트 함수]
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

        btnKorean.setOnClickListener { updateSelection("ko") }
        btnEnglish.setOnClickListener { updateSelection("en") }
        btnJapanese.setOnClickListener { updateSelection("ja") }

        bottomSheet.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// [명사형 주석: 캘린더 뷰 홀더 - inner 삭제로 에러 해결]
class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
}