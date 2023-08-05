package aandrosov.bookstore;

import aandrosov.bookstore.fragment.CategoryPageFragment;
import aandrosov.bookstore.fragment.FavoritesPageFragment;
import aandrosov.bookstore.fragment.ProfilePageFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity {

    private final ProfilePageFragment profilePageFragment = new ProfilePageFragment();
    private CategoryPageFragment categoryPageFragment;
    private final FavoritesPageFragment favoritesPageFragment = new FavoritesPageFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        Application app = (Application) getApplication();
        categoryPageFragment = new CategoryPageFragment(app.booksRepository, app.fileRepository);

        setPageByItemId(R.id.page_category);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> setPageByItemId(item.getItemId()));
    }

    public boolean setPageByItemId(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(id == R.id.page_category) {
            transaction.replace(R.id.fragment_container, categoryPageFragment).commit();
        } else if(id == R.id.page_favorites) {
            transaction.replace(R.id.fragment_container, favoritesPageFragment).commit();
        } else if(id == R.id.page_profile) {
            transaction.replace(R.id.fragment_container, profilePageFragment).commit();
        } else {
            return false;
        }

        return true;
    }

    public void setIndeterminateProgressBarVisibility(int mode) {
        findViewById(R.id.indeterminate_progress_bar).setVisibility(mode);
    }

    public int getIndeterminateProgressBarVisibility() {
        return findViewById(R.id.indeterminate_progress_bar).getVisibility();
    }
}
