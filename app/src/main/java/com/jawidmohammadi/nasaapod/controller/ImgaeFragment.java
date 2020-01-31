package com.jawidmohammadi.nasaapod.controller;

import android.icu.text.IDNA.Info;
import android.os.Bundle;
import android.provider.CalendarContract.CalendarAlerts;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
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
  private Apod apod;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setRetainInstance(true);
  }

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
        (apod) -> {
          contentView.loadUrl(apod.getUrl());
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(apod.getDate());
          setupCalendarPicker(calendar);
          this.apod = apod;
        });
    viewModel.getThrowable().observe(getViewLifecycleOwner(), (throwable) -> {
      loading.setVisibility(View.GONE);
      Toast toast = Toast.makeText(getActivity(),
          getString(R.string.error_message, throwable.getMessage()), Toast.LENGTH_LONG);
      toast.setGravity(Gravity.BOTTOM, 0,
          (int) getResources().getDimension(R.dimen.toast_vertical_margin));
      toast.show();
    });
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.options, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()){
      case R.id.info:
        if (apod != null){
          InfoFragment fragment = new InfoFragment();
          Bundle args = new Bundle();
          args.putString(InfoFragment.TITLE_KEY, apod.getTitle());
          args.putString(InfoFragment.DESCRIPTION_KEY, apod.getDescription());
          args.putString(InfoFragment.COPYRIGHT_KEY, apod.getCopyright());
          args.putSerializable(InfoFragment.DATE_KEY, apod.getDate());
          fragment.setArguments(args);
          fragment.show(getChildFragmentManager(), fragment.getClass().getName());
        }
        break ;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;

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
        Toast toast = Toast.makeText(getActivity(), apod.getTitle(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, (int) getContext().getResources()
            .getDimensionPixelOffset(R.dimen.toast_vertical_margin));
        toast.show();
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