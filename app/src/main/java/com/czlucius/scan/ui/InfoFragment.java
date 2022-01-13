/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2021 Lucius Chee Zihan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.callbacks.Producer;
import com.czlucius.scan.databinding.FragmentInfoBinding;
import com.czlucius.scan.misc.monetization.AdStrategy2;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


/**
 *
 * @deprecated Switching over to preferences fragment so can set some settings
 *
 */
@Deprecated
public class InfoFragment extends Fragment {
    private FragmentInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentInfoBinding.inflate(inflater, container, false);


        // WebView for displaying open-source licenses
        WebView webView = new WebView(requireContext());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return shouldOverrideUrlLoading(Uri.parse(url));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                return shouldOverrideUrlLoading(url);
            }

            private boolean shouldOverrideUrlLoading(Uri url) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(url);
                boolean appAvailable = Utils.launchIntentCheckAvailable(urlIntent, requireContext());
                if (!appAvailable) {
                    Toast.makeText(getContext(), R.string.no_browsers, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        webView.zoomBy(0.8F);

        MaterialAlertDialogBuilder ossDialog = new MaterialAlertDialogBuilder(requireContext());
        ossDialog.setView(webView);
        AlertDialog finalDialog = ossDialog.create();

        binding.openSourceButton.setOnClickListener(v -> {
            webView.loadUrl("file:///android_asset/licenses.html"); // Load from app's asset folder
            finalDialog.show();
        });



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // AdMob SDK, will not display (or even connect to internet) on F-Droid and Galaxy versions
        AdStrategy2 adStrategy = AdStrategy2.getInstance(requireContext().getApplicationContext());
        adStrategy.loadAdView(view::findViewById);
        adStrategy.initialiseRewardedAds(getActivity(), view);
    }
}
