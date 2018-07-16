package com.example.mless.nishino;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mHobby = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 回転対応
        // refs: https://qiita.com/kobakei/items/5f38339dd6528fdc6b5d


        //趣味周り
        setHobbyView(mHobby);
        Button select_hobby_button = findViewById(R.id.select_hobby_button);
        select_hobby_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HobbySelectionActivity.class);
                intent.putStringArrayListExtra("hobby", mHobby);
                startActivityForResult(intent, 0);
            }
        });

        // 職業(spinner)
        // refs: http://y-anz-m.blogspot.com/2014/04/androidspinnerxml.html
        // res/string.xml


        // 利用規約
        final TextView rule_text = findViewById(R.id.rule_text);
        final Pattern rule_pattern = Pattern.compile("利用規約");
        final String url_string = "https://google.com";
        final Linkify.MatchFilter matchFilter = new Linkify.MatchFilter() {
            @Override
            public boolean acceptMatch(CharSequence charSequence, int i, int i1) {
                return true;
            }
        };
        final Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String s) {
                return "";
            }
        };
        Linkify.addLinks(rule_text, rule_pattern, url_string, matchFilter, transformFilter);

        // ボタンの無・有効化
        final Button doneButton = findViewById(R.id.done_button);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    doneButton.setBackgroundColor(Color.parseColor("#FFFF8800"));
                    doneButton.setEnabled(true);
                }else{
                    doneButton.setBackgroundColor(Color.parseColor("#FFAAAAAA"));
                    doneButton.setEnabled(false);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPressDoneButton();
            }
        });
    }

    private void onPressDoneButton() {

        // フォームの全ての入力値を取得
        SerializableFormValues formValues = getValuesFromForm();
        // 氏名が無い場合
        if(formValues.surName.equals("") || formValues.firstName.equals("")){
            // AlertDialog
            // refs: https://qiita.com/suzukihr/items/8973527ebb8bb35f6bb8
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("氏名が入力されていません")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }else{
            // ConfirmationActivityにデータを送る
            Intent confirmationIntent = new Intent(MainActivity.this, ConfirmationActivity.class);
            confirmationIntent.putExtra("formValues", formValues);

            startActivity(confirmationIntent);

        }

    }

    private SerializableFormValues getValuesFromForm() {

        RadioGroup radioGroup = findViewById(R.id.sex_radio);
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        String sex;
        if(radioButtonId == -1){
            // 何もチェックされていないとき
            sex = "";
        }else{
            sex = ((RadioButton)findViewById(radioButtonId)).getText().toString();
        }

        final String surName = ((TextView)findViewById(R.id.surname_input)).getText().toString();
        final String firstName = ((TextView)findViewById(R.id.firstname_input)).getText().toString();
        final String phone = ((TextView)findViewById(R.id.phone_input)).getText().toString();
        final List<String> hobby = mHobby;
        final String work = ((Spinner)findViewById(R.id.work_spinner)).getSelectedItem().toString();

        SerializableFormValues formValues = new SerializableFormValues();
        formValues.surName = surName;
        formValues.firstName = firstName;
        formValues.sex = sex;
        formValues.phone = phone;
        formValues.hobby = hobby;
        formValues.work = work;
        return formValues;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            mHobby = data.getStringArrayListExtra("hobby");
            setHobbyView(mHobby);
        }
    }


    private void setHobbyView(List<String> hobby){
        TextView hobbyText = findViewById(R.id.hobby_view);
        // StringBuilder
        // refs: https://www.sejuku.net/blog/14982
        StringBuilder tmp = new StringBuilder();
        tmp.append("");
        // アプリ起動時はlength==0のオブジェクトを渡す
        if(hobby.size() != 0){
            for(int i = 0; i < hobby.size(); i++){
                tmp.append(hobby.get(i));
                if(i !=  hobby.size()-1){
                    tmp.append("、");
                }
            }
        }
        hobbyText.setText(tmp);
    }
}

