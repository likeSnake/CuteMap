package net.ncie.cutemap.act;

import static net.ncie.cutemap.constant.Constant.AD_BEAN;
import static net.ncie.cutemap.constant.Constant.APP_GET_URL;
import static net.ncie.cutemap.util.HttpUtils.sendGetRequest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import net.ncie.cutemap.R;
import net.ncie.cutemap.bean.AdBean;
import net.ncie.cutemap.util.AesUtil;
import net.ncie.cutemap.util.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class StartAct extends AppCompatActivity implements View.OnClickListener{

    private ImageView enter;
    private ProgressBar progress;
    private AdBean adBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);
        initUI();
        initData();
        initListener();

        request_permissions();
    }

    public void initUI(){
        enter = findViewById(R.id.enter);
        progress = findViewById(R.id.progress);

    }
    public void initData(){
        sendGetRequest(APP_GET_URL, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(()-> {
                    if (result != null) {

                        Gson gson = new Gson();

                        adBean = gson.fromJson(AesUtil.decrypt(result), new TypeToken<AdBean>() {
                        }.getType());
                        System.out.println("成功" + adBean.toString());

                        System.out.println(adBean.getMy());
                        if (adBean.getAdIcon() != null && !adBean.getAdIcon().isEmpty()) {
                            enter.setImageResource(R.drawable.ic_enter2);
                        }

                        enter.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        enter.setOnClickListener(StartAct.this);

                    } else {
                        startActivity(new Intent(StartAct.this, MainAct.class));
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(String failure) {
                runOnUiThread(()-> {
                    Log.e("Http onFailure","请求链接失败");
                    //使用默认值

                    if (failure != null) {
                        Gson gson = new Gson();
                        adBean = gson.fromJson(AesUtil.decrypt(failure), new TypeToken<AdBean>() {
                        }.getType());
                        System.out.println("请求失败，使用默认值" + adBean.toString());


                        if (adBean.getAdIcon() != null && !adBean.getAdIcon().isEmpty()) {
                            enter.setImageResource(R.drawable.ic_enter2);
                        }

                        enter.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        enter.setOnClickListener(StartAct.this);

                    } else {
                        startActivity(new Intent(StartAct.this, MainAct.class));
                        finish();
                    }
                });
            }
        });
    }

    public void initListener(){


    }
    private void request_permissions() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        List<String> permissionList = new ArrayList<>();

        // 判断权限是否已经授予，没有就把该权限添加到列表中
        //精确定位
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        //粗略定位
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            System.out.println("权限-----"+permissions);
            ActivityCompat.requestPermissions(this, permissions, 1002);
        } else {

        }
    }

    // 请求权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                // 1002请求码对应的是申请多个权限
                if (grantResults.length > 0) {
                    List<String> list = new ArrayList<>();
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                            String permission = permissions[i];

                            list.add(permission);
                            // Toast.makeText(Start.this, permissions[i] + "权限被拒绝了,请手动打开权限", Toast.LENGTH_SHORT).show();
                            // getAppDetailSettingIntent(Start.this);
                        }
                    }
                    if (list.isEmpty()){

                    }else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                        System.out.println("用户选择始终拒绝不再弹出");
                        Toast.makeText(StartAct.this,"Be sure to grant location permissions, otherwise it won't work!",Toast.LENGTH_LONG).show();
                        getAppDetailSettingIntent(StartAct.this);
                    }else{
                        //Toast.makeText(StartActivity.this,"Please give permission to locate",Toast.LENGTH_SHORT).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle("Permission settings")
                                .setMessage("Be sure to grant location permissions, otherwise it won't work!")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        request_permissions();
                                    }
                                }).create();
                        alertDialog.show();
                        }
                }
        }
    }
    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter:
                if (adBean!=null) {
                    MMKV.defaultMMKV().encode(AD_BEAN,new Gson().toJson(adBean));
                }else {
                    Toast.makeText(this, "adBean=null", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, LoginAct.class);
                intent.putExtra(AD_BEAN,adBean);
                startActivity(intent);
                finish();
                break;
        }
    }
}