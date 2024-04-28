import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class CFHttpClient {
    private static OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static RecordInfo requestDNSList(String zone_id){
        Request req = new Request.Builder()
                .url(String.format(Constant.cf_recordsListUrl, zone_id))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + Constant.key)
                .build();

        Response resp = null;
        RecordInfo recordInfo;
        try {
            resp = client.newCall(req).execute();
            Gson gson = new Gson();
            recordInfo = gson.fromJson(resp.body().string(), RecordInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return recordInfo;
    }

    public static String requestExternalIP(){
        Request req = new Request.Builder()
                .url(Constant.ip_query)
                .build();
        Response resp = null;
        try {
            resp = client.newCall(req).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String result = null;
        try {
            result = resp.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.replaceAll("\\n", "").replaceAll("\\r", "").trim();
    }

    public static boolean requestPatchADNS(String zoneID, String id, String ip) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\"content\": \"%s\"}", ip));
        Request request = new Request.Builder()
                .url(String.format(Constant.cf_patchDNSUrl, zoneID, id))
                .patch(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + Constant.key)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
