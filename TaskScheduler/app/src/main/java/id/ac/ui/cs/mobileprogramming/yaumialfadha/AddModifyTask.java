package id.ac.ui.cs.id.mobileprogramming.yaumialfadha;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddModifyTask extends AppCompatActivity {

    Calendar calendar;
    DBHelper mydb;

    Boolean isModify = false;
    String task_id;
    TextView toolbar_title;
    EditText edit_text;
    EditText edit_text_1;
    TextView dateText;
    Button save_btn;
    TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_modify_task);

        mydb = new DBHelper(getApplicationContext());
        calendar = new GregorianCalendar();
        toolbar_title = findViewById(R.id.toolbar_title);
        edit_text = findViewById(R.id.edit_text);
        edit_text_1 = findViewById(R.id.edit_text_1);
        dateText = findViewById(R.id.dateText);
        save_btn = findViewById(R.id.save_btn);
        timeText = findViewById(R.id.timeText);

        dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));



        Intent intent = getIntent();
        if (intent.hasExtra("isModify")) {
            isModify = intent.getBooleanExtra("isModify", false);
            task_id = intent.getStringExtra("id");
            init_modify();
        }


    }

    public void init_modify() {
        toolbar_title.setText("Modify Task");
        save_btn.setText("Update");
        LinearLayout deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setVisibility(View.VISIBLE);
        Cursor task = mydb.getSingleTask(task_id);
        if (task != null) {
            task.moveToFirst();


            edit_text.setText(task.getString(1));
            edit_text_1.setText(task.getString(4));
            timeText.setText(task.getString(5));

            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                calendar.setTime(iso8601Format.parse(task.getString(2)));
            } catch (ParseException e) {
            }

            dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));


        }

    }


    public void saveTask(View v) {

        /*Checking for Empty Task*/
        if (edit_text.getText().toString().trim().length() > 0) {
            String message = "you have successfully added";
            if (isModify) {
                mydb.updateTask(task_id, edit_text.getText().toString(), new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()), edit_text_1.getText().toString(), timeText.getText().toString());
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        AddModifyTask.this
                )
                        .setSmallIcon(R.drawable.clock)
                        .setContentTitle("New Notification")
                        .setContentText(message)
                        .setAutoCancel(true);
                Intent intent = new Intent(AddModifyTask.this,
                        NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);

                PendingIntent pendingIntent = PendingIntent.getActivity(AddModifyTask.this,
                        0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );
                notificationManager.notify(0, builder.build());
            } else {
                mydb.insertTask(edit_text.getText().toString(), new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()), edit_text_1.getText().toString(), timeText.getText().toString());
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }


            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Empty task can't be saved.", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteTask(View v) {
        mydb.deleteTask(task_id);
        Toast.makeText(getApplicationContext(), "Task Removed", Toast.LENGTH_SHORT).show();
        finish();
    }


    public void chooseDate(View view) {
        final View dialogView = View.inflate(this, R.layout.date_picker, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Choose Date");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));

            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void chooseTime(View view) {
        final View dialogView = View.inflate(this, R.layout.time_picker, null);
        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker);



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Choose Date");
        builder.setNegativeButton("Cancel", null);

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                final int hour = timePicker.getHour();
                int minute = timePicker.getMinute();


                timeText.setText(String.valueOf(hour) + ":"+String.valueOf(minute) );

            }
        });
        builder.show();


    }
}
