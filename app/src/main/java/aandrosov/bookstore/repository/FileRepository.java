package aandrosov.bookstore.repository;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executor;

public class FileRepository {

    private final Executor executor;

    public FileRepository(Executor executor) {
        this.executor = executor;
    }

    public void downloadFile(String fileUrl, RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(fileUrl);
                InputStream inputStream = url.openStream();

                File file = File.createTempFile("temp", null);
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

                inputStream.close();
                callback.onComplete(new Result.Success(file));
            } catch(Exception exception) {
                callback.onComplete(new Result.Error(exception));
            }
        });
    }
}
