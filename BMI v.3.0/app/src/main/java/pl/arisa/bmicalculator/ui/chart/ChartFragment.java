package pl.arisa.bmicalculator.ui.chart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pl.arisa.bmicalculator.R;
import pl.arisa.bmicalculator.ui.bmi.BmiFragment;


public class ChartFragment extends Fragment {
    WebView wb_chart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wb_chart = (WebView) view.findViewById(R.id.webview_chart);
        wb_chart.loadUrl("https://ourworldindata.org/obesity");
    }
}