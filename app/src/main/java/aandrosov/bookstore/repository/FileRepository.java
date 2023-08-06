package aandrosov.bookstore.repository;

import android.os.Handler;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executor;

public class FileRepository {

    private final Executor executor;

    private final Handler handler;

    public FileRepository(Executor executor, Handler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    public void downloadFile(String fileUrl, String fileName, RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(fileUrl);
                InputStream inputStream = url.openStream();

                File file = new File(fileName);
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

                inputStream.close();
                handler.post(() -> callback.onComplete(new Result.Success(file.getPath())));
            } catch(Exception exception) {
                handler.post(() -> callback.onComplete(new Result.Error(exception)));
            }
        });
    }
}
