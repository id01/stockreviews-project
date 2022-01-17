package one.id0.stockreviews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import one.id0.stockreviews.databinding.ActivityReviewAddBinding;

public class ReviewAddActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityReviewAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReviewAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_review_add);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/
        binding.postButton.setOnClickListener(v->{
            Intent data = new Intent();
            String text = String.format("%d %s", (int)(binding.ratingSelect.getRating()), binding.reviewInput.getText().toString());
            data.setData(Uri.parse(text));
            setResult(RESULT_OK, data);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
/*        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_review_add);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }
}