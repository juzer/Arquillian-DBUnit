package arq.sample.testing;

import java.io.File;

/**
 * Utility class for locating artifacts in local Maven repository.
 * 
 * @see http://community.jboss.org/wiki/HowdoIaddJARfilestothetestarchive
 */
public class ArtifactResolver {
    /**
     * Local Maven repository path.
     */
    private static final String LOCAL_MAVEN_REPO =
	    System.getProperty("maven.repo.local") != null ?
		    System.getProperty("maven.repo.local") :
		    (System.getProperty("user.home") + File.separatorChar +
			    ".m2" + File.separatorChar + "repository");

    public static File resolve(String groupId, String artifactId, String version) {
	return new File(LOCAL_MAVEN_REPO + File.separatorChar +
		groupId.replace(".", File.separator) + File.separatorChar +
		artifactId + File.separatorChar +
		version + File.separatorChar +
		artifactId + "-" + version + ".jar");
    }

    public static File resolve(String qualifiedArtifactId) {
	String[] segments = qualifiedArtifactId.split(":");
	return resolve(segments[0], segments[1], segments[2]);
    }
}
