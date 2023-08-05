package aandrosov.bookstore.fragment;

import aandrosov.api.books.Book;
import aandrosov.bookstore.CardOverviewBookAdapter;
import aandrosov.bookstore.MainActivity;
import aandrosov.bookstore.R;
import aandrosov.bookstore.Utils;
import aandrosov.bookstore.repository.BooksRepository;
import aandrosov.bookstore.repository.FileRepository;
import aandrosov.bookstore.repository.Result;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryPageFragment extends Fragment {

    public static final int MAX_BOOKS_VIEW = 10;

    private int bookOffset = 0;

    private final BooksRepository booksRepository;

    private final ArrayList<Book> books = new ArrayList<>();

    private final CardOverviewBookAdapter cardBookOverviewAdapter;

    private MainActivity activity;

    public CategoryPageFragment(BooksRepository booksRepository, FileRepository fileRepository) {
        this.booksRepository = booksRepository;
        cardBookOverviewAdapter = new CardOverviewBookAdapter(books, fileRepository);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_page_category, container, false);
        activity = (MainActivity) getActivity();

        RecyclerView cardBooksOverviewRecycler = layout.findViewById(R.id.card_book_overview_recycler);
        cardBooksOverviewRecycler.setAdapter(cardBookOverviewAdapter);

        SearchView searchView = layout.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(activity.getIndeterminateProgressBarVisibility() != View.VISIBLE) {
                        searchView.clearFocus();
                        resetBooks();
                        searchBooks(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            }
        );

        cardBooksOverviewRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                boolean canScrollDown = recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN);

                if(!canScrollDown && activity.getIndeterminateProgressBarVisibility() != View.VISIBLE) {
                    String query = searchView.getQuery().toString().trim();
                    bookOffset += MAX_BOOKS_VIEW;
                    searchBooks(query.isEmpty() ? "*" : query);
                }
            }
        });

        searchBooks("*");
        return layout;
    }

    public void resetBooks() {
        cardBookOverviewAdapter.notifyItemRangeRemoved(0, books.size());
        books.clear();
        bookOffset = 0;
    }

    private void searchBooks(String query) {
        activity.setIndeterminateProgressBarVisibility(View.VISIBLE);
        booksRepository.search(query, MAX_BOOKS_VIEW, bookOffset, this::onCompleteSearch);
    }

    private void onCompleteSearch(Result result) {
        if(result instanceof Result.Success) {
            String[][] isbns = (String[][]) result.getData();

            for(String[] isbn : isbns) {
                if(isbn == null) {
                    continue;
                }
                booksRepository.fetchBook(isbn[isbn.length - 1], this::onCompleteBookFetch);
            }
        } else {
            Utils.showSimpleDialog(getContext(), "Error", result.getData().toString());
            ((Exception) result.getData()).printStackTrace(System.err);
        }
        activity.setIndeterminateProgressBarVisibility(View.INVISIBLE);
    }

    private void onCompleteBookFetch(Result result) {
        if(result instanceof Result.Success) {
            Book fetchedBook = (Book) result.getData();
            books.add(fetchedBook);
            cardBookOverviewAdapter.notifyItemInserted(books.size());
        } else {
            Utils.showSimpleDialog(getContext(), "Error", result.getData().toString());
            ((Exception) result.getData()).printStackTrace(System.err);
        }
    }
}
