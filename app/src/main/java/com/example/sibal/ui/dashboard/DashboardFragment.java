package com.example.sibal.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sibal.DBHelper;
import com.example.sibal.Earning_input;
import com.example.sibal.Input;
import com.example.sibal.MonthlyAmountCalculator;
import com.example.sibal.MyAdapter;
import com.example.sibal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.NumberFormat;

public class DashboardFragment extends Fragment {

    private TextView textDay;
    private ImageButton btnLeft;
    private ImageButton btnRight;
    private Button button;
    private TextView earning,pay;

    private MyAdapter expenseAdapter; // 추가: 지출 어댑터
    private ArrayList<String> expenseData; // 추가: 지출 데이터
    private ListView expenseListView; // 추가: 지출 리스트뷰
    private MyAdapter incomeAdapter; // 추가: 수입 어댑터
    private ArrayList<String> incomeData; // 추가: 수입 데이터
    private ListView incomeListView; // 추가: 수입 리스트뷰

    private TextView amount, card, classification;
    private View sbutton;
    private TextView expenditureTextView;
    private TextView expenditureDailyTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // DBHelper 객체 초기화
        DBHelper dbHelper = new DBHelper(requireContext());

        // 지출 데이터 어댑터 설정
        expenseData = dbHelper.getExpenseData(); // 데이터베이스에서 지출 데이터 가져오기
        expenseAdapter = new MyAdapter(requireContext(), expenseData);

        // XML 레이아웃에서 ListView ID를 확인하고, 해당 ID를 사용하여 expenseListView 초기화
        expenseListView = root.findViewById(R.id.listExpense);

        // expenseListView가 null이면 초기화 코드 추가
        if (expenseListView == null) {
            // expenseListView 초기화, 실제로 사용하는 XML 레이아웃에서 ListView에 할당된 ID로 바꾸세요.
            expenseListView = new ListView(requireContext());
        }

        // 어댑터 설정
        expenseListView.setAdapter(expenseAdapter);


        // +버튼을 누르면 input 액티비티로 이동
        button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Input.class);
                startActivity(intent);
            }
        });
        sbutton = root.findViewById(R.id.earningButton);
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Earning_input.class);
                startActivity(intent);
            }
        });

        Intent intent = getActivity().getIntent();

        String selectedDateFromInput = intent.getStringExtra("selectedDateFromInput");

        textDay = root.findViewById(R.id.text_day);

        if (selectedDateFromInput != null) {
            // Input 액티비티에서 전달받은 날짜를 textDay에 설정
            textDay.setText(selectedDateFromInput);
        }


        // text_day TextView의 클릭 리스너 설정
        textDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


//        revenueTextView = root.findViewById(R.id.text_revenue);
        expenditureTextView = root.findViewById(R.id.text_expenditure);
        expenditureDailyTextView = root.findViewById(R.id.text_revenue);
        // 형식화된 금액을 가져오기 위한 NumberFormat 객체 생성
        NumberFormat numberFormat = NumberFormat.getInstance();

        // 현재 날짜를 가져와서 "yyyy-MM" 형식으로 변환
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String targetMonth = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat_d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String targetDaily = dateFormat_d.format(calendar.getTime());

        // MonthlyAmountCalculator를 통해 월별 수입과 지출을 계산
        MonthlyAmountCalculator calculator = new MonthlyAmountCalculator(requireContext());
        int totalExpense = calculator.calculateMonthlyExpense(targetMonth);
        int totalDailyExpense = calculator.calculateDailyExpense(targetDaily);

        // 월별 지출을 형식화된 문자열로 변환하여 TextView에 설정
        expenditureTextView.setText(numberFormat.format(totalExpense));


        // 일별 지출을 형식화된 문자열로 변환하여 TextView에 설정
        expenditureDailyTextView.setText(numberFormat.format(totalDailyExpense));

        earning = root.findViewById(R.id.top).findViewById(R.id.all).findViewById(R.id.right).findViewById(R.id.text_earning);
        Intent intent2 = getActivity().getIntent();
        String earnstr = intent2.getStringExtra("str2");

        try {
            int earnInt = Integer.parseInt(earnstr);
            NumberFormat numberFormat_earning = NumberFormat.getInstance(Locale.getDefault());
//            earning.setText(String.valueOf(earnInt));
            earning.setText(String.valueOf(numberFormat_earning.format(earnInt)));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        earning = root.findViewById(R.id.top).findViewById(R.id.all).findViewById(R.id.right).findViewById(R.id.text_pay);
        Intent intent3 = getActivity().getIntent();
        String earnstr1 = intent2.getStringExtra("str2");

        try {
            Calendar calendar1 = Calendar.getInstance();
            int today = calendar1.get(Calendar.DAY_OF_MONTH);
            int lastDayOfMonth = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
            int earnInt = Integer.parseInt(earnstr);
            int remainingDays = lastDayOfMonth - today - 1;
            double result = (double) earnInt / remainingDays;
            NumberFormat numberFormat_e = NumberFormat.getInstance(Locale.getDefault());
            String resultString = numberFormat_e.format((int) result);
            earning.setText(resultString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        return root;
    }


    // DatePickerDialog를 보여주는 메서드
    private void showDatePickerDialog() {
        // 현재 날짜 가져오기
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog 생성 및 리스너 설정
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 선택된 날짜 처리
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

                        // DBHelper 인스턴스 생성
                        DBHelper dbHelper = new DBHelper(requireContext());

                        // 지출 데이터 가져오기
                        ArrayList<String> expenseDataByDate = dbHelper.getExpenseDataByDate(selectedDate);

                        // expenseAdapter에 데이터 설정
                        expenseAdapter.updateData(expenseDataByDate);

                        // 업데이트된 날짜를 TextView에 설정
                        updateTextView(selectedDate);
                    }
                },
                year, month, day);

        // 다이얼로그 표시
        datePickerDialog.show();
    }

    // TextView 업데이트 메서드
    private void updateTextView(String date) {
        textDay.setText(date);
    }
}