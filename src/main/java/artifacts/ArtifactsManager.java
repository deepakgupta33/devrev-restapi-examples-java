package artifacts;

import java.io.File;
import java.io.IOException;

/**
 * Manage DevRev Artifacts.
 */
public interface ArtifactsManager {
    /**
     * Creates the artifact on DevRev.
     * */
    String createArtifact(File file) throws IOException;

    /**
     * Creates a download URL to download the given artifact.
     */
    String locateArtifact(String artifactId) throws IOException;
}
