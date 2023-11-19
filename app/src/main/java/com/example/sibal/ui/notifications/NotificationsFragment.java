package com.example.sibal.ui.notifications;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sibal.DBHelper;
import com.example.sibal.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsFragment extends Fragment {

    PieChart pieChart;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        pieChart = view.findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        dbHelper = new DBHelper(getContext()); // DBHelper 인스턴스 생성

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        // classification 데이터를 가져와서 Pie Chart에 표시
        ArrayList<PieEntry> yValues = new ArrayList<>();
        ArrayList<String> classifications = dbHelper.getExpenseClassifications(); // DBHelper에서 classification 데이터를 가져오는 메서드가 필요함

        for (int i = 0; i < classifications.size(); i++) {
            String classification = classifications.get(i);
            int expenseAmount = dbHelper.calculateTotalExpenseByClassification(classification); // DBHelper에서 해당 classification의 총 지출액을 계산하는 메서드가 필요함

            if (expenseAmount > 0) {
                yValues.add(new PieEntry((float) expenseAmount, classification));
            }
        }
        Description description = new Description();
        description.setText("Expense Classifications");
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues, "Expense Classifications");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }
}