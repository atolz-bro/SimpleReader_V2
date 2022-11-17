package com.example.simplereader_v1.Adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.simplereader_v1.database.models.Note
import com.example.simplereader_v1.ui.ViewPagerRevisionNoteFragment

class RevisonFragmentViewPager2Adapter(frag: Fragment,val revisionNotes: List<Note>) : FragmentStateAdapter(frag) {
    override fun getItemCount(): Int {
        return revisionNotes.size
    }

    override fun createFragment(position: Int): Fragment {
        val frag = ViewPagerRevisionNoteFragment()
        val bundle = Bundle()
        bundle.putString("note",revisionNotes.get(position).text)
        bundle.putInt("color", revisionNotes.get(position).color)
        frag.arguments = bundle
        return frag
    }
}