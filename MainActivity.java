package com.example.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText editTextMemo;
    private Button buttonSave;
    private Button buttonDelete;
    private Button buttonEdit;
    private TextView textViewMemo;
    private Spinner spinnerCategory;

    private List<String> memoList;
    private Map<String, List<String>> memoMap;
    private StringBuilder memoStringBuilder;
    private String selectedMemo;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMemo = findViewById(R.id.editTextMemo);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonEdit = findViewById(R.id.buttonEdit);
        textViewMemo = findViewById(R.id.textViewMemo);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        memoList = new ArrayList<>();
        memoMap = new HashMap<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMemo();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMemo();
            }
        });

        textViewMemo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedMemo = textViewMemo.getText().toString();
                editTextMemo.setText(selectedMemo);
                editTextMemo.requestFocus();
                return true;
            }
        });
    }

    private void saveMemo() {
        String memoText = editTextMemo.getText().toString().trim();
        selectedCategory = spinnerCategory.getSelectedItem().toString();

        if (!memoText.isEmpty()) {
            memoList.add(memoText);

            if (memoMap.containsKey(selectedCategory)) {
                List<String> memos = memoMap.get(selectedCategory);
                memos.add(memoText);
            } else {
                List<String> memos = new ArrayList<>();
                memos.add(memoText);
                memoMap.put(selectedCategory, memos);
            }

            updateMemoTextView();
            editTextMemo.getText().clear();
        }
    }

    private void deleteMemo() {
        if (selectedMemo != null && !selectedMemo.isEmpty()) {
            if (memoList.contains(selectedMemo)) {
                memoList.remove(selectedMemo);
                memoMap.get(selectedCategory).remove(selectedMemo);
                selectedMemo = null;
                editTextMemo.getText().clear();
                updateMemoTextView();
            }
        } else {
            Toast.makeText(this, "메모를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editMemo() {
        if (selectedMemo != null && !selectedMemo.isEmpty()) {
            String updatedMemo = editTextMemo.getText().toString().trim();

            if (!updatedMemo.isEmpty()) {
                if (memoList.contains(selectedMemo)) {
                    memoList.set(memoList.indexOf(selectedMemo), updatedMemo);
                    memoMap.get(selectedCategory).set(
                            memoMap.get(selectedCategory).indexOf(selectedMemo), updatedMemo);
                    selectedMemo = null;
                    editTextMemo.getText().clear();
                    updateMemoTextView();
                }
            } else {
                Toast.makeText(this, "수정할 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "메모를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMemoTextView() {
        memoStringBuilder = new StringBuilder();

        if (memoMap.isEmpty()) {
            memoStringBuilder.append("메모가 없습니다.");
        } else {
            for (Map.Entry<String, List<String>> entry : memoMap.entrySet()) {
                String category = entry.getKey();
                List<String> memos = entry.getValue();

                memoStringBuilder.append("[").append(category).append("]").append("\n");

                for (String memo : memos) {
                    memoStringBuilder.append("- ").append(memo).append("\n");
                }
            }
        }

        textViewMemo.setText(memoStringBuilder.toString());
    }
}

