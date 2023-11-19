package com.example.sibal;

import android.content.Context;
import android.widget.TextView;
public class MonthlyAmountCalculator {
    private DBHelper dbHelper;

    public MonthlyAmountCalculator(Context context) {
        dbHelper = new DBHelper(context);
    }

    // 월별 지출 총액 계산 메서드
    public int calculateMonthlyExpense(String targetMonth) {
        return dbHelper.calculateMonthlyExpense(targetMonth);
    }
    // 하루 지출 총액 계산 메서드
    public int calculateDailyExpense (String targetDaily) {
        return dbHelper.calculateDailyExpense(targetDaily);
    }
}

