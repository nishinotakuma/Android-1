package com.example.mless.nishino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // ツールバー周り
        Toolbar toolbar = findViewById(R.id.toolbar_confirmation);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ナビゲーションアイコンクリック時の処理
                setResult(1);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        SerializableFormValues formValues = (SerializableFormValues) extras.getSerializable("formValues");

        TextView name = findViewById(R.id.name_text);
        TextView sex = findViewById(R.id.sex_text);
        TextView phone = findViewById(R.id.phone_text);
        TextView hobby = findViewById(R.id.hobby_text);
        TextView work = findViewById(R.id.work_text);

        List<String> hobbies = formValues.hobby;
        StringBuilder hobby_sb = new StringBuilder();
        hobby_sb.append("");
        if(hobbies.size() != 0){
            for(int i = 0; i < hobbies.size(); i++){
                hobby_sb.append(hobbies.get(i));
                if(i != hobbies.size()-1){
                    hobby_sb.append("、");
                }
            }
        }

        name.setText("名前:　　　　" + formValues.surName + "　" + formValues.firstName);
        sex.setText("性別:　　　　" + formValues.sex);
        phone.setText("電話番号:　　　　" + formValues.phone);
        hobby.setText("趣味:　　　　" + hobby_sb);
        work.setText("職業:　　　　" + formValues.work);
    }
}
