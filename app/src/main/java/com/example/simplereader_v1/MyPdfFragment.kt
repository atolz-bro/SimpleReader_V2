package com.example.simplereader_v1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.pspdfkit.datastructures.TextSelection
import com.pspdfkit.ui.PdfFragment
import com.pspdfkit.ui.special_mode.controller.TextSelectionController
import com.pspdfkit.ui.special_mode.manager.TextSelectionManager

class MyPdfFragment( val context : AppCompatActivity) : TextSelectionManager.OnTextSelectionChangeListener, TextSelectionManager.OnTextSelectionModeChangeListener{
     val TAG = "MyActivityTextSelection"



    override fun onEnterTextSelectionMode(controller: TextSelectionController) {
        Log.i(TAG, "Text selection mode has started.")
        context.findViewById<LinearLayout>(R.id.options_panel).visibility = View.VISIBLE
    }

    override fun onExitTextSelectionMode(controller: TextSelectionController) {
        Log.i(TAG, "Text selection mode has ended.")
        context.findViewById<LinearLayout>(R.id.options_panel).visibility = View.GONE
    }


    override fun onBeforeTextSelectionChange(p0: TextSelection?, p1: TextSelection?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onAfterTextSelectionChange(p0: TextSelection?, p1: TextSelection?) {
        TODO("Not yet implemented")
    }
}