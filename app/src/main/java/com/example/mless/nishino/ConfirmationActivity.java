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
        // actionbarだと、戻るボタンを自動でつけてくれるのでそちらのほうがいいと思います
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                    finish();
//                    break;
//            }
//            return super.onOptionsItemSelected(item);
//        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ナビゲーションアイコンクリック時の処理
                // resultCodeは定数にしてonActivityResultとここでそれを参照するようにしたほうがよいです
                setResult(1);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        // nullの考慮はしなくて大丈夫ですか？
        SerializableFormValues formValues = (SerializableFormValues) extras.getSerializable("formValues");

        TextView name = findViewById(R.id.name_text);
        TextView sex = findViewById(R.id.sex_text);
        TextView phone = findViewById(R.id.phone_text);
        TextView hobby = findViewById(R.id.hobby_text);
        TextView work = findViewById(R.id.work_text);

        // nullの考慮はしなくて大丈夫ですか？
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

        // strings.xmlに<string name="hoge">名前：%1$s　%2$s</string>みたいに定義して
        // activity側からgetSrting(R.strings.hoge, formValues.surName, formValues.firstName)
        // みたいにできます。基本的に文字列のハードコーディングはメンテ観点でNGです
        // refs: http://y-anz-m.blogspot.com/2011/03/android-xml.html
        name.setText("名前:　　　　" + formValues.surName + "　" + formValues.firstName);
        sex.setText("性別:　　　　" + formValues.sex);
        phone.setText("電話番号:　　　　" + formValues.phone);
        hobby.setText("趣味:　　　　" + hobby_sb);
        work.setText("職業:　　　　" + formValues.work);
    }
}
