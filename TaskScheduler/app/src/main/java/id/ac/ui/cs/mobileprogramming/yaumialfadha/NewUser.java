package id.ac.ui.cs.id.mobileprogramming.yaumialfadha;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.text.SimpleDateFormat;


public class NewUser extends AppCompatActivity {

    DBHelper mydb;
    LinearLayout empty;
    NestedScrollView scrollView;
    EditText emptyName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.new_user);

        mydb = new DBHelper(this);
        empty = findViewById(R.id.empty);
        scrollView = findViewById(R.id.scrollView);
        emptyName = findViewById(R.id.emptyName);

    }
    public void saveBiodata(View v) {
        mydb.insertName(emptyName.getText().toString());
        finish();
    }

}
