package com.example.sibal;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class YourActivity extends AppCompatActivity {
    private ListView incomeListView, expenseListView;
    private MyAdapter incomeAdapter, expenseAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        dbHelper = new DBHelper(this);


//        // 수입 데이터 조회
//        ArrayList<String> incomeDataList = dbHelper.getIncomeData();
//        incomeListView = findViewById(R.id.listIncome);
//        incomeAdapter = new MyAdapter(this, incomeDataList);
//        incomeListView.setAdapter(incomeAdapter);

        // 지출 데이터 조회
        ArrayList<String> expenseDataList = dbHelper.getExpenseData();
        expenseListView = findViewById(R.id.listExpense);
        expenseAdapter = new MyAdapter(this, expenseDataList);
        expenseListView.setAdapter(expenseAdapter);

        // 데이터베이스에서 새로운 데이터를 가져와 어댑터 업데이트
        updateAdapters();
    }

    // 데이터베이스에서 새로운 데이터를 가져와 어댑터 업데이트하는 메서드 추가
//    private void updateAdapters() {
//        // 예를 들어, 데이터베이스에서 새로운 수입 데이터를 가져옴
//        ArrayList<String> newIncomeData = dbHelper.getIncomeData();
//
//        // 어댑터에 데이터 업데이트
//        incomeAdapter.updateData(newIncomeData);
//
//        // 마찬가지로 지출 데이터도 업데이트 가능
//        ArrayList<String> newExpenseData = dbHelper.getExpenseData();
//        expenseAdapter.updateData(newExpenseData);
//    }
//}

    protected void updateAdapters() {
        if (this != null) { // 객체가 null이 아닌지 확인{
            // 수입 데이터 업데이트
            ArrayList<String> newIncomeData = dbHelper.getIncomeData();
            Log.d("YourActivity", "New Income Data: " + newIncomeData.toString()); // 디버깅용 Log
            incomeAdapter.clear();
            incomeAdapter.addAll(newIncomeData);
            incomeAdapter.notifyDataSetChanged();

            // 지출 데이터 업데이트
            ArrayList<String> newExpenseData = dbHelper.getExpenseData();
            Log.d("YourActivity", "New Expense Data: " + newExpenseData.toString()); // 디버깅용 Log
            expenseAdapter.clear();
            expenseAdapter.addAll(newExpenseData);
            expenseAdapter.notifyDataSetChanged();
        }
    }
}

