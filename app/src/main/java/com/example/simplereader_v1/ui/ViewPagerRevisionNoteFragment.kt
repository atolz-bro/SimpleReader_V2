package com.example.simplereader_v1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.simplereader_v1.R

class ViewPagerRevisionNoteFragment : Fragment() {

    var noteText = "Empty"
    var noteColor = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteText = arguments?.getString("note")!!
        noteColor = arguments?.getInt("color")!!
        return inflater.inflate(R.layout.viewpager_revision_note,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.text_view).text = noteText
        //view.findViewById<TextView>(R.id.text_view).setBackgroundColor(noteColor)
    }
}