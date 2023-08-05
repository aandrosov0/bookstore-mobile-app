package aandrosov.bookstore.repository;

import aandrosov.api.books.Book;
import aandrosov.api.books.BooksApi;
import android.os.Handler;

import java.util.concurrent.Executor;

public class BooksRepository {

    private final BooksApi api;
    private final Executor executor;
    private final Handler handler;

    public BooksRepository(BooksApi api, Executor executor, Handler handler) {
        this.api = api;
        this.executor = executor;
        this.handler = handler;
    }

    public void search(String query, int limit, int offset, RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                String[][] isbns = api.search(query, limit, offset);
                handler.post(() -> callback.onComplete(new Result.Success(isbns)));
            } catch(Exception exception) {
                handler.post(() -> callback.onComplete(new Result.Error(exception)));
            }
        });
    }

    public void fetchBook(String isbn, RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                Book book = api.fetchBook(isbn);
                handler.post(() -> callback.onComplete(new Result.Success(book)));
            } catch(Exception exception) {
                handler.post(() -> callback.onComplete(new Result.Error(exception)));
            }
        });
    }
}
