package com.bansarichothani.kotlindemo.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bansarichothani.kotlindemo.Example;
import com.bansarichothani.kotlindemo.R;

import java.util.ArrayList;
import java.util.List;

public class Mydata {
    private static Mydata instance;
    public List<String> SkiltaglList = new ArrayList<>();
    public String selectedSkill = "";
    public String selectedEducator = "";
    public String selectedCurricule = "";
    public String selectedStyle = "";
    public ArrayList<TextView> textViewList;
    public ArrayList<View> View;
    public ArrayList<String>  integers ;

    public static Mydata getInstance() {

        if (instance == null) {
            instance = new Mydata();
        }
        return instance;
    }

    public List<String> getSkiltaglList(List<Example> examples) {
        SkiltaglList = new ArrayList<>();
        for (Example example : examples) {
            System.out.println(example.getSkillTags().get(0));
            SkiltaglList.addAll(example.getSkillTags());
        }
        return SkiltaglList;
    }

    public List<String> getCurrSkiltaglList(List<Example> examples) {
        SkiltaglList = new ArrayList<>();

        for (Example example : examples) {
            System.out.println(example.getCurriculumTags().get(0));
            SkiltaglList.addAll(example.getCurriculumTags());
        }
        return SkiltaglList;
    }

    public List<String> getStyleSkiltaglList(List<Example> examples) {
        SkiltaglList = new ArrayList<>();

        for (Example example : examples) {
            System.out.println(example.getStyleTags().get(0));
            SkiltaglList.addAll(example.getStyleTags());
        }
        return SkiltaglList;
    }

    public List<String> getEducatorSkiltaglList(List<Example> examples) {
        SkiltaglList = new ArrayList<>();

        for (Example example : examples) {
            SkiltaglList.add(example.getEducator());
        }
        return SkiltaglList;
    }
    public ConstraintLayout GenrateMain_CustomTextView(Activity activity,boolean Gravity1,String s) {
        ConstraintLayout linearLayout = new ConstraintLayout(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(activity);
        textView.setText(s);
        textView.setPadding(25, 16, 0, 4);
        textView.setTextSize(14);
        textView.setGravity(Gravity1? Gravity.START :Gravity.END);
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(layoutParams);

        TextView textView1 = new TextView(activity);
        TextView textView1 = new TextView(activity);
        textView1.setText("View All >>");
        textView1.setPadding(25, 16, 0, 4);
        textView1.setTextSize(14);
        textView1.setGravity(Gravity.END);
        textView1.setTextColor(Color.WHITE);
        textView1.setLayoutParams(layoutParams);

        linearLayout.addView(textView);
        linearLayout.addView(textView1);
        return linearLayout;
    }
    public View Genrate_CustomTextView(Activity activity, String text, boolean b, int drawable , int color , int Bg) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout newViewLayout = new LinearLayout(activity);
        newViewLayout.setOrientation(LinearLayout.HORIZONTAL);
        newViewLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        int padding = (int) (5 * activity.getResources().getDisplayMetrics().density);
        newViewLayout.setPadding(padding,padding,padding,padding);
        TextView clearedFilterTextView = new TextView(activity);
        clearedFilterTextView.setLayoutParams(layoutParams);
        newViewLayout.setBackground(activity.getResources().getDrawable(Bg));
        clearedFilterTextView.setText(text);
        clearedFilterTextView.setPadding(16, 16, 16, 16);
        clearedFilterTextView.setTextSize(12);
        clearedFilterTextView.setSingleLine();
        clearedFilterTextView.setGravity(Gravity.CENTER);
        clearedFilterTextView.setTextColor(color);
        LinearLayout.LayoutParams clearedFilterLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        clearedFilterLayoutParams.setMargins(12, 0, 12, 0); // set margin between textviews
        clearedFilterTextView.setLayoutParams(clearedFilterLayoutParams);
        clearedFilterTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
        clearedFilterTextView.setCompoundDrawablePadding(8);

        newViewLayout.addView(clearedFilterTextView);
        textViewList.add(clearedFilterTextView);
            View.add(clearedFilterTextView);

        return newViewLayout;
    }
}
