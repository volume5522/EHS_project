package com.example.sibal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Earning_input extends AppCompatActivity {

    private Button save;
    private EditText earningamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earning_input);

        // XML 레이아웃에서 해당 뷰들을 찾음
        earningamount = findViewById(R.id.earningamount);
        save = findViewById(R.id.save);

        // 버튼 클릭 리스너 설정
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력값을 가져옴
                String str2 = earningamount.getText().toString();

                // MainActivity로 전환하면서 입력값을 전달
                Intent intent = new Intent(Earning_input.this, MainActivity.class);
                intent.putExtra("str2", str2);
                startActivity(intent);
            }
        });
    }
}
