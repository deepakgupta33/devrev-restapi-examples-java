package artifacts;

import java.util.Map;

/**
 * Represents information required to upload a file to DevRev.
 */
public class UploadInfo {
    String artifactId;
    String uploadURL;
    Map<String, String> formData;

    public UploadInfo(
            String artifactId,
            String uploadURL,
            Map<String, String> formData) {
        this.artifactId = artifactId;
        this.uploadURL = uploadURL;
        this.formData = formData;
    }
}
