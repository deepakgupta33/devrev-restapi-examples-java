package artifacts;

import java.io.File;
import java.io.IOException;

public class DefaultArtifactsManager implements ArtifactsManager {

    private final DevRevAPIManager devRevAPIManager;

    public DefaultArtifactsManager(String devrevPAT) {
        this.devRevAPIManager = new DevRevAPIManager(devrevPAT);
    }

    @Override
    public String createArtifact(File file) throws IOException {
        UploadInfo info = devRevAPIManager.prepareArtifact();
        devRevAPIManager.uploadArtifact(info, file);
        return info.artifactId;
    }

    @Override
    public String locateArtifact(String artifactId) throws IOException {
        return devRevAPIManager.locateArtifact(artifactId);
    }
}
