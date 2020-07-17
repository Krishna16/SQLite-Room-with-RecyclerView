package com.example.roomdatabasetutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button_add, button_reset;
    private RecyclerView recyclerView;

    private List<MainData> dataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RoomDB roomDatabase;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        button_add = findViewById(R.id.btn_add);
        button_reset = findViewById(R.id.btn_reset);
        recyclerView = findViewById(R.id.recycler_view);

        roomDatabase = RoomDB.getInstance(this);
        dataList = roomDatabase.MainDao().getAll();

        linearLayoutManager = new LinearLayoutManager(this);
        databaseAdapter = new DatabaseAdapter(dataList, this, roomDatabase);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(databaseAdapter);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sText = editText.getText().toString().trim();

                if (!sText.equals("")){
                    MainData mainData = new MainData();
                    mainData.setText(sText);

                    roomDatabase.MainDao().insert(mainData);
                    editText.setText("");

                    //notify when data is inserted
                    dataList.clear();
                    dataList.addAll(roomDatabase.MainDao().getAll());
                    databaseAdapter.notifyDataSetChanged();
                }
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete all data from database
                roomDatabase.MainDao().reset(dataList);

                //notify when all data is deleted
                dataList.clear();
                dataList.addAll(roomDatabase.MainDao().getAll());
                databaseAdapter.notifyDataSetChanged();
            }
        });
    }
}