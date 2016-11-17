package com.bobomee.android.recyclerviewhelperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bobomee.android.recyclerviewhelper.selectmode.ItemClickSupport;
import com.bobomee.android.recyclerviewhelper.selectmode.ItemSelectionSupport;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  protected TextView mTvTouch;
  private static final String TAG = "MainActivity";
  protected RecyclerView recyclerView;
  private BaseRecyclerAdapter mBaseRecyclerAdapter;
  List<String> datas = new ArrayList<>();
  private Toast mToast;
  private ItemClickSupport mItemClickSupport;
  private ItemSelectionSupport mItemSelectionSupport;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.activity_main);
    initData();
    initView();
  }

  private void initData() {
    for (int i = 0; i < 10; i++) {
      datas.add(i + "position");
    }
  }

  private void initView() {
    mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    mToast.setGravity(Gravity.CENTER, 0, 0);

    recyclerView = (RecyclerView) findViewById(R.id.view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(mBaseRecyclerAdapter = new BaseRecyclerAdapter<String>(datas, this) {
      @Override public void bindData(RecyclerViewHolder holder, int position, String item) {
        holder.setText(R.id.tvItemName, item);
      }

      @Override public int getItemLayoutId(int viewType) {
        return R.layout.item_adapter;
      }
    });

    mItemClickSupport = ItemClickSupport.from(recyclerView).add();

    mItemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
      @Override public void onItemClick(RecyclerView parent, View child, int position, long id) {
        mToast.setText("Item clicked: " + position);
        mToast.show();
        mBaseRecyclerAdapter.delete(position);
      }
    });

    mItemClickSupport.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
        mToast.setText("Item long pressed: " + position);
        mToast.show();
        mItemClickSupport.remove();
        mItemSelectionSupport.add();
        mItemSelectionSupport.setItemChecked(position, true);
        return true;
      }
    });

    ///////
    mItemSelectionSupport = ItemSelectionSupport.from(recyclerView)
        .setChoiceMode(ItemSelectionSupport.ChoiceMode.MULTIPLE);

    mItemSelectionSupport.setOnItemSelectListener(new ItemSelectionSupport.OnItemSelectListener() {
      @Override
      public void onItemSelect(RecyclerView parent, View view, int position, boolean checked) {
        mToast.setText("Item --> " + position + ", selected --> " + checked);
        mToast.show();
      }
    });

    mItemSelectionSupport.setonItemSelectChangeListener(
        new ItemSelectionSupport.OnItemSelectChangeListener() {
          @Override
          public void onItenSelectChange(ItemSelectionSupport.CheckedStates mCheckedStates) {

            if (mItemSelectionSupport.getCheckedItemCount() == 0) {
              mItemSelectionSupport.remove();
              mItemClickSupport.add();
            }
          }
        });
  }

  @Override public void onBackPressed() {
    if (mItemSelectionSupport.getCheckedItemCount() > 0) {
      mItemSelectionSupport.clearChoices();
      return;
    }
    super.onBackPressed();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.add, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add:{
        mBaseRecyclerAdapter.add(0,"new item");
      }
        break;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }
}
