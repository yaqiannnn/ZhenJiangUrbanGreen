package com.nju.urbangreen.zhenjiangurbangreen.ugo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.search.SearchActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UgoListActivity extends BaseActivity {

    @BindView(R.id.add_ugo_title_bar)
    TitleBarLayout addUgoTitleBarLayout;
    @BindView(R.id.recycler_ugo_list)
    RecyclerView recyclerUgoList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    private List<GreenObject> ugObjectList = new ArrayList<>();
    private UgoListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ugo_list);
        ButterKnife.bind(this);
        
        addUgoTitleBarLayout.setTitleText("养护对象列表");
        addUgoTitleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addUgoTitleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_add);
        addUgoTitleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UgoListActivity.this, SearchUgoActivity.class);
//                startActivity(intent);
                startActivityForResult(intent,1);
            }
        });
//        initUgos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addUgoTitleBarLayout.recoverReceiver != null) {
            unregisterReceiver(addUgoTitleBarLayout.recoverReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String returnData = data.getStringExtra("selectUgos");
                    Toast.makeText(this, returnData, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //刷新养护对象数据
    private void refreshUgos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        initUgos();
                        Toast.makeText(UgoListActivity.this, "刷新数据（还没做）", Toast.LENGTH_SHORT).show();
//                        ugObjectList.add(new GreenObject("00000001", "00000003", "古树名木", "新数据", "镇江市", "镇江市", 15, "null"));
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initUgos() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载列表");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] errMsg = new String[1];
                try {
                    ugObjectList = WebServiceUtils.getUGOInfoExceptST(errMsg);
                    Log.d("test",ugObjectList+"");
                }catch (Exception e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView();
                    }
                });
            }
        }).start();

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUgoList.setLayoutManager(linearLayoutManager);
        adapter = new UgoListAdapter(ugObjectList);
        recyclerUgoList.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(UgoListActivity.this);
                    builder.setMessage("你确定要删除这一项吗?");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.notifyItemRemoved(position);
                            ugObjectList.remove(position);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //这2句是什么意思..
                            adapter.notifyItemRemoved(position + 1);
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                        }
                    }).show();*/
                    adapter.notifyItemRemoved(position);
//                    ugObjectList.remove(position);
                    Snackbar.make(viewHolder.itemView,"数据已删除",Snackbar.LENGTH_SHORT)
                            .setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(UgoListActivity.this, "数据已恢复", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            }
        };

        //列表项侧滑
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerUgoList);

        //列表项分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerUgoList.getContext(),
                linearLayoutManager.getOrientation());
        recyclerUgoList.addItemDecoration(dividerItemDecoration);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUgos();
            }
        });
    }
}
