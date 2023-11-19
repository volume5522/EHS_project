package com.example.sibal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;




public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Account.db";
    private static final int DATABASE_VERSION = 2;
    // 첫 번째 테이블 (수입)
    private static final String TABLE_INCOME = "income";
    private static final String COL_ID_INCOME = "_id";
    private static final String COL_DATE_INCOME = "date";
    private static final String COL_CARD_INCOME = "card";
    private static final String COL_CLASS_INCOME = "class";
    private static final String COL_AMOUNT_INCOME = "amount";


    // 두 번째 테이블 (지출)
    private static final String TABLE_EXPENSE = "expense";
    private static final String COL_ID_EXPENSE = "_id";
    private static final String COL_DATE_EXPENSE = "date";
    private static final String COL_CARD_EXPENSE = "card";
    private static final String COL_CLASS_EXPENSE = "class";
    private static final String COL_AMOUNT_EXPENSE = "amount";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String query = "CREATE TABLE " + TABLE_NAME
//                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COLUMN_DATE + " TEXT, "
//                + COLUMN_CARD + " TEXT, "
//                + COLUMN_CLASS + " TEXT, "
//                + COLUMN_AMOUNT + " TEXT, "
//                + COLUMN_CONTENT + " TEXT); ";
//        db.execSQL(query);
//    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 수입 테이블 생성 쿼리
        String createIncomeTableQuery = "CREATE TABLE " + TABLE_INCOME + " (" +
                COL_ID_INCOME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE_INCOME + " TEXT, " +
                COL_CARD_INCOME + " TEXT, " +
                COL_CLASS_INCOME + " TEXT, " +
                COL_AMOUNT_INCOME + " TEXT); ";

        db.execSQL(createIncomeTableQuery);

        // 지출 테이블 생성 쿼리
        String createExpenseTableQuery = "CREATE TABLE " + TABLE_EXPENSE + " (" +
                COL_ID_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE_EXPENSE + " TEXT, " +
                COL_CARD_EXPENSE + " TEXT, " +
                COL_CLASS_EXPENSE + " TEXT, " +
                COL_AMOUNT_EXPENSE + " TEXT); ";

        db.execSQL(createExpenseTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);
    }
    // 수입 데이터 추가 메서드
    public void addIncome(Date date, String card, String classification, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // 날짜를 "yyyy-MM-dd" 형식의 문자열로 변환하여 데이터베이스에 저장
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date); // 바뀐 부분
        cv.put(COL_DATE_INCOME, formattedDate);
        cv.put(COL_CARD_INCOME, card);
        cv.put(COL_CLASS_INCOME, classification);
        cv.put(COL_AMOUNT_INCOME, amount);

        try {
            // 데이터베이스에 데이터 추가
            long result = db.insert(TABLE_INCOME, null, cv);
            if (result == -1) {
                throw new Exception("수입 데이터 추가에 실패했습니다.");
            }
            Toast.makeText(context, "수입 데이터가 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("DBHelper", "데이터 추가 실패: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    // 지출 데이터 추가 메서드
    public void addExpense(Date date, String card, String classification, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date); // 바뀐 부분
        cv.put(COL_DATE_EXPENSE, formattedDate);
        cv.put(COL_CARD_EXPENSE, card);
        cv.put(COL_CLASS_EXPENSE, classification);
        cv.put(COL_AMOUNT_EXPENSE, amount);

        try {
            long result = db.insert(TABLE_EXPENSE, null, cv);
            if (result == -1) {
                throw new Exception("Failed to insert data into expense table.");
            }
            Toast.makeText(context, "Expense Data Added Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MyDatabaseHelper", "Failed to insert data: " + e.getMessage());

        } finally {
            db.close();
        }
    }

    // 수입 데이터 조회 메서드
    public ArrayList<String> getIncomeData() {
        ArrayList<String> incomeData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INCOME, null);

        while (cursor.moveToNext()) {
            String dateStr = cursor.getString(cursor.getColumnIndex(COL_DATE_INCOME));
            Date date = null;

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // date 객체를 사용할 수 있습니다.
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = outputDateFormat.format(date); // 바뀐 부분

            String card = cursor.getString(cursor.getColumnIndex(COL_CARD_INCOME));
            String classification = cursor.getString(cursor.getColumnIndex(COL_CLASS_INCOME));
            int amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT_INCOME));

            // 바뀐 부분: Date 형식을 formattedDate로 변경
            String data = String.format("%s, %s, %s, %s", formattedDate, card, classification, amount);
            incomeData.add(data);
        }

        cursor.close();
        db.close();

        return incomeData;
    }

    // 지출 데이터 조회 메서드
    public ArrayList<String> getExpenseData() {
        ArrayList<String> expenseData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE, null);

        while (cursor.moveToNext()) {
            String dateStr = cursor.getString(cursor.getColumnIndex(COL_DATE_EXPENSE));
            Date date = null;

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // date 객체를 사용할 수 있습니다.
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = outputDateFormat.format(date); // 바뀐 부분

            String card = cursor.getString(cursor.getColumnIndex(COL_CARD_EXPENSE));
            String classification = cursor.getString(cursor.getColumnIndex(COL_CLASS_EXPENSE));
            int amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT_EXPENSE));

            // 바뀐 부분: Date 형식을 formattedDate로 변경
            String data = String.format("%s, %s, %s, %s", formattedDate, card, classification, amount);
            expenseData.add(data);
        }

        cursor.close();
        db.close();

        return expenseData;
    }

    // 수입 데이터를 Cursor 형태로 가져오는 메서드 추가
    public Cursor getIncomeDataCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_INCOME, null);
    }

    // 지출 데이터를 Cursor 형태로 가져오는 메서드 추가
    public Cursor getExpenseDataCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSE, null);
    }

    // 대시보드 데이터 가져오기
    public ArrayList<String> getDashboardData() {
        ArrayList<String> dashboardData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM your_dashboard_table", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // 데이터베이스에서 필요한 컬럼의 값을 가져와서 리스트에 추가
                    String data = cursor.getString(cursor.getColumnIndex("your_column_name"));
                    dashboardData.add(data);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return dashboardData;
    }

    // 월별 지출 총액 계산 메서드
    public int calculateMonthlyExpense(String targetMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_AMOUNT_EXPENSE + " FROM " + TABLE_EXPENSE
                + " WHERE strftime('%Y-%m', " + COL_DATE_EXPENSE + ") = ?", new String[]{targetMonth});

        int totalExpense = 0;

        try {
            if (cursor.moveToFirst()) {
                do {
                    int amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT_EXPENSE));
                    totalExpense += amount;
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return totalExpense;
    }
    public int calculateDailyExpense(String targetDaily) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_AMOUNT_EXPENSE + " FROM " + TABLE_EXPENSE
                + " WHERE strftime('%Y-%m-%d', " + COL_DATE_EXPENSE + ") = ?", new String[]{targetDaily});

        int totalExpense = 0;

        try {
            if (cursor.moveToFirst()) {
                do {
                    int amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT_EXPENSE));
                    totalExpense += amount;
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return totalExpense;
    }

    public ArrayList<String> getExpenseDataByDate(String selectedDate) {
        ArrayList<String> expenseData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COL_DATE_EXPENSE + " = ?", new String[]{selectedDate});

        try {
            while (cursor.moveToNext()) {
                String dateStr = cursor.getString(cursor.getColumnIndex(COL_DATE_EXPENSE));
                Date date = null;

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    date = dateFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = outputDateFormat.format(date);

                String card = cursor.getString(cursor.getColumnIndex(COL_CARD_EXPENSE));
                String classification = cursor.getString(cursor.getColumnIndex(COL_CLASS_EXPENSE));
                int amount = cursor.getInt(cursor.getColumnIndex(COL_AMOUNT_EXPENSE));

                String data = String.format("%s, %s, %s, %s", formattedDate, card, classification, amount);
                expenseData.add(data);
            }
        } finally {
            cursor.close();
        }

        return expenseData;
    }

// 수정된 백분율 계산 메서드
    public HashMap<String, Float> getExpensePercentageByClassification() {
        HashMap<String, Float> percentageMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_CLASS_EXPENSE + ", SUM(" + COL_AMOUNT_EXPENSE + ") AS totalAmount " +
                "FROM " + TABLE_EXPENSE +
                " GROUP BY " + COL_CLASS_EXPENSE, null);

        try {
            int totalExpense = 0;

            while (cursor.moveToNext()) {
                String classification = cursor.getString(cursor.getColumnIndex(COL_CLASS_EXPENSE));
                int totalAmount = cursor.getInt(cursor.getColumnIndex("totalAmount"));
                totalExpense += totalAmount;
            }

            // 다시 cursor를 처음으로 이동
            cursor.moveToFirst();

            while (cursor.moveToNext()) {
                String classification = cursor.getString(cursor.getColumnIndex(COL_CLASS_EXPENSE));
                int totalAmount = cursor.getInt(cursor.getColumnIndex("totalAmount"));

                float percentage = ((float) totalAmount / totalExpense) * 100;
                percentageMap.put(classification, percentage);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return percentageMap;
    }
    public ArrayList<String> getExpenseClassifications() {
        ArrayList<String> classifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COL_CLASS_EXPENSE + " FROM " + TABLE_EXPENSE, null);

        try {
            while (cursor.moveToNext()) {
                String classification = cursor.getString(cursor.getColumnIndex(COL_CLASS_EXPENSE));
                classifications.add(classification);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return classifications;
    }
    // 특정 분류에 해당하는 총 지출액을 계산하는 메서드
    public int calculateTotalExpenseByClassification(String classification) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_AMOUNT_EXPENSE + ") FROM " + TABLE_EXPENSE +
                " WHERE " + COL_CLASS_EXPENSE + " = ?", new String[]{classification});

        int totalExpense = 0;

        try {
            if (cursor.moveToFirst()) {
                totalExpense = cursor.getInt(0);
            }
        } finally {
            cursor.close();
            db.close();
        }

        return totalExpense;
    }


}