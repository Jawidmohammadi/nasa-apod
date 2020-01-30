package com.jawidmohammadi.nasaapod.controller;

import android.os.Bundle;
import android.provider.CalendarContract.CalendarAlerts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jawidmohammadi.nasaapod.R;
import com.jawidmohammadi.nasaapod.model.Apod;
import com.jawidmohammadi.nasaapod.viewmodel.MainViewModel;
import edu.cnm.deepdive.android.DateTimePickerFragment;
import edu.cnm.deepdive.android.DateTimePickerFragment.Mode;
import edu.cnm.deepdive.android.DateTimePickerFragment.OnChangeListener;
import java.util.Calendar;

public class ImgaeFragment extends Fragment {
    private static final String IMAGE_URL =
        "https://apod.nasa.gov/apod/image/2001/Comet67P_Rosetta_1024.jpg";
    private WebView contentView;
    private MainViewModel viewModel;
    private ProgressBar loading;
    private FloatingActionButton calendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image, container, false);
        loading = root.findViewById(R.id.loading);
        calendar = root.findViewById(R.id.calendar);
        setupWebView(root);
        setupCalendarPicker(Calendar.getInstance());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel.getApod().observe(getViewLifecycleOwner(),
            (Apod apod) -> {
                contentView.loadUrl(apod.getUrl());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(apod.getDate());
        setupCalendarPicker(calendar);
            });
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
                loading.setVisibility(View.GONE);
            }
        });
        WebSettings settings = contentView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

  private void setupCalendarPicker(Calendar calendar) {
        this.calendar.setOnClickListener((v) -> {
            DateTimePickerFragment fragment = new DateTimePickerFragment();
            fragment.setCalendar(calendar);
            fragment.setMode(Mode.DATE);
            fragment.setOnChangeListener((cal) -> {
            loading.setVisibility(View.VISIBLE);
            viewModel.setApodDate(cal.getTime());
            });
            fragment.show(getChildFragmentManager(), fragment.getClass().getName());
        });
    }
}