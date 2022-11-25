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
}
