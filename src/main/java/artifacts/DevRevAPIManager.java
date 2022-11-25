package artifacts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import okio.ByteString;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Interacts with DevRev REST APIs.
 */
public class DevRevAPIManager {

    private static final String PREPARE_ARTIFACTS_URL = "https://api.devrev.ai/artifacts.prepare";
    private static final String LOCATE_ARTIFACTS_URL = "https://api.devrev.ai/artifacts.locate";

    private final OkHttpClient httpClient;
    private final String devrevPAT;

    public DevRevAPIManager(String devrevPAT) {
        this.httpClient = new OkHttpClient();
        this.devrevPAT = devrevPAT;
    }

    // Prepares an artifact.
    public UploadInfo prepareArtifact() throws IOException  {
        // Create empty request body
        String requestParams = "{\"file_name\": \"test.png\"}";
        RequestBody body = RequestBody.create(
                ByteString.of(requestParams.getBytes()),
                MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(PREPARE_ARTIFACTS_URL)
                .method("POST", body)
                .addHeader("Authorization", devrevPAT)
                .build();
        Response response = httpClient.newCall(request).execute();
        String responseBody = Objects.requireNonNull(response.body()).string();
        JsonObject responseJson = new JsonParser().parse(responseBody).getAsJsonObject();
        String artifactId = responseJson.get("id").getAsString();
        String uploadURL = responseJson.get("url").getAsString();
        JsonArray formData = responseJson.get("form_data").getAsJsonArray();
        Map<String, String> formFields = new HashMap<>();
        for (JsonElement entry : formData) {
            JsonObject entryJson = entry.getAsJsonObject();
            formFields.put(
                    entryJson.get("key").getAsString(),
                    entryJson.get("value").getAsString());
        }
        return new UploadInfo(artifactId, uploadURL, formFields);
    }

    // upload the artifact using the @UploadInfo.
    public void uploadArtifact(UploadInfo uploadInfo, File file) {
        MultipartBody.Builder builder = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : uploadInfo.formData.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        builder.addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(uploadInfo.uploadURL)
                .method("POST", requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Locates the artifact, returns the download URL.
    public String locateArtifact(String artifactId) throws IOException {
        String requestParams = String.format("{\"id\": \"%s\"}", artifactId);
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(LOCATE_ARTIFACTS_URL)).newBuilder()
                .addQueryParameter("id", artifactId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", devrevPAT)
                .build();
        Response response = httpClient.newCall(request).execute();
        String responseBody = Objects.requireNonNull(response.body()).string();
        JsonObject responseJson = new JsonParser().parse(responseBody).getAsJsonObject();
        return responseJson.get("url").getAsString();
    }
}
