package net.ncie.cutemap.util;

import android.util.Log;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static String  DEFAULT_DATA = "a5a6a2a27d21a1a7975faab2319b2934aed7c5ff050eb5888af165ad745e187b659b29f43a16c47e8898372db1d8eb391662806a1444db53a058088e520264eeb35ee4a4e27ecb13f0f8afca1a87073b64f86ab3152fa24f1f4d134b225b848d8f66ed0583e3d253fb5bb1348c354a114365bc494e3d9ce21ee772aa3e8fe4db07fc2adf0849d7357bb627bca9ed02520f3156bfebe21d262a043e38acd7bb72b65ab205626298757948484329410c67e225d098895fea60b6ef9724816c1ca0e51dd10ca4aaa285a222e1f4c937d09d3d224abcae8b2d62b3b86ea44d9371641d725d5279f15a267670ff8ea926cf45037bf1980c86fa3d66a8ff9d20762f08782d9a9c4aff1dfb6bfef5488c4de943239ff50d559c2458484c87047931a5aadee0f83933d8b08b5549e4ec225698eff44b46bf87af1dfb583383c08c64456bcde126bdc53bb687e2facc9768fde4811187cd58d7c33205d1a91222041de4f8675057d7c02629878478d2c22c7cafe9f88f1b371af1f135ff8cefdf1c60a4370c759d3fce9128de1f27a2f78e81c25b6fae619cfa7d5c2fda5e9d4311004c7dc26680c4bc827cffda557d8dff20b5a2d12f68e215f8f98015413d9c74f5a8a47ccd2e61ecdfc6adb4e1107d038e4c568e3dbf571a290c1da1013c02a06c5dc0d28087b377e2f39bdad146ee910688428febaf2fb0a7bd0604d886fb2dcf243b6f4444d85a43135f1d8790d15f12c8f36f002645d2d8397d88b4aab0465943e7de978c06686a54a4a3d920e4d287e0c2294e4750fc10ac0f538444a503416cd3de272c152448deb1345a596195bdd61b0c946dcf31fccf266dd67d94ffbe29f569163e82e5ab979a1cb743095ad840330614f5bf6f941f379d1cfab74bbcdf76e7ce788b80e49a6178253f3a36a74b726bbf3e2c81878661e66ad2d47658978a5d97b71e261a1fb60d6eab62b1f430d763ce863b03080376ca3cabcf93666090c56baea8c58be9f622b6dd8185f6b3399bb7898329eb8a9ba78ef6ed1910f0c5b745a7dcf2110a0f4bd89e37de0a4e415875fafc6d952456eeffdf9467dca3f6fdef72fe33da486bfabf2b812caad25696409320facb85df86b7bc52f57c5ab4332f3ee4fc23992f95954ca55f1c39253da752eada6bf8567fe53f175da63cecc5d5671395dceebf1c2291c5cb5e6a704cfd1ab06c910e5cd59c9dffbbaca21f9e577b7e190cdb1adce581de5dad7a9ffedc94aa312f1d2558d37dfe0cc0bcffec0437167c123297dcfc54d7d4277ed22d4149a9dd1c07e3faac7e52a7e49cea60c0ae612f63fd392e4146f19e8a8ac72e4fc27c387057b5b16358a0185e0f206ca4b592a9e4059c0c0cebc5f6c94d4900775b85c3d0461ff3165512d569fa9c2e69b836d0f03db61ea20583c7fc2292d98d6feeb979947b41112b78f3699ba0f06338064902a4fd5067135b175185d3d5f166b1514021575caafea11c6d4cc18a3067fa7206d5de01e417fc4820b545";

    private static final String TAG = "MyHttpUtils";

    public static void sendGetRequest(String urlString, final HttpCallback<String> callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(DEFAULT_DATA);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    try {

                        String responseString = response.body().string();
                        Log.d(TAG, "Response: " + responseString);

                        //测试阶段，请求成功后使用默认值
                        callback.onSuccess(DEFAULT_DATA);


                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure(DEFAULT_DATA);
                    }
                }
            }
        });

    }

    public static void sendPostRequest(String urlString, String requestBody, final HttpCallback<String> callback) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(requestBody, mediaType);
        Request request = new Request.Builder()
                .url(urlString)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure("Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "Response: " + responseString);
                        callback.onSuccess(responseString);
                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure("JSON parse failed: " + e.getMessage());
                    }
                }
            }
        });
    }

    public interface HttpCallback<T> {
        void onSuccess(T result);
        void onFailure(T failure);
    }
}
