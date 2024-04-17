package artifacts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

class DefaultArtifactsTest {

    @Test
    public void testArtifact() throws IOException  {
        String testPAT = System.getenv("DEVREV_PAT");

        File testFile = new File("/home/deepakgupta/test.html");
        ArtifactsManager artifactsManager = new DefaultArtifactsManager(testPAT);

        // Upload file
        String artifactID = artifactsManager.createArtifact(testFile);
        Assertions.assertNotNull(artifactID);

        // Download file
        String downloadURL = artifactsManager.locateArtifact(artifactID);
        byte[] downloadedContent = new DownloadManager().DownloadFile(downloadURL);

        // match uploaded and downloaded content
        byte[] localFileContent = Files.readAllBytes(testFile.toPath());
        if (!Arrays.equals(downloadedContent, localFileContent)) {
            Assertions.fail("Files didn't match");
        }
    }

    @Test
    public void testArtifactCreationFailure() throws IOException {
        String testPAT = System.getenv("DEVREV_PAT");
        File invalidFile = new File("/path/to/nonexistent/file.html");
        ArtifactsManager artifactsManager = new DefaultArtifactsManager(testPAT);
        Assertions.assertThrows(IOException.class, () -> artifactsManager.createArtifact(invalidFile), "Expected createArtifact to throw, but it didn't");
    }

    @Test
    public void testLocateArtifactFailure() throws IOException {
        String testPAT = System.getenv("DEVREV_PAT");
        ArtifactsManager artifactsManager = new DefaultArtifactsManager(testPAT);
        String invalidArtifactId = "nonexistent-id";
        Assertions.assertThrows(IOException.class, () -> artifactsManager.locateArtifact(invalidArtifactId), "Expected locateArtifact to throw, but it didn't");
    }
}
