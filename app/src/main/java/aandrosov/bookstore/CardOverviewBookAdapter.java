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
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        public void setCoverFromFilePathAndSave(String filePath) {
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
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holder.cover.setImageDrawable(null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        String isbn = book.getIsbn();
        Cover cover = book.getCover();
        String title = book.getTitle();
        Author[] authors = book.getAuthors();

        String cacheDirectoryPath = holder.itemView.getContext().getCacheDir().toString() + "/";
        Path downloadFilePath = Paths.get(cacheDirectoryPath + isbn);
        boolean isDownloadFileExists = Files.exists(downloadFilePath, LinkOption.NOFOLLOW_LINKS);

        System.out.println(book.getTitle() + " " + book.getUrl());
        if(cover != null && !isDownloadFileExists) {
            fileRepository.downloadFile(cover.getMedium(), downloadFilePath.toString(), result -> {
                Drawable drawable = Drawable.createFromPath((String) result.getData());
                holder.cover.setImageDrawable(drawable);
            });
        } else if(isDownloadFileExists) {
            Drawable drawable = Drawable.createFromPath(downloadFilePath.toString());
            holder.cover.setImageDrawable(drawable);
        }

        if(authors == null) {
            holder.author.setText("No Author");
        } else {
            holder.author.setText(authors[0].getName());
        }

        int titleLength = holder.itemView.getResources().getInteger(R.integer.card_book_title_length);
        if(title.length() > titleLength) {
            title = title.substring(0, titleLength) + "...";
        }

        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
