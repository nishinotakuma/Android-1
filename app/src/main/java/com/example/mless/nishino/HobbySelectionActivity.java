package com.example.mless.nishino;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;


public class HobbySelectionActivity extends AppCompatActivity {

    // 設定ボタンを押さない限り登録されない仕様

    private ArrayList<String> mSelectedHobby = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_selection);

        mSelectedHobby = getIntent().getStringArrayListExtra("hobby");

        // ツールバー周り
        Toolbar toolbar = findViewById(R.id.toolbar_hobby_selection);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ナビゲーションアイコンクリック時の処理
                setResult(1);
                finish();
            }
        });

        // recyclerview
        final RecyclerView recyclerView = findViewById(R.id.select_hobby_recycler);
        List<String> dataSet = getDataSet();
        HobbySelectionRecyclerViewAdapter adapter = new HobbySelectionRecyclerViewAdapter(dataSet);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        // メニューのインフレート、メニューアイテムのクリック処理
        toolbar.inflateMenu(R.menu.toolbar_menu_hobby_selection);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // メニューのクリック処理
                mSelectedHobby = new ArrayList<>();
                List<String> dataSet = getDataSet();
                HobbySelectionRecyclerViewAdapter adapter = new HobbySelectionRecyclerViewAdapter(dataSet);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });

        Button configButton = findViewById(R.id.config_button);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra( "hobby", mSelectedHobby);
                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            setResult(1);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private List<String> getDataSet(){
        List<String> dataSet = new ArrayList<>();
        dataSet.add("読書");
        dataSet.add("映画鑑賞");
        dataSet.add("スポーツ");
        dataSet.add("盆栽");
        dataSet.add("釣り");
        dataSet.add("プログラミング");
        dataSet.add("スノボー");
        dataSet.add("ボルダリング");
        dataSet.add("昼寝");
        dataSet.add("勉強");
        dataSet.add("散歩");
        dataSet.add("買い物");
        return dataSet;
    }




    // recyclerview周り
    public class HobbySelectionRecyclerViewAdapter extends RecyclerView.Adapter<HobbySelectionViewHolder>{

        private List<String> hobbyList;

        public HobbySelectionRecyclerViewAdapter(List<String> list) {
            hobbyList = list;
        }

        @NonNull
        @Override
        public HobbySelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.hobby_items, parent,false);
            HobbySelectionViewHolder vh = new HobbySelectionViewHolder(inflate);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final HobbySelectionViewHolder holder, int position) {
            holder.checkBox.setText(hobbyList.get(position));
            for(int i = 0; i < mSelectedHobby.size(); i++){
                if(holder.checkBox.getText().equals(mSelectedHobby.get(i))){
                    holder.checkBox.setChecked(true);
                }
            }
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.checkBox.isChecked()){
                        mSelectedHobby.add(holder.checkBox.getText().toString());
                    }else{
                        mSelectedHobby.remove(holder.checkBox.getText().toString());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return hobbyList.size();
        }
    }

    public class HobbySelectionViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;

        public HobbySelectionViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox_hobbyItems);
        }
    }
    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        Drawable mDivider;

        public DividerItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            drawVertical(c, parent);
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
