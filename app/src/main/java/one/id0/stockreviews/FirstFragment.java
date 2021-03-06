package one.id0.stockreviews;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Flowable;
import one.id0.stockreviews.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private MainActivity activity;
    private Set<String> tickersAdded;

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        activity = (MainActivity)getActivity();
        tickersAdded = new HashSet<>();

        RxDataStore<Preferences> dataStore = ((MainActivity)getActivity()).getDataStore();
        Flowable<List<String>> tickerFlow = dataStore.data().map(prefs->{
            ArrayList<String> out = new ArrayList<>();
            for (Preferences.Key<?> key : prefs.asMap().keySet()) {
                out.add(key.getName());
            }
            return out;
        });
        List<String> tickers = new ArrayList<>();
        tickerFlow.forEach(tickerSet->{
            for (String ticker : tickerSet) {
                if (!tickersAdded.contains(ticker)) {
                    tickersAdded.add(ticker);

                    URL url = new URL("http://frontier-ssh.id0.one:5000/tickerMetadata?ticker=" + ticker);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    MiniStonkInfo msi;
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        JSONObject jobj = new JSONObject(readStream(in));
                        double rating = jobj.getDouble("rating");
                        msi = MiniStonkInfo.newInstance(ticker, (float) rating, 0.0f, 0.0f, 0.0f);
                    } catch (FileNotFoundException e) {
                        msi = null;
                    } finally {
                        urlConnection.disconnect();
                    }

                    if (msi != null) {
                        View msiView = msi.onCreateView(inflater, container, savedInstanceState);
                        final MiniStonkInfo msifinal = msi;
                        msiView.setOnClickListener(v -> {
                            activity.setStockBeingViewed(ticker);
                            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_firstFragment_to_viewReviewsFragment, msifinal.getParams());
                        });
                        getActivity().runOnUiThread(() -> {
                            if (binding != null) {
                                binding.stonkView.addView(msiView);
                            }
                        });
                    }
                }
            }
        });

/*        MiniStonkInfo msi = MiniStonkInfo.newInstance("AAPL", 3.2f, 0.1f, 100.55f, 1.1f);
        View msiView = msi.onCreateView(inflater, container, savedInstanceState);
        binding.stonkView.addView(msiView);
        msiView.setOnClickListener(v->{
            activity.setStockBeingViewed("AAPL");
            Log.println(Log.VERBOSE, "CLICKED", "CLICKED");
            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_firstFragment_to_viewReviewsFragment, msi.getParams());
        });*/

        activity.resetVariablesToMainPage();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}