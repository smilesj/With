package com.example.seonjae.with;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonjae.with.dummy.ListProjectAdapter;
import com.example.seonjae.with.dummy.MP_Project_Fragment;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TodoAddActivity extends AppCompatActivity {

    private TextView endDate;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Spinner spinner;
    private EditText workName;
    private EditText workDescription;
    private SeekBar priority;

    static final int DATE_DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        DataConn dataConn = new DataConn();
        ArrayList<String> projectNameList = new ArrayList<String>();
        projectNameList.addAll(dataConn.getProjectNameList());

        ArrayAdapter<String> adapter = new  ArrayAdapter<String> (this , android.R.layout.simple_spinner_item, projectNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setSelection(0);
        spinner.setAdapter (adapter);

        workName = (EditText)findViewById(R.id.workName);
        workDescription = (EditText)findViewById(R.id.workDescription);
        endDate = (TextView)findViewById(R.id.endDate);
        priority = (SeekBar)findViewById(R.id.sbBar);

        Button selectEnddate = (Button)findViewById(R.id.selectEnddate);
        selectEnddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();

        Button btnAddWork = (Button)findViewById(R.id.btnAddWork);
        btnAddWork.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String t_workID;
                  String t_projectID = spinner.getSelectedItem().toString();
                  String t_workName = workName.getText().toString();
                  String t_workDescription = workDescription.getText().toString();
                  int t_priority = priority.getProgress();
                  Date date = new Date();
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                  String t_startDate = sdf.format(date);
                  String t_endDate = String.valueOf(mYear)+String.valueOf(mMonth)+String.valueOf(mDay);
                  String t_resiEmail = "test@mail.com"; //수정하기

                  char ch1 = (char) ((Math.random() * 26) + 65);
                  char ch2 = (char) ((Math.random() * 26) + 65);
                  char ch3 = (char) ((Math.random() * 26) + 65);
                  char ch4 = (char) ((Math.random() * 26) + 65);
                  t_workID = String.valueOf(ch1) + String.valueOf(ch2) + String.valueOf(ch3) + String.valueOf(ch4) + t_startDate.substring(0, 8);

                  try{
                      URL url = new URL("http://with7.cloudapp.net/workAdd.php?workID=" + t_workID +"&projectID="+t_projectID
                              + "&workName=" + t_workName + "&workContents=" + t_workDescription + "&complete=0"
                              + "&priority=" + t_priority + "&startDay=" + t_startDate + "&endDay="+t_endDate+ "&resiEmail=" +t_resiEmail);
                      url.openStream();

                      Toast.makeText(TodoAddActivity.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                  }catch(IOException e){
                      e.printStackTrace();
                  }
              }
          }
        );
    }

    private void updateDisplay()
    {
        endDate.setText(new StringBuilder()
                .append(mYear).append("-")
                .append(mMonth + 1).append("-")
                .append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =  new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
}
