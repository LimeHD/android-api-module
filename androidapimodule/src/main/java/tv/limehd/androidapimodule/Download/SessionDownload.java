package tv.limehd.androidapimodule.Download;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tv.limehd.androidapimodule.LimeApiClient;
import tv.limehd.androidapimodule.LimeCacheSettings;
import tv.limehd.androidapimodule.LimeCurlBuilder;
import tv.limehd.androidapimodule.LimeUri;
import tv.limehd.androidapimodule.Values.ApiValues;

import static tv.limehd.androidapimodule.LimeApiClient.convertMegaByteToByte;

public class SessionDownload {
    private ApiValues apiValues;
    private Context context;
    private File cacheDir;

    public SessionDownload() {
        initialization();
    }

    public SessionDownload(Context context, File cacheDir) {
        initialization();
        this.context = context;
        this.cacheDir = cacheDir;
    }

    private void connectCacheInOkHttpClient(OkHttpClient.Builder okHttpClientBuilder) {
        if(cacheDir!=null) {
            Cache cache = new Cache(cacheDir, convertMegaByteToByte(2));
            okHttpClientBuilder.cache(cache);
        }
    }

    private void initialization() {
        apiValues = new ApiValues();
    }

    public void sessionDownloadRequest(final String scheme, final String api_root, final String endpoint_session
            , final String application_id, final String x_access_token, final String x_test_ip, final boolean use_cache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LimeCurlBuilder.Builder limeCurlBuilder = new LimeCurlBuilder().setLogCurlInterface(new LimeCurlBuilder.LogCurlInterface() {
                    @Override
                    public void logCurl(String message) {
                        if (callBackSessionRequestInterface != null)
                            callBackSessionRequestInterface.callBackCurlRequest(message);
                    }
                });

                connectCacheInOkHttpClient(limeCurlBuilder);

                OkHttpClient client = new OkHttpClient(limeCurlBuilder);

                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add(apiValues.getAPP_ID_KEY(), application_id);
                FormBody formBody = formBodyBuilder.build();

                Request.Builder builder = new Request.Builder()
                        .addHeader(apiValues.getACCEPT_KEY(), apiValues.getACCEPT_VALUE())
                        .addHeader(apiValues.getX_ACCESS_TOKEN_KEY(), x_access_token)
                        .url(LimeUri.getUriSession(scheme, api_root, endpoint_session));
                if (x_test_ip != null)
                    builder.addHeader(apiValues.getX_TEXT_IP_KEY(), x_test_ip);

                if (use_cache) {
                    builder.cacheControl(new CacheControl.Builder().maxAge(tryGetMaxAge(), TimeUnit.SECONDS).build());
                } else {
                    builder.cacheControl(new CacheControl.Builder().noCache().build());
                }
                builder.post(formBody);
                Request request = builder.build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        if (callBackSessionInterface != null)
                            callBackSessionInterface.callBackError(e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            if (callBackSessionInterface != null) {
                                callBackSessionInterface.callBackError(("Unexpected code " + response));
                            }
                            throw new IOException("Unexpected code " + response);
                        }

                        if (response.networkResponse() != null) {
                            int maxAge = LimeApiClient.getMaxCacheFromCacheControl(response);
                            trySaveMaxAge(maxAge);
                        }

                        if (callBackSessionInterface != null)
                            callBackSessionInterface.callBackSuccess(response.body().string());
                    }
                });
            }
        }).start();
        if (callBackSessionRequestInterface != null)
            callBackSessionRequestInterface.callBackUrlRequest(LimeUri.getUriSession(scheme, api_root, endpoint_session));
    }

    private boolean trySaveMaxAge(int maxAge) {
        if (context != null) {
            LimeCacheSettings.setMaxAge(context, LimeCacheSettings.DOWNLOADER_SESSION, maxAge);
            return true;
        } else {
            return false;
        }
    }

    private int tryGetMaxAge() {
        if (context != null) {
            return LimeCacheSettings.getMaxAge(context, LimeCacheSettings.DOWNLOADER_SESSION);
        } else {
            return 0;
        }
    }

    public interface CallBackSessionInterface {
        void callBackSuccess(String response);

        void callBackError(String message);
    }

    public interface CallBackSessionRequestInterface {
        void callBackUrlRequest(String request);

        void callBackCurlRequest(String request);
    }

    private CallBackSessionInterface callBackSessionInterface;
    private CallBackSessionRequestInterface callBackSessionRequestInterface;

    public void setCallBackSessionInterface(CallBackSessionInterface callBackSessionInterface) {
        this.callBackSessionInterface = callBackSessionInterface;
    }

    public void setCallBackSessionRequestInterface(CallBackSessionRequestInterface callBackSessionRequestInterface) {
        this.callBackSessionRequestInterface = callBackSessionRequestInterface;
    }
}
