package tv.limehd.androidapimodule;

import android.content.Context;

import tv.limehd.androidapimodule.Download.Client.ClientDownloading;
import tv.limehd.androidapimodule.Values.ApiValues;

public class LimeApiClient {

    private String api_root;
    private String application_id;
    private String scheme;
    private ApiValues apiValues;
    private String x_access_token;
    private String locale;
    private String x_test_ip;
    private boolean use_cache;

    public LimeApiClient(String api_root, String scheme, String application_id, String x_access_token, String locale, boolean use_cache){
        apiValues = new ApiValues();
        this.api_root = api_root;
        this.scheme = scheme;
        this.application_id = application_id;
        this.x_access_token = x_access_token;
        this.locale = locale;
        x_test_ip = null;
        this.use_cache = use_cache;
    }

    public LimeApiClient(String api_root, String scheme, String application_id, String x_access_token, String locale) {
        apiValues = new ApiValues();
        this.api_root = api_root;
        this.scheme = scheme;
        this.application_id = application_id;
        this.x_access_token = x_access_token;
        this.locale = locale;
        x_test_ip = null;
        use_cache = true;
    }

    public void updateLimeApiClientData(String api_root, String scheme, String application_id, String x_access_token, String locale) {
        this.api_root = api_root;
        this.scheme = scheme;
        this.application_id = application_id;
        this.x_access_token = x_access_token;
        this.locale = locale;
        x_test_ip = null;
    }

    public void setUse_cache(boolean use_cache){
        this.use_cache = use_cache;
    }

    public boolean getUse_cache(){
        return use_cache;
    }

    public void setXTestIp(String x_test_ip){
        this.x_test_ip = x_test_ip;
    }

    public void updateApiRoot(String api_root){
        this.api_root = api_root;
    }

    public void upDateLocale(String locale){
        this.locale = locale;
    }

    /*Download channel List*/
    //region Download channel List

    public void downloadChannelList(String channel_group_id) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadChannelList();
            downloadChannelList(clientDownloading, channel_group_id, use_cache);
        }
    }

    public void downloadChannelList(String channel_group_id, boolean use_cache){
        if(api_root!=null){
            ClientDownloading clientDownloading = initializeDownloadChannelList();
            downloadChannelList(clientDownloading, channel_group_id, use_cache);
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

    private void downloadChannelList(ClientDownloading clientDownloading, String channel_group_id, boolean use_cache) {
        clientDownloading.downloadChannelList(scheme, api_root, apiValues.getURL_CHANNELS_BY_GROUP(), application_id, x_access_token, channel_group_id, locale, x_test_ip, use_cache);
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

    public void downloadBroadcast(String channel_id, String before_date, String after_date, String time_zone) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadBroadcast();
            downloadBroadcast(clientDownloading, channel_id, before_date, after_date, time_zone, use_cache);
        }
    }

    public void downloadBroadcast(String channel_id, String before_date, String after_date, String time_zone, boolean use_cache) {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadBroadcast();
            downloadBroadcast(clientDownloading, channel_id, before_date, after_date, time_zone, use_cache);
        }
    }

    private ClientDownloading initializeDownloadBroadcast() {
        ClientDownloading clientDownloading = new ClientDownloading();
        clientDownloading.setCallBackDownloadInterface(new ClientDownloading.CallBackDownloadInterface() {
            @Override
            public void callBackDownloadedSuccess(String response) {
                if (downloadBroadCastCallBack != null)
                    downloadBroadCastCallBack.downloadBroadCastSuccess(response);
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

    private void downloadBroadcast(ClientDownloading clientDownloading, String channel_id, String before_date, String after_date, String time_zone, boolean use_cache) {
        clientDownloading.downloadBroadCast(scheme, api_root, apiValues.getURL_BROADCAST_PATH(), channel_id, before_date, after_date, time_zone, application_id
                , x_access_token, locale, x_test_ip, use_cache);
    }

    public interface DownloadBroadCastCallBack {
        void downloadBroadCastSuccess(String response);

        void downloadBroadCastError(String message);
    }

    private DownloadBroadCastCallBack downloadBroadCastCallBack;

    public void setDownloadBroadCastCallBack(DownloadBroadCastCallBack downloadBroadCastCallBack) {
        this.downloadBroadCastCallBack = downloadBroadCastCallBack;
    }
    //endregion

    //region Download ping

    public void downloadPing() {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadPing();
            downloadPing(clientDownloading, use_cache);
        }
    }

    public void downloadPing(boolean use_cache){
        if(api_root!=null){
            ClientDownloading clientDownloading = initializeDownloadPing();
            downloadPing(clientDownloading, use_cache);
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

    private void downloadPing(ClientDownloading clientDownloading, boolean use_cache) {
        clientDownloading.downloadPing(scheme, api_root, apiValues.getURL_PING_PATH(), application_id, x_access_token, x_test_ip, use_cache);
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

    public void downloadSession() {
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadSession();
            downloadSession(clientDownloading, use_cache);
        }
    }

    public void downloadSession(boolean use_cache){
        if (api_root != null) {
            ClientDownloading clientDownloading = initializeDownloadSession();
            downloadSession(clientDownloading, use_cache);
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

    private void downloadSession(ClientDownloading clientDownloading, boolean use_cache) {
        clientDownloading.downloadSession(scheme, api_root, apiValues.getURL_SESSION_PATH(), application_id, x_access_token, x_test_ip, use_cache);
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

    //region RequestCallBack
    public interface RequestCallBack {
        void callBackUrlRequest(String request);

        void callBackCurlRequest(String request);
    }

    private RequestCallBack requestBroadCastCallBack;
    private RequestCallBack requestPingCallBack;
    private RequestCallBack requestChannelList;
    private RequestCallBack requestSession;

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
    //endregion

    //get version name and code api client
    public static int getVersionCode(Context context) {
            return 12;
    }

    public static String getVersionName(Context context) {
        return "0.2.9";
    }
    //end region
}
