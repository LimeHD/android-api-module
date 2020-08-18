package tv.limehd.androidapimodule.Download;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tv.limehd.androidapimodule.Download.Data.Component;
import tv.limehd.androidapimodule.Download.Data.DataForRequest;
import tv.limehd.androidapimodule.Interfaces.ListenerRequest;
import tv.limehd.androidapimodule.LimeApiClient;
import tv.limehd.androidapimodule.LimeCurlBuilder;
import tv.limehd.androidapimodule.LimeUri;

public class ChannelListDownloading extends DownloadingBase {

    private Component.DataChannelList specificData;

    public ChannelListDownloading() {
        super();
    }

    @Override
    protected String getUriFromLimeUri() {
        return null;
    }

    public ChannelListDownloading(@NonNull Context context, @NonNull File cacheDir) {
        super(context, cacheDir);
    }

    public void loadingRequestChannelList(DataForRequest dataForRequest) {
        dataBasic = dataForRequest.getComponent(Component.DataBasic.class);
        specificData = dataForRequest.getComponent(Component.DataChannelList.class);

        LimeCurlBuilder.Builder limeCurlBuilder = createLimeCurlBuilder();
        tryConnectCacheInOkHttpClient(limeCurlBuilder);
        OkHttpClient client = createOkHttpClient(limeCurlBuilder);
        Request.Builder builder = createRequestBuilder(dataBasic.getxAccessToken());

        try {
            builder.url(LimeUri.getUriChannelList(
                    dataBasic.getScheme(),
                    dataBasic.getApiRoot(),
                    dataBasic.getEndpoint(),
                    specificData.getChannelGroupId(),
                    specificData.getTimeZone(),
                    specificData.getLocale()));
        } catch (Exception e) {
            e.printStackTrace();
            if (listenerRequest != null) {
                listenerRequest.onError(e.getMessage());
            }
            return;
        }

        if (dataBasic.getxTestIp() != null)
            builder.addHeader(apiValues.getX_TEXT_IP_KEY(), dataBasic.getxTestIp());
        if (dataBasic.isUseCache()) {
            builder.cacheControl(new CacheControl.Builder().maxAge(tryGetMaxAge(), TimeUnit.SECONDS).build());
        } else {
            builder.cacheControl(new CacheControl.Builder().noCache().build());
        }
        Request request = builder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (listenerRequest != null)
                    listenerRequest.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (listenerRequest != null)
                        listenerRequest.onError(("Unexpected code " + response));
                    throw new IOException("Unexpected code " + response);
                }

                if (isResponseFromNetwork(response)) {
                    int maxAge = LimeApiClient.getMaxCacheFromCacheControl(response);
                    trySaveMaxAge(maxAge);
                }

                if (listenerRequest != null)
                    listenerRequest.onSuccess(response.body().string());
            }
        });

        if (callBackUrlCurlRequestInterface != null)
            callBackUrlCurlRequestInterface.callBackUrlRequest(
                    LimeUri.getUriChannelList(
                            dataBasic.getScheme(),
                            dataBasic.getApiRoot(),
                            dataBasic.getEndpoint(),
                            specificData.getChannelGroupId(),
                            specificData.getTimeZone(),
                            specificData.getLocale()));
    }

    private ListenerRequest listenerRequest;

    public void setListenerRequest(ListenerRequest listenerRequest) {
        this.listenerRequest = listenerRequest;
    }
}
