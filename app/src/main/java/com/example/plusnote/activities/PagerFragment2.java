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
    final DateTimeFormatter noteDayF = DateTimeFormatter.ofPattern("yyyy_MM_dd");
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
//        View mainView = inflater.inflate(R.layout.activity_main, container, false);
//        TextView[] week_ds = new TextView[7];
//        week_ds[0] = mainView.findViewById(R.id.weekDay);
//        week_ds[1] = mainView.findViewById(R.id.weekDay1);
//        week_ds[2] = mainView.findViewById(R.id.weekDay2);
//        week_ds[3] = mainView.findViewById(R.id.weekDay3);
//        week_ds[4] = mainView.findViewById(R.id.weekDay4);
//        week_ds[5] = mainView.findViewById(R.id.weekDay5);
//        week_ds[6] = mainView.findViewById(R.id.weekDay6);
        String[] dates = new String[7];
        TextView[] dvs = new TextView[7];
        dvs[0] = result.findViewById(R.id.Day);
        dvs[1] = result.findViewById(R.id.Day1);
        dvs[2] = result.findViewById(R.id.Day2);
        dvs[3] = result.findViewById(R.id.Day3);
        dvs[4] = result.findViewById(R.id.Day4);
        dvs[5] = result.findViewById(R.id.Day5);
        dvs[6] = result.findViewById(R.id.Day6);
        if (MainActivity.pnc != pageNumber) {
            for (int i = 0; i < dvs.length; i++) {
                dvs[i].setTextColor(Color.parseColor("#9000FF00"));
            }
        }
        int i = 1;
        if (pageNumber % 2 == 1) {
            i = 2 + (pageNumber - 1) * 7;
            ld = ld.plusDays(i);
            days = dtf.format(ld);
            for (int s = 0; s < 7; s++) {
                dates[weekDayToNum(ld.getDayOfWeek())] = noteDayF.format(ld);
                dvs[weekDayToNum(ld.getDayOfWeek())].setText(days);
                int finalS = s;
                dvs[s].setOnClickListener(view -> {
                    for (TextView dv : dvs) {
                        dv.setTextColor(Color.parseColor("#9000FF00"));
//                        week_ds[z].setTextColor(Color.parseColor("#9000FF00"));
                    }
                    MainActivity.pnc = pageNumber;
                    MainActivity.pageNumberForDay = -1;
                    dvs[finalS].setTextColor(Color.parseColor("#FFA500"));
//                    week_ds[finalS].setTextColor(Color.parseColor("#FFA500"));
                    MainActivity.ClickOnDay(dvs[finalS], dates[finalS], getContext());
                });
                ld = ld.plusDays(1);
                days = dtf.format(ld);
            }
        }
        if (pageNumber % 2 == 0) {
            i = 2 + (pageNumber - 1) * 7;
            ld = ld.plusDays(i);
            days = dtf.format(ld);
            for (int s = 0; s < 7; s++) {
                dates[weekDayToNum(ld.getDayOfWeek())] = noteDayF.format(ld);
                dvs[weekDayToNum(ld.getDayOfWeek())].setText(days);
                int finalS = s;
                dvs[s].setOnClickListener(view -> {
                    for (TextView dv : dvs) {
                        dv.setTextColor(Color.parseColor("#9000FF00"));
//                        week_ds[z].setTextColor(Color.parseColor("#9000FF00"));
                    }
                    MainActivity.pnc = pageNumber;
                    MainActivity.pageNumberForDay = -1;
                    dvs[finalS].setTextColor(Color.parseColor("#FFA500"));
//                    week_ds[finalS].setTextColor(Color.parseColor("#FFA500"));
                    MainActivity.ClickOnDay(dvs[finalS], dates[finalS], getContext());
                });
                ld = ld.plusDays(1);
                days = dtf.format(ld);
            }
        }
        if (pageNumber == MainActivity.pageNumberForDay) {
            final DateTimeFormatter twoDayNum = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
            String check = twoDayNum.format(MainActivity.stLdate);
            if (check.contentEquals(dvs[0].getText())) {
                dvs[0].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.contentEquals(dvs[1].getText())) {
                dvs[1].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.contentEquals(dvs[2].getText())) {
                dvs[2].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.contentEquals(dvs[3].getText())) {
                dvs[3].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.contentEquals(dvs[4].getText())) {
                dvs[4].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.contentEquals(dvs[5].getText())) {
                dvs[5].setTextColor(Color.parseColor("#FFA500"));
            }
            if (check.contentEquals(dvs[6].getText())) {
                dvs[6].setTextColor(Color.parseColor("#FFA500"));
            }
        }
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

