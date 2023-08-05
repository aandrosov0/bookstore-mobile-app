package aandrosov.bookstore;

import aandrosov.api.books.Author;
import aandrosov.api.books.Book;
import aandrosov.api.books.Cover;
import aandrosov.bookstore.repository.FileRepository;
import aandrosov.bookstore.repository.Result;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class CardOverviewBookAdapter extends RecyclerView.Adapter<CardOverviewBookAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover;
        private final TextView title;
        private final TextView author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
        }

    }
    private final List<Book> books;

    private final FileRepository fileRepository;

    public CardOverviewBookAdapter(List<Book> books, FileRepository fileRepository) {
        this.books = books;
        this.fileRepository = fileRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_book_overview, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());

        Author[] authors = book.getAuthors();

        if(authors != null) {
            holder.author.setText(book.getAuthors()[0].getName());
        } else {
            holder.author.setText("No Author");
        }

        Cover cover = book.getCover();
        if(cover != null) {
            fileRepository.downloadFile(cover.getMedium(), r -> setBookCover(holder.cover, r));
        }
    }



    @Override
    public int getItemCount() {
        return books.size();
    }

    private void setBookCover(ImageView cover, Result result) {
        if(result instanceof Result.Success) {
            File file = (File) result.getData();
            Drawable drawable = Drawable.createFromPath(file.getPath());
            if(cover != null) {
                cover.post(() -> {
                    cover.setImageDrawable(drawable);
                    cover.setForeground(null);
                });
            }
        } else {
            Object data = result.getData();
            if(data instanceof FileNotFoundException) {
                return;
            }

            cover.post(() -> Utils.showSimpleDialog(cover.getContext(), "Error", data.toString()));
        }
    }
}
