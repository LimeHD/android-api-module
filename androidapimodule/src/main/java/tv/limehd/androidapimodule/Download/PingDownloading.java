package tv.limehd.androidapimodule.Download;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tv.limehd.androidapimodule.LimeCurlBuilder;
import tv.limehd.androidapimodule.LimeUri;
import tv.limehd.androidapimodule.Values.ApiValues;

public class PingDownloading {
    private ApiValues apiValues;

    public PingDownloading() {
        apiValues = new ApiValues();
    }

    public void pingDownloadRequest(final String scheme, final String api_root, final String endpoint_ping
            , String application_id, final String x_access_token, final String x_test_ip,final boolean use_cache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LimeCurlBuilder.Builder limeCurlBuilder = new LimeCurlBuilder().setLogCurlInterface(new LimeCurlBuilder.LogCurlInterface() {
                    @Override
                    public void logCurl(String message) {
                        if (callBackPingRequestInterface != null)
                            callBackPingRequestInterface.callBackCurlRequest(message);
                    }
                });
                OkHttpClient client = new OkHttpClient(limeCurlBuilder);
                Request.Builder builder = new Request.Builder()
                        .url(LimeUri.getUriPing(scheme, api_root, endpoint_ping))
                        .addHeader(apiValues.getACCEPT_KEY(), apiValues.getACCEPT_VALUE())
                        .addHeader(apiValues.getX_ACCESS_TOKEN_KEY(), x_access_token);
                if (x_test_ip != null)
                    builder.addHeader(apiValues.getX_TEXT_IP_KEY(), x_test_ip);
                if (use_cache) {
                    builder.cacheControl(new CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build());
                } else {
                    builder.cacheControl(new CacheControl.Builder().noCache().build());
                }
                Request request = builder.build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        if (callBackPingInterface != null)
                            callBackPingInterface.callBackError(e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            if (callBackPingInterface != null)
                                callBackPingInterface.callBackError(("Unexpected code " + response));
                            throw new IOException("Unexpected code " + response);
                        }
                        if (callBackPingInterface != null)
                            callBackPingInterface.callBackSuccess(response.body().string());
                    }
                });
            }
        }).start();
        if (callBackPingRequestInterface != null)
            callBackPingRequestInterface.callBackUrlRequest(LimeUri.getUriPing(scheme, api_root, endpoint_ping));
    }

    public interface CallBackPingInterface {
        void callBackSuccess(String response);

        void callBackError(String message);
    }

    public interface CallBackPingRequestInterface {
        void callBackUrlRequest(String request);
        void callBackCurlRequest(String request);
    }

    private CallBackPingInterface callBackPingInterface;
    private CallBackPingRequestInterface callBackPingRequestInterface;

    public void setCallBackPingInterface(CallBackPingInterface callBackPingInterface) {
        this.callBackPingInterface = callBackPingInterface;
    }

    public void setCallBackPingRequestInterface(CallBackPingRequestInterface callBackPingRequestInterface) {
        this.callBackPingRequestInterface = callBackPingRequestInterface;
    }
}
