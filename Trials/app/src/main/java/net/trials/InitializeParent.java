package net.trials;


import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Avichal Rakesh on 4/17/2015.
 */
public class InitializeParent extends FragmentActivity {
    EditText stuName, stuClass, stuSec, stuNum, teaName, teaNum;
    ViewPager initializeView;

    CharSequence[] titles = {"Splash Screen", "Selector"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialize_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.outline_ic);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("DPS RKP", bm, 0xFF00810d);
            setTaskDescription(td);
        }

        initializeView = (ViewPager) findViewById(R.id.start_pager);
        initializeView.setPageTransformer(true, new ZoomOutPageTranformer());
        InitializePageAdapter mAdapter = new InitializePageAdapter(getSupportFragmentManager(), titles, 2);
        initializeView.setAdapter(mAdapter);
    }

    public void replaceStudent(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.replaceable_layout, new DetailsStudents(), "Student");
        ft.addToBackStack(null);
        ft.setCustomAnimations(R.anim.slide_in, 0);
        ft.commit();
    }

    public void replaceTeacher(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.replaceable_layout, new DetailsTeacher(), "Teacher");
        ft.addToBackStack(null);
        ft.setCustomAnimations(R.anim.slide_in, 0);
        ft.commit();
    }

    public void setFirstTimeDone(int e) {
        SharedPreferences.Editor editor = getSharedPreferences("APP_SETTINGS", MODE_PRIVATE).edit();
        editor.putBoolean("firstRun", false);
        editor.putBoolean("goRegi", true);
        if (e == 1) editor.putBoolean("detailsEntered", true);
        editor.commit();
    }

    public void studentSend(View v) {

        stuName = (EditText) findViewById(R.id.studentName);
        stuClass = (EditText) findViewById(R.id.studentClass);
        stuSec = (EditText) findViewById(R.id.studentSec);
        stuNum = (EditText) findViewById(R.id.studentNum);

        String name = stuName.getText().toString().trim();
        String Class = (stuClass.getText().toString().trim().length() > 0) ? stuClass.getText().toString().trim() : "0";
        String Sec = stuSec.getText().toString().trim().trim();
        String num = stuNum.getText().toString().trim();

        if (name.length() > 1) {
            if (Integer.parseInt(Class) <= 12 && Integer.parseInt(Class) >= 1) {
                if (Sec.length() > 0) {
                    if (num.length() == 6) {
                        SharedPreferences.Editor editor = getSharedPreferences("APP_SETTINGS", MODE_PRIVATE).edit();
                        editor.putString("Type", "Student");
                        editor.putString("Name", name);
                        editor.putString("Class", Class);
                        editor.putString("Sec", Sec);
                        editor.putString("Identifier", num);
                        editor.commit();
                        setFirstTimeDone(1);
                        Intent main = new Intent(this, MainActivity.class);
                        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main);
                    } else {
                        Toast.makeText(this, "Please check your Admission Number!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter the correct section!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid Class", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter the details!", Toast.LENGTH_SHORT).show();
        }
    }

    public void teacherSend(View v) {
        teaName = (EditText) findViewById(R.id.teacherName);
        teaNum = (EditText) findViewById(R.id.teacherNum);

        String name = teaName.getText().toString().trim();
        String num = teaNum.getText().toString().trim();

        if (name.length() > 1) {
            if (num.length() > 1) {
                SharedPreferences.Editor editor = getSharedPreferences("APP_SETTINGS", MODE_PRIVATE).edit();
                editor.putString("Type", "Teacher");
                editor.putString("Name", name);
                editor.putString("Identifier", num);
                editor.commit();
                setFirstTimeDone(1);
                Intent main = new Intent(this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
            } else {
                Toast.makeText(this, "Enter your Teacher Number!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter your details!", Toast.LENGTH_SHORT).show();
        }

    }

    public void skipReg(View v) {
        setFirstTimeDone(0);
        Intent main = new Intent(this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }

    @Override
    public void onBackPressed() {

        DetailsStudents Students = (DetailsStudents) getSupportFragmentManager().findFragmentByTag("Student");
        DetailsTeacher Teachers = (DetailsTeacher) getSupportFragmentManager().findFragmentByTag("Teacher");

        if (Students != null) {
            if (Students.isVisible())
                getSupportFragmentManager().beginTransaction().remove(Students).commit();
        } else if (Teachers != null) {
            if (Teachers.isVisible())
                getSupportFragmentManager().beginTransaction().remove(Teachers).commit();
        } else if (initializeView.getCurrentItem() != 0) {
            initializeView.setCurrentItem(initializeView.getCurrentItem() - 1);
        } else
            super.onBackPressed();

    }
}
