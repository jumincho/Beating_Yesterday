package com.jumincho.beatingyesterday.ui.productivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jumincho.beatingyesterday.R;
import com.jumincho.beatingyesterday.data.NoteDatabase;

public class TodoActivity extends AppCompatActivity {
    private static final String TAG = "TodoActivity";

    Fragment todoListFragment;
    EditText inputToDo;

    public static NoteDatabase noteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        todoListFragment = new TodoListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, todoListFragment).commit();

        Button saveButton = findViewById(R.id.Savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDo();
                Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        openDatabase();
    }

    private void saveToDo() {
        inputToDo = findViewById(R.id.inputTodo);
        String todo = inputToDo.getText().toString();

        ContentValues values = new ContentValues();
        values.put("TODO", todo);

        NoteDatabase database = NoteDatabase.getInstance(getApplicationContext());
        database.insert(NoteDatabase.TABLE_NOTE, null, values);

        inputToDo.setText("");
    }

    public void openDatabase() {
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }
}
