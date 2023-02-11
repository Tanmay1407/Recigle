package com.patel.tanmay.recigle.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_instructions, container, false)

        val args = arguments
        val myBundle : com.patel.tanmay.recigle.models.Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        view.instructions_webview.webViewClient = object : WebViewClient(){}
        val webURL : String = myBundle!!.sourceUrl
        view.instructions_webview.loadUrl(webURL)

        return view
    }
}