package com.example.seonjae.with.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.gcm.GcmActivity;

public class DialogGcmActivity extends Activity {

    private String pName;
    private String title;
    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getIntent().getExtras();
        pName = bundle.getString("projectName");
        title = bundle.getString("title");
        message = bundle.getString("message");
        setContentView(R.layout.activity_dialog_gcm);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
//        builder.setPositiveButton("지금확인하기",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Action for 'Yes' Button
//                    }
//                });
        builder.setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
