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
    private boolean isCheckedAgreement = false;
//    private int selectedSexId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TODO: 回転対応
        // refs: https://qiita.com/kobakei/items/5f38339dd6528fdc6b5d

        // savedInstanceStateについて
        // https://qiita.com/yyyske/items/c6e342a9008bebef75bd
        if (savedInstanceState != null) {
            mHobby = savedInstanceState.getStringArrayList("mHobby");
            isCheckedAgreement = savedInstanceState.getBoolean("isCheckedAgreement");
//            selectedSexId = savedInstanceState.getInt("selectedSexId");
        }

        // 性別
        // refs: http://www.adakoda.com/android/000067.html
//        RadioGroup radioGroup = findViewById(R.id.sex_radio);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                selectedSexId = checkedId;
//            }
//        });

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

        // checkboxは内部的にsavedinstanceを実装していたような気がするので、viewからフラグを常に取得するようにすれば
        // このフラグを管理する必要がなくなるかなと思います。(違ったらすいません)
        // onCreateでviewのインスタンスを取得→フィールドに退避させて、チェックされてるかのフラグが欲しいときは
        // そこからisChecked()で取得するってイメージ
        if (isCheckedAgreement) {
            // enable/disableの状態によって背景をかえるのではなくて、selectorを使ってください。（後述）
            doneButton.setBackgroundColor(Color.parseColor("#FFFF8800"));
            doneButton.setEnabled(true);
        } else {
            doneButton.setBackgroundColor(Color.parseColor("#FFAAAAAA"));
            doneButton.setEnabled(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // クリックするたびにenable/disableを切り替えたいなら
                // doneButton.setEnable(checkBox.isChecked());
                // でいいのでは？

                //　あとcheckboxはCompoundButtonを継承していて、OnCheckedChangeListener#onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                //　が使えるので以下のような形が良いかと思います
                // checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                //            @Override
                //            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //                doneButton.setEnabled(checkBox.isChecked());
                //            }
                //        });

                //　enable/disableの状態によってボタンの背景とか変えたいならselectorリソースを定義してそれをbuttonのbackgroundに指定すれば
                //　コードでsetBackgroundしなくてもAndroidライブラリが自動で切り替えてくれます
                // refs: https://developer.android.com/guide/topics/resources/color-list-resource
                // https://inon29.hateblo.jp/entry/2014/01/13/184153
                if (checkBox.isChecked()) {
                    isCheckedAgreement = true;
                    doneButton.setBackgroundColor(Color.parseColor("#FFFF8800"));
                    doneButton.setEnabled(true);
                } else {
                    isCheckedAgreement = false;
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
                            // dismissがないです
                            // if (dialog != null) {
                            // dialog.dismiss();
                            // }
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

        // AppCompatActivity#RESULT_OKもチェックしてください
        if (resultCode == 0) {
            //こういうkeyとかはベタがきしないで定数で定義したほうが良いと思います
            mHobby = data.getStringArrayListExtra("hobby");
            setHobbyView(mHobby);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mHobby", mHobby);
        outState.putBoolean("isCheckedAgreement", isCheckedAgreement);
//        outState.putInt("selectedSexId", selectedSexId);
    }


    private void setHobbyView(List<String> hobby) {

        // 空の場合は早期リターンすればネストが一個無くなります
        // if (hobby.size() == 0) {
        //    return;
        // }
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

