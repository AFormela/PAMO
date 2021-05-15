package pl.arisa.bmicalculator.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import pl.arisa.bmicalculator.R

class ChartFragment : Fragment() {
    var wb_chart: WebView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wb_chart = view.findViewById<View>(R.id.webview_chart) as WebView
        wb_chart!!.loadUrl("https://ourworldindata.org/obesity")
    }
}