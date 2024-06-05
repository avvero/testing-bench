package pw.avvero.spring.sandbox

import java.nio.file.Files

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

class FileUtils {

    static void createTempDirs(String reportDirectory, List<String> tempDirs) {
        for (String tempDir : tempDirs) {
            File tempDirectory = new File(reportDirectory + tempDir)
            if (!tempDirectory.exists() && !tempDirectory.mkdirs()) {
                throw new UnsupportedOperationException()
            }
            tempDirectory.setReadable(true, false)
            tempDirectory.setWritable(true, false)
            tempDirectory.setExecutable(true, false)
        }
    }

    static void copyFiles(String srcDir, String destDir) {
        def src = new File(srcDir)
        def dest = new File(destDir)
        src.eachFile { file ->
            def destFile = new File(dest, file.name)
            if (file.isDirectory()) {
                copyFiles(file.path, destFile.path)
            } else {
                Files.copy(file.toPath(), destFile.toPath(), REPLACE_EXISTING)
            }
        }
    }
}
