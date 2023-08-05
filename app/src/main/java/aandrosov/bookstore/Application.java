package aandrosov.bookstore;

import aandrosov.api.books.BooksApi;
import aandrosov.bookstore.repository.BooksRepository;
import aandrosov.bookstore.repository.FileRepository;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application extends android.app.Application {

    public static final byte THREADS = 4;
    protected final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

    protected final BooksApi api = new BooksApi();
    protected final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    protected final BooksRepository booksRepository = new BooksRepository(api, executorService, mainThreadHandler);

    protected final FileRepository fileRepository = new FileRepository(executorService);

}
