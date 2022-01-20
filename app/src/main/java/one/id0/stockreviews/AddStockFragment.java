package one.id0.stockreviews;

import android.os.Bundle;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.datastore.preferences.PreferencesProto;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.rxjava3.core.Single;
import one.id0.stockreviews.databinding.FragmentAddStockBinding;
import one.id0.stockreviews.databinding.FragmentViewReviewsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStockFragment extends Fragment {
    private FragmentAddStockBinding binding;

    public AddStockFragment() {
        // Required empty public constructor
    }

    public static AddStockFragment newInstance() {
        AddStockFragment fragment = new AddStockFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Init binding
        binding = FragmentAddStockBinding.inflate(inflater, container, false);
        View bindingRoot = binding.getRoot();
        // When submitButton is clicked add the ticker to preferences and navigate back to the main page
        binding.submitButton.setOnClickListener(v->{
            RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(getActivity(), "tickers").build();
            dataStore.updateDataAsync(dataIn->{
                MutablePreferences mutablePreferences = dataIn.toMutablePreferences();
                mutablePreferences.set(new Preferences.Key<Boolean>(binding.tickerInput.getText().toString()), true);
                return Single.just(mutablePreferences);
            });
            Navigation.findNavController(bindingRoot).navigate(R.id.action_addStockFragment_to_firstFragment);
        });
        return bindingRoot;
    }
}