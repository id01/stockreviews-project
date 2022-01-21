package one.id0.stockreviews;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import one.id0.stockreviews.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser user;

    private MenuItem signInButton;

    private String stockBeingViewed;
    private RxDataStore<Preferences> dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        stockBeingViewed = null;

        ActivityResultLauncher<Intent> reviewAddLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), uri->{
            int resultCode = uri.getResultCode();
            String data = uri.getData().getDataString();
            int firstSpace = data.indexOf(' ');
            Review review = new Review(user.getUid(), data.substring(firstSpace+1), Integer.parseInt(data.substring(0, firstSpace)), System.currentTimeMillis());
            Log.v(TAG, review.toString());
            db.collection("User/Ticker/" + stockBeingViewed).document(user.getUid()).set(review.toMap()).addOnSuccessListener(aVoid->{
                Snackbar.make(binding.getRoot(), "Review added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }).addOnFailureListener(aVoid->{
                Snackbar.make(binding.getRoot(), "Review add failed!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
        });

        binding.fab.setOnClickListener(v->{
            if (stockBeingViewed != null) {
                // Add reviews
                if (user == null || db == null) {
                    Snackbar.make(binding.getRoot(), "You need to sign in for that!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    reviewAddLauncher.launch(new Intent(this, ReviewAddActivity.class));
                }
            } else {
                // Add stock to list
                navController.navigate(R.id.action_firstFragment_to_addStockFragment);
                binding.fab.setVisibility(GONE);
            }
        });

        db = FirebaseFirestore.getInstance();
    }

    public void signIn() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || db == null) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

            // Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        } else {
            signInButton.setTitle("Sign Out");
        }
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    IdpResponse response = result.getIdpResponse();
                    if (result.getResultCode() == RESULT_OK) {
                        // Successfully signed in
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        db = FirebaseFirestore.getInstance();
                        signInButton.setTitle("Sign Out");
                    } else {
                        // Sign in failed. If response is null the user canceled the
                        // sign-in flow using the back button. Otherwise check
                        // response.getError().getErrorCode() and handle the error.
                        // ...
                        Log.println(Log.ERROR, "APP", "SIGN IN FAILED");
                    }
                }
            }
    );

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                user = null;
                signInButton.setTitle("Sign In");
            }
        });
    }

    public FirebaseFirestore getDB() {
        return db;
    }

    public void setStockBeingViewed(String stockBeingViewed) {
        this.stockBeingViewed = stockBeingViewed;
        getSupportActionBar().setDisplayHomeAsUpEnabled(stockBeingViewed != null);
    }

    // Resets variables to the main page
    public void resetVariablesToMainPage() {
        this.stockBeingViewed = null;
        try {
            this.binding.fab.setVisibility(VISIBLE);
        } catch (NullPointerException e) {
            // Do nothing
        }
    }

    // Gets datastore
    public RxDataStore<Preferences> getDataStore() {
        if (dataStore == null) {
            dataStore = new RxPreferenceDataStoreBuilder(this, "tickers").build();
        }
        return dataStore;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_signin) {
            signInButton = item;
            if (item.getTitle() == "Sign Out") {
                signOut();
            } else {
                signIn();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}