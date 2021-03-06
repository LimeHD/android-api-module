package tv.limehd.androidapimodule;

import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.Response;
import tv.limehd.androidapimodule.Download.Client.ClientDownloading;
import tv.limehd.androidapimodule.Download.Data.Component;
import tv.limehd.androidapimodule.Download.Data.DataForRequest;
import tv.limehd.androidapimodule.Values.ApiValues;

public class LimeApiClient {

    private String deviceId;
    private String api_root;
    private String application_id;
    private String scheme;
    private ApiValues apiValues;
    private String x_access_token;
    private String locale;
    private String x_test_ip;
    private boolean use_cache;
    private File cacheDir;
    private static final int defaultMaxAge = 600;
    private Context context;

    private Component.DataBasic dataBasic;
    private Component.DataCache dataCache;

    @Deprecated
    public LimeApiClient(Context context, String deviceId, String api_root, String scheme, String application_id, String x_access_token, String locale, File cacheDir, boolean use_cache) {
        initialization(context, deviceId,api_root, scheme, application_id, x_access_token, locale, cacheDir, use_cache);
    }

    @Deprecated
    public LimeApiClient(Context context, String deviceId, String api_root, String scheme, String application_id, String x_access_token, String locale, File cacheDir) {
        initialization(context, deviceId,api_root, scheme, application_id, x_access_token, locale, cacheDir, true);
    }

    @Deprecated
    public LimeApiClient(String api_root, String deviceId, String scheme, String application_id, String x_access_token, String locale){
        initialization(null, deviceId, api_root, scheme, application_id, x_access_token, locale, null, false);
    }

    public LimeApiClient(Component.DataCache dataCache, Component.DataBasic dataBasic) {
        initialization(dataCache, dataBasic);
    }

    private void initialization(Component.DataCache dataCache, Component.DataBasic dataBasic) {
        this.dataBasic = dataBasic;
        this.dataCache = dataCache;
    }

    @Deprecated
    private void initialization(Context context, String deviceId, String api_root, String scheme, String application_id, String x_access_token, String locale, File cacheDir, boolean use_cache) {
        apiValues = new ApiValues();
        this.api_root = api_root;
        this.scheme = scheme;
        this.application_id = application_id;
        this.x_access_token = x_access_token;
        this.locale = locale;
        x_test_ip = null;
        this.use_cache = use_cache;
        this.cacheDir = cacheDir;
        this.context = context;
        this.deviceId = deviceId;
    }

    @Deprecated
    public void updateLimeApiClientData(String api_root, String scheme, String application_id, String x_access_token, String locale) {
        this.api_root = api_root;
        this.scheme = scheme;
        this.application_id = application_id;
        this.x_access_token = x_access_token;
        this.locale = locale;
        x_test_ip = null;
    }

    @Deprecated
    public void updateLimeApiClientData(String api_root, String device_id, String scheme, String application_id, String x_access_token, String locale) {
        updateLimeApiClientData(api_root, scheme, application_id, x_access_token, locale);
        this.deviceId = device_id;
    }

    public void setUse_cache(boolean use_cache) {
        this.use_cache = use_cache;
    }

    public boolean getUse_cache() {
        return use_cache;
    }

    public void setXTestIp(String x_test_ip) {
        this.x_test_ip = x_test_ip;
    }

    public void updateApiRoot(String api_root) {
        this.api_root = api_root;
    }

    public void upDateLocale(String locale) {
        this.locale = locale;
    }

    /*Download channel List*/
    //region Download channel List

    @Deprecated
    public void downloadChannelList(String channel_group_id, String time_zone) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadChannelList();
            downloadChannelList(clientDownloading, channel_group_id, time_zone, use_cache);
        }
    }

    @Deprecated
    public void downloadChannelList(String channel_group_id, String time_zone, boolean use_cache) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadChannelList();
            downloadChannelList(clientDownloading, channel_group_id, time_zone, use_cache);
        }
    }

    public void downloadChannelList(@NonNull Component.DataChannelList dataChannelList) {
        if(dataBasic != null && dataBasic.getApiRoot() != null) {
            ClientDownloading clientDownloading = initializeDownloadChannelList();

            DataForRequest dataForRequest = new DataForRequest();
            dataForRequest.addComponent(dataBasic);
            dataForRequest.addComponent(dataCache);
            dataForRequest.addComponent(dataChannelList);

            clientDownloading.downloadChannelList(dataForRequest);
        }
    }

    private ClientDownloading initializeDownloadChannelList() {
        ClientDownloading clientDownloading = new ClientDownloading();
        clientDownloading.setCallBackDownloadInterface(new ClientDownloading.CallBackDownloadInterface() {
            @Override
            public void callBackDownloadedSuccess(String response) {
                if (downloadChannelListCallBack != null)
                    downloadChannelListCallBack.downloadChannelListSuccess(response);
            }

            @Override
            public void callBackDownloadedError(String error_message) {
                if (downloadChannelListCallBack != null)
                    downloadChannelListCallBack.downloadChannelListError(error_message);
            }
        });
        clientDownloading.setCallBackRequestInterface(new ClientDownloading.CallBackRequestInterface() {
            @Override
            public void callBackUrlRequest(String request) {
                if (requestChannelList != null)
                    requestChannelList.callBackUrlRequest(request);
            }

            @Override
            public void callBackCurlRequest(String request) {
                if (requestChannelList != null)
                    requestChannelList.callBackCurlRequest(request);
            }
        });
        return clientDownloading;
    }

    @Deprecated
    private void downloadChannelList(ClientDownloading clientDownloading, String channel_group_id, String time_zone, boolean use_cache) {
        clientDownloading.downloadChannelList(context, cacheDir, scheme, api_root, apiValues.getURL_CHANNELS_BY_GROUP(), application_id, x_access_token, channel_group_id, time_zone, locale, x_test_ip,  use_cache);
    }

    public interface DownloadChannelListCallBack {
        void downloadChannelListSuccess(String response);

        void downloadChannelListError(String message);
    }

    private DownloadChannelListCallBack downloadChannelListCallBack;

    public void setDownloadChannelListCallBack(DownloadChannelListCallBack downloadChannelListCallBack) {
        this.downloadChannelListCallBack = downloadChannelListCallBack;
    }
    //endregion

    /*Download broadcast*/
    //region DownloadBroadcast

    @Deprecated
    public void downloadBroadcast(String channel_id, String before_date, String after_date, String time_zone) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadBroadcast();
            downloadBroadcast(clientDownloading, channel_id, before_date, after_date, time_zone, use_cache);
        }
    }

    @Deprecated
    public void downloadBroadcast(String channel_id, String before_date, String after_date, String time_zone, boolean use_cache) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadBroadcast();
            downloadBroadcast(clientDownloading, channel_id, before_date, after_date, time_zone, use_cache);
        }
    }

    public void downloadBroadcast(@NonNull Component.DataBroadcast dataBroadcast) {
        if(dataBasic != null && dataBasic.getApiRoot() != null) {
            ClientDownloading clientDownloading = initializeDownloadBroadcast();

            DataForRequest dataForRequest = new DataForRequest();
            dataForRequest.addComponent(dataCache);
            dataForRequest.addComponent(dataBasic);
            dataForRequest.addComponent(dataBroadcast);

            clientDownloading.downloadBroadCast(dataForRequest);
        }
    }

    private ClientDownloading initializeDownloadBroadcast() {
        ClientDownloading clientDownloading = new ClientDownloading();
        clientDownloading.setCallBackDownloadInterfaceBroadcast(new ClientDownloading.CallBackDownloadInterfaceBroadcast() {
            @Override
            public void callBackDownloadedSuccess(String response, String channel_id) {
                if (downloadBroadCastCallBack != null)
                    downloadBroadCastCallBack.downloadBroadCastSuccess(response, channel_id);
            }

            @Override
            public void callBackDownloadedError(String error_message) {
                if (downloadBroadCastCallBack != null)
                    downloadBroadCastCallBack.downloadBroadCastError(error_message);
            }
        });
        clientDownloading.setCallBackRequestInterface(new ClientDownloading.CallBackRequestInterface() {
            @Override
            public void callBackUrlRequest(String request) {
                if (requestBroadCastCallBack != null)
                    requestBroadCastCallBack.callBackUrlRequest(request);
            }

            @Override
            public void callBackCurlRequest(String request) {
                if (requestBroadCastCallBack != null)
                    requestBroadCastCallBack.callBackCurlRequest(request);
            }
        });
        return clientDownloading;
    }

    @Deprecated
    private void downloadBroadcast(ClientDownloading clientDownloading, String channel_id, String before_date, String after_date, String time_zone, boolean use_cache) {
        clientDownloading.downloadBroadCast(context, scheme, api_root, apiValues.getURL_BROADCAST_PATH(), channel_id, before_date, after_date, time_zone, application_id
                , x_access_token, locale, x_test_ip, use_cache);
    }

    public interface DownloadBroadCastCallBack {
        void downloadBroadCastSuccess(String response, String channel_id);

        void downloadBroadCastError(String message);
    }

    private DownloadBroadCastCallBack downloadBroadCastCallBack;

    public void setDownloadBroadCastCallBack(DownloadBroadCastCallBack downloadBroadCastCallBack) {
        this.downloadBroadCastCallBack = downloadBroadCastCallBack;
    }
    //endregion

    //region Download ping

    @Deprecated
    public void downloadPing() {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadPing();
            downloadPing(clientDownloading, use_cache);
        }
    }

    @Deprecated
    public void downloadPing(boolean use_cache) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadPing();
            downloadPing(clientDownloading, use_cache);
        }
    }

    public void downloadPing(@NonNull Component.DataPing dataPing) {
        if(dataBasic != null && dataBasic.getApiRoot() != null) {
            ClientDownloading clientDownloading = initializeDownloadPing();

            DataForRequest dataForRequest = new DataForRequest();
            dataForRequest.addComponent(dataCache);
            dataForRequest.addComponent(dataBasic);
            dataForRequest.addComponent(dataPing);

            clientDownloading.downloadPing(dataForRequest);
        }
    }

    private ClientDownloading initializeDownloadPing() {
        ClientDownloading clientDownloading = new ClientDownloading();
        clientDownloading.setCallBackDownloadInterface(new ClientDownloading.CallBackDownloadInterface() {
            @Override
            public void callBackDownloadedSuccess(String response) {
                if (downloadPingCallBack != null)
                    downloadPingCallBack.downloadPingSuccess(response);
            }

            @Override
            public void callBackDownloadedError(String error_message) {
                if (downloadPingCallBack != null)
                    downloadPingCallBack.downloadPingError(error_message);
            }
        });
        clientDownloading.setCallBackRequestInterface(new ClientDownloading.CallBackRequestInterface() {
            @Override
            public void callBackUrlRequest(String request) {
                if (requestPingCallBack != null)
                    requestPingCallBack.callBackUrlRequest(request);
            }

            @Override
            public void callBackCurlRequest(String request) {
                if (requestPingCallBack != null)
                    requestPingCallBack.callBackCurlRequest(request);
            }
        });
        return clientDownloading;
    }

    @NonNull
    private void downloadPing(ClientDownloading clientDownloading, boolean use_cache) {
        clientDownloading.downloadPing(context, cacheDir, scheme, api_root, apiValues.getURL_PING_PATH(), application_id, x_access_token, x_test_ip, use_cache);
    }

    public interface DownloadPingCallBack {
        void downloadPingSuccess(String response);

        void downloadPingError(String message);
    }

    private DownloadPingCallBack downloadPingCallBack;

    public void setDownloadPingCallBack(DownloadPingCallBack downloadPingCallBack) {
        this.downloadPingCallBack = downloadPingCallBack;
    }
    //endregion

    //region Download session

    @Deprecated
    public void downloadSession() {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadSession();
            downloadSession(clientDownloading, use_cache);
        }
    }

    @Deprecated
    public void downloadSession(boolean use_cache) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadSession();
            downloadSession(clientDownloading, use_cache);
        }
    }

    private void downloadSession(@NonNull Component.DataSession dataSession) {
        if(dataBasic != null && dataBasic.getApiRoot() != null) {
            ClientDownloading clientDownloading = initializeDownloadSession();

            DataForRequest dataForRequest = new DataForRequest();
            dataForRequest.addComponent(dataBasic);
            dataForRequest.addComponent(dataCache);
            dataForRequest.addComponent(dataSession);

            clientDownloading.downloadSession(dataForRequest);
        }
    }

    private ClientDownloading initializeDownloadSession() {
        ClientDownloading clientDownloading = new ClientDownloading();
        clientDownloading.setCallBackDownloadInterface(new ClientDownloading.CallBackDownloadInterface() {
            @Override
            public void callBackDownloadedSuccess(String response) {
                if (downloadSessionCallBack != null)
                    downloadSessionCallBack.downloadSessionSuccess(response);
            }

            @Override
            public void callBackDownloadedError(String error_message) {
                if (downloadSessionCallBack != null)
                    downloadSessionCallBack.downloadSessionError(error_message);
            }
        });
        clientDownloading.setCallBackRequestInterface(new ClientDownloading.CallBackRequestInterface() {
            @Override
            public void callBackUrlRequest(String request) {
                if (requestSession != null)
                    requestSession.callBackUrlRequest(request);
            }

            @Override
            public void callBackCurlRequest(String request) {
                if (requestSession != null)
                    requestSession.callBackCurlRequest(request);
            }
        });
        return clientDownloading;
    }

    @Deprecated
    private void downloadSession(ClientDownloading clientDownloading, boolean use_cache) {
        clientDownloading.downloadSession(context, cacheDir, scheme, api_root, apiValues.getURL_SESSION_PATH(), application_id, x_access_token, x_test_ip,  use_cache);
    }

    public interface DownloadSessionCallBack {
        void downloadSessionSuccess(String response);

        void downloadSessionError(String message);
    }

    private DownloadSessionCallBack downloadSessionCallBack;

    public void setDownloadSessionCallBack(DownloadSessionCallBack downloadSessionCallBack) {
        this.downloadSessionCallBack = downloadSessionCallBack;
    }
    //endregion

    //region Download deepclicks

    @Deprecated
    public void downloadDeepClicks(String query, String path){
        if(api_root!=null){
            ClientDownloading clientDownloading = initializeSendingDeepClicks();
            downloadDeepClicks(clientDownloading, use_cache, query, path);
        }
    }

    @Deprecated
    public void downloadDeepClicks(String query, String path, boolean use_cache){
        if(api_root!=null){
            ClientDownloading clientDownloading = initializeSendingDeepClicks();
            downloadDeepClicks(clientDownloading, use_cache, query, path);
        }
    }

    private void downloadDeepClicks(@NonNull Component.DataDeepClick dataDeepClick) {
        if(dataBasic != null && dataBasic.getApiRoot() != null) {
            ClientDownloading clientDownloading = initializeSendingDeepClicks();

            DataForRequest dataForRequest = new DataForRequest();
            dataForRequest.addComponent(dataBasic);
            dataForRequest.addComponent(dataCache);
            dataForRequest.addComponent(dataDeepClick);

            clientDownloading.downloadDeepClicks(dataForRequest);
        }
    }

    private ClientDownloading initializeSendingDeepClicks(){
        ClientDownloading clientDownloading = new ClientDownloading();
        clientDownloading.setCallBackDownloadInterface(new ClientDownloading.CallBackDownloadInterface() {
            @Override
            public void callBackDownloadedSuccess(String response) {
                if (downloadDeepClicksCallBack != null)
                    downloadDeepClicksCallBack.sendingDeepClicksSuccess(response);
            }

            @Override
            public void callBackDownloadedError(String error_message) {
                if (downloadDeepClicksCallBack != null)
                    downloadDeepClicksCallBack.sendingDeepClicksError(error_message);
            }
        });
        clientDownloading.setCallBackRequestInterface(new ClientDownloading.CallBackRequestInterface() {
            @Override
            public void callBackUrlRequest(String request) {
                if (requestDeepClicks != null)
                    requestDeepClicks.callBackUrlRequest(request);
            }

            @Override
            public void callBackCurlRequest(String request) {
                if (requestDeepClicks != null)
                    requestDeepClicks.callBackCurlRequest(request);
            }
        });
        return clientDownloading;
    }

    @Deprecated
    private void downloadDeepClicks(ClientDownloading clientDownloading, boolean use_cache, String query, String path){
        clientDownloading.sendingDeepClicks(context, cacheDir, scheme, api_root, apiValues.getURL_DEEPCLICKS(), application_id, x_access_token, x_test_ip, use_cache, query, path, deviceId);
    }

    public interface DownloadDeepClicksCallBack {
        void sendingDeepClicksSuccess(String response);

        void sendingDeepClicksError(String message);
    }

    private DownloadDeepClicksCallBack downloadDeepClicksCallBack;

    public void setDownloadDeepClicksCallBack(DownloadDeepClicksCallBack downloadDeepClicksCallBack) {
        this.downloadDeepClicksCallBack = downloadDeepClicksCallBack;
    }

    //endregion

    //region RequestCallBack
    public interface RequestCallBack {
        void callBackUrlRequest(String request);

        void callBackCurlRequest(String request);
    }

    private RequestCallBack requestBroadCastCallBack;
    private RequestCallBack requestPingCallBack;
    private RequestCallBack requestChannelList;
    private RequestCallBack requestSession;
    private RequestCallBack requestDeepClicks;

    public void setRequestBroadCastCallBack(RequestCallBack requestBroadCastCallBack) {
        this.requestBroadCastCallBack = requestBroadCastCallBack;
    }

    public void setRequestPingCallBack(RequestCallBack requestPingCallBack) {
        this.requestPingCallBack = requestPingCallBack;
    }

    public void setRequestChannelList(RequestCallBack requestChannelList) {
        this.requestChannelList = requestChannelList;
    }

    public void setRequestSession(RequestCallBack requestSession) {
        this.requestSession = requestSession;
    }

    public void setRequestDeepClicks(RequestCallBack requestDeepClicks){
        this.requestDeepClicks = requestDeepClicks;
    }

    //endregion

    //region get version name and code api client
    public static int getVersionCode(Context context) {
        return 18;
    }

    public static String getVersionName(Context context) {
        return "0.2.30";
    }
    //endregion


    //region public static methods

    public static int getMaxCacheFromCacheControl(Response response) {
        try {
            String cacheControlValue = response.networkResponse().header("Cache-Control", "0");
            String[] cacheArray = cacheControlValue.split("=");
            return Integer.parseInt(cacheArray[cacheArray.length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultMaxAge;
    }

    public static long convertMegaByteToByte(int megaByte) {
        return megaByte * 1024 * 1024;
    }

    public static String getDeviceId(Context context) {
        try {
            return Settings.Secure
                    .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "device_id_null";
        }
    }
    //endregion
}
