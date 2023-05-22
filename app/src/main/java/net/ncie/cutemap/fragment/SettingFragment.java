package net.ncie.cutemap.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import net.ncie.cutemap.R;
import net.ncie.cutemap.act.FeedbackAct;

public class SettingFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RelativeLayout share_layout;
    private RelativeLayout set_feedback;
    private RelativeLayout protocol;
    private RelativeLayout star;
    public SettingFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(rootView);
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){
        share_layout = rootView.findViewById(R.id.share_layout);
        set_feedback = rootView.findViewById(R.id.set_feedback);
        protocol = rootView.findViewById(R.id.protocol);
        star = rootView.findViewById(R.id.star);

    }

    public void initDate(){

    }

    public void initListener(){
        share_layout.setOnClickListener(this);
        set_feedback.setOnClickListener(this);
        star.setOnClickListener(this);
        protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_layout:
                allShare();
                break;
            case R.id.set_feedback:
                startActivity(new Intent(context, FeedbackAct.class));
                break;
            case R.id.star:
                openGooglePlayForAppReview(context.getPackageName());
                break;
            case R.id.protocol:
                break;

        }
    }

    /**
     * Android原生分享功能
     * 默认选取手机所有可以分享的APP
     */
    public void allShare(){

        // 构造分享意图
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

// 设置分享内容类型
        shareIntent.setType("text/plain");

// 设置分享文本
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using a great mapping app: CuteMap, which is completely free and targeted. The app download address: "+"https://play.google.com/store/apps/details?id=" + context.getPackageName());

// 允许用户选择如何分享,并支持返回结果
        shareIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);

// 启动分享意图
        startActivityForResult(Intent.createChooser(shareIntent, "Share"), 0);
    }

    // 接受回传的结果
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // 用户分享了文本

            } else {
                // 用户没有分享文本

            }
        }
    }

    public void openGooglePlayForAppReview(String appPackageName) {
        // 生成 Google Play 商店 URL
        String url = "https://play.google.com/store/apps/details?id=" + appPackageName;

        // 创建 Intent，跳转到 Google Play 商店
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // 添加 Intent 的 Flags，指定要查看当前应用的页面并打开评论部分
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("reviewId", appPackageName);

        // 启动 Intent
        startActivity(intent);
    }
}
