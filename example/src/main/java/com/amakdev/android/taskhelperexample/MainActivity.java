package com.amakdev.android.taskhelperexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.amakdev.android.taskhelper.LocalTaskExecutor;
import com.amakdev.android.taskhelper.TaskListener;
import com.amakdev.android.taskhelper.TaskRunnable;

public class MainActivity extends FragmentActivity implements TaskListener, View.OnClickListener {

    private static final String TAG_EXAMPLE_TASK = "TAG_EXAMPLE_TASK";

    private LocalTaskExecutor taskExecutor;
    private ProgressDialog progressDialog;

    private EditText secondsToRun;
    private CheckBox shouldFail;

    private class Request {

        int seconds;
        boolean shouldFail;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskExecutor = LocalTaskExecutor.getForActivity(this);
        findViewById(R.id.btn_run).setOnClickListener(this);
        secondsToRun = (EditText) findViewById(R.id.seconds_to_run);
        shouldFail = (CheckBox) findViewById(R.id.chk_should_fail);
        taskExecutor.registerListener(this);
    }

    @Override
    public void onClick(View v) {
        Request request = new Request();
        try {
            request.seconds = Integer.parseInt(secondsToRun.getText().toString().trim());
        } catch (Exception e) {
            request.seconds = 10;
        }
        request.shouldFail = shouldFail.isChecked();
        taskExecutor.executeTask(TAG_EXAMPLE_TASK, request, new TaskRunnable<Request, Integer>() {
            @Override
            public Integer execute(Request request) throws Exception {
                Thread.sleep(request.seconds * 1000L);
                if (request.shouldFail) {
                    throw new Exception("Failed for " + request.seconds + " seconds");
                }
                return request.seconds;
            }
        });
    }

    @Override
    public void onTaskProgress(String componentTag, String taskTag, boolean inProgress) {
        if (inProgress) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("You can rotate device");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            progressDialog.setCancelable(false);
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onTaskCompleted(String componentTag, String taskTag, Object resultObject) {
        Integer result = (Integer) resultObject;
        Toast.makeText(this, "Completed for " + result + " seconds", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskFailed(String componentTag, String taskTag, Exception ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskExecutor.unregisterListener();
    }

}
