package com.example.sibal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Input extends AppCompatActivity {

    private Button saveBtn; // 저장 버튼
    private EditText edtAmount, edtDate; // 금액과 날짜 입력을 위한 에딧텍스트

    private Spinner edtCard, edtClass; // 카드와 분류를 선택하기 위한 스피너

    private String str, card, classification; // 입력된 데이터를 저장할 변수들
    private Calendar myCalendar = Calendar.getInstance(); // 날짜 선택에 사용할 캘린더 객체
    // DBHelper 객체 선언
    private DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input); // input.xml 레이아웃을 화면에 표시

        // DBHelper 객체 초기화
        dbHelper = new DBHelper(this);

        // UI 요소 초기화
        edtAmount = findViewById(R.id.edtAmount);
        edtDate = findViewById(R.id.edtDate);
        edtCard = findViewById(R.id.edtCard);
        edtClass = findViewById(R.id.edtClass);

        // Card 선택을 위한 어댑터 설정
        ArrayAdapter<CharSequence> cardAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Card,
                android.R.layout.simple_spinner_item
        );
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtCard.setAdapter(cardAdapter);

        // Class 선택을 위한 어댑터 설정
        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.Class,
                android.R.layout.simple_spinner_item
        );
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtClass.setAdapter(classAdapter);

        // 달력 아이콘을 클릭했을 때 날짜 선택 다이얼로그 표시
        ImageView calendarIllustration = findViewById(R.id.calendarillustration);
        calendarIllustration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // 저장하기 버튼 클릭 리스너
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 데이터 가져오기
                String selectedDate = edtDate.getText().toString();
                card = edtCard.getSelectedItem().toString();
                classification = edtClass.getSelectedItem().toString();
                str = edtAmount.getText().toString();
                // 버튼의 태그를 읽어옴
                String tag = (String) v.getTag();

                // 수입인 경우
                if ("income".equals(tag)) {
                    dbHelper.addIncome(myCalendar.getTime(), card, classification, Integer.parseInt(str));
                    Toast.makeText(getApplicationContext(), "수입이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
                // 지출인 경우
                else if ("expense".equals(tag)) {
                    dbHelper.addExpense(myCalendar.getTime(), card, classification, Integer.parseInt(str));
                    Toast.makeText(getApplicationContext(), "지출이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
//                // 데이터베이스에 수입 데이터 추가
//                dbHelper.addIncome(myCalendar.getTime(), card, classification, Integer.parseInt(str));


//                // MainActivity로 데이터 전달하는 인텐트 생성
//                Intent intent = new Intent(Input.this, MainActivity.class);
//                intent.putExtra("str", str);
//                intent.putExtra("selectedDate", selectedDate);
//                intent.putExtra("card", card);
//                intent.putExtra("classification", classification);
//                intent.putExtra("selectedDateFromInput", selectedDate);
//                startActivity(intent);

                // YourActivity의 updateAdapters 메서드 호출
                ((YourActivity)getParent()).updateAdapters();





            }
        });
    }

    // 날짜 선택 다이얼로그를 표시하는 메소드
    private void showDatePickerDialog() {
        new DatePickerDialog(
                this,
                myDatePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    // 날짜가 선택되었을 때 실행되는 리스너
    private DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    // 날짜 선택 후 EditText에 업데이트하는 메소드
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        edtDate.setText(sdf.format(myCalendar.getTime()));
    }
}
