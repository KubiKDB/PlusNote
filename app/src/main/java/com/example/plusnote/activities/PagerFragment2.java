package com.example.plusnote.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.plusnote.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PagerFragment2 extends Fragment {
    public LocalDate ld = LocalDate.of(2022, Month.JANUARY, 1);
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy EEEE", Locale.ENGLISH);
    public String days = dtf.format(ld);

    private static int pageNumber;

    public static PagerFragment2 newInstance(int page) {
        PagerFragment2 fragment2 = new PagerFragment2();
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment2.setArguments(args);
        return fragment2;
    }

    public PagerFragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_page_2, container, false);

        TextView dayView = result.findViewById(R.id.Day);
        TextView dayView1 = result.findViewById(R.id.Day1);
        TextView dayView2 = result.findViewById(R.id.Day2);
        TextView dayView3 = result.findViewById(R.id.Day3);
        TextView dayView4 = result.findViewById(R.id.Day4);
        TextView dayView5 = result.findViewById(R.id.Day5);
        TextView dayView6 = result.findViewById(R.id.Day6);
        TextView[] dvs = new TextView[7];
        dvs[0] = dayView;
        dvs[1] = dayView1;
        dvs[2] = dayView2;
        dvs[3] = dayView3;
        dvs[4] = dayView4;
        dvs[5] = dayView5;
        dvs[6] = dayView6;
        int i = 1;
        if (pageNumber % 2 == 1) {
            i = 2 + (pageNumber - 1) * 7;
            ld = ld.plusDays(i);
            days = dtf.format(ld);
            for (int s = 0; s < 7; s++) {
                dvs[weekDayToNum(ld.getDayOfWeek())].setText(days);
                ld = ld.plusDays(1);
                days = dtf.format(ld);
            }
        }
        if (pageNumber % 2 == 0) {
            i = 2 + (pageNumber - 1) * 7;
            ld = ld.plusDays(i);
            days = dtf.format(ld);
            for (int s = 0; s < 7; s++) {
                dvs[weekDayToNum(ld.getDayOfWeek())].setText(days);
                ld = ld.plusDays(1);
                days = dtf.format(ld);
            }
        }
        if (pageNumber == MainActivity.pageNumberForDay){
            final DateTimeFormatter twoDayNum = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
            String check = twoDayNum.format(MainActivity.stLdate);
            if (check.equals(dvs[0].getText())) {
                dvs[0].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.equals(dvs[1].getText())) {
                dvs[1].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.equals(dvs[2].getText())) {
                dvs[2].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.equals(dvs[3].getText())) {
                dvs[3].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.equals(dvs[4].getText())) {
                dvs[4].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.equals(dvs[5].getText())) {
                dvs[5].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.equals(dvs[6].getText())) {
                dvs[6].setTextColor(Color.parseColor("#FFA500"));
            }
        }
//        if (pageNumber == 0) {
//            dayView5.setText("0"+i);
//            i++;
//            dayView6.setText("0"+i);
//        } else if (pageNumber%2 == 1){
//            i = 3 + (pageNumber-1)*7;
//            TextView[] dViews = new TextView[7];
//            dViews[0] = dayView;
//            dViews[1] = dayView1;
//            dViews[2] = dayView2;
//            dViews[3] = dayView3;
//            dViews[4] = dayView4;
//            dViews[5] = dayView5;
//            dViews[6] = dayView6;
//            for (int j = 0; j < 7; j++) {
//                if (i < 10) {
//                    dViews[j].setText("0" + i);
//                    i++;
//                } else {
//                    dViews[j].setText(i + "");
//                    i++;
//                    if (i > 31) break;
//                }
//            }
//        } else if (pageNumber%2 == 0){
//            i = 3 + (pageNumber-1)*7;
//            TextView[] dViews = new TextView[7];
//            dViews[0] = dayView;
//            dViews[1] = dayView1;
//            dViews[2] = dayView2;
//            dViews[3] = dayView3;
//            dViews[4] = dayView4;
//            dViews[5] = dayView5;
//            dViews[6] = dayView6;
//            for (int j = 0; j < 7; j++) {
//                    dViews[j].setText(i + "");
//                    i++;
//            }
//        }
        return result;
    }
    public int weekDayToNum(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return 0;
            case TUESDAY:
                return 1;
            case WEDNESDAY:
                return 2;
            case THURSDAY:
                return 3;
            case FRIDAY:
                return 4;
            case SATURDAY:
                return 5;
            case SUNDAY:
                return 6;
            default:
                return 0;
        }
    }
}

