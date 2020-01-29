package com.jawidmohammadi.nasaapod.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jawidmohammadi.nasaapod.BuildConfig;
import com.jawidmohammadi.nasaapod.R;
import com.jawidmohammadi.nasaapod.model.Apod;
import com.jawidmohammadi.nasaapod.service.ApodService;
import com.jawidmohammadi.nasaapod.viewmodel.MainViewModel;
import java.io.IOException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImgaeFragment extends Fragment {

    private static final String IMAGE_URL =
        "https://apod.nasa.gov/apod/image/2001/Comet67P_Rosetta_1024.jpg";

    private WebView contentView;
    private MainViewModel viewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image, container, false);
        setupWebView(root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

    }

    private void setupWebView(View root) {
        contentView = root.findViewById(R.id.content_view);
        contentView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //TODO Update view to indicate that load is complete.
            }
        });
        WebSettings settings = contentView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        new Retriever().start();
    }

}