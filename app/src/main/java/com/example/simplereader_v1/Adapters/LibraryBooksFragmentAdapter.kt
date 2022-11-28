package com.example.simplereader_v1.Adapters

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplereader_v1.MainActivity
import com.example.simplereader_v1.MyPdfActivity
import com.example.simplereader_v1.R
import com.example.simplereader_v1.ReaderActivity
import com.example.simplereader_v1.database.models.LibraryBook
import com.example.simplereader_v1.databinding.LibraryBookItemBinding
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.configuration.activity.ThumbnailBarMode
import com.pspdfkit.ui.PdfActivityIntentBuilder

class LibraryBooksFragmentAdapter(val context : FragmentActivity, val remove : ( b: LibraryBook)->Unit) : ListAdapter<LibraryBook,LibraryBooksFragmentAdapter.LibraryBookViewHolder>(
    DiffCallBack){
     init {
         Log.d("TagD","init")
         Log.d("TagD","init$itemCount")

     }

    class LibraryBookViewHolder(var binding : LibraryBookItemBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryBookViewHolder {
        Log.d("TagD","onCreateAdapter")
        return LibraryBookViewHolder(
            LibraryBookItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LibraryBookViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            thumbnail.setImageURI(Uri.parse(item.thumbnailUri))
            Log.d("TagD",item.thumbnailUri)
            title.text = item.name
            removeBook.setOnClickListener(){
                remove(item)
            }
            progress.text = context.getString(R.string.progress,item.currentPage,item.pageCount)

            timeSpent.text = context.getString(R.string.time_in_pdf,item.timeUsed/60)

            PopupMenu(context,more).apply {
                inflate(R.menu.more_options)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.more_info ->{
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
                more.setOnClickListener(){
                    show()
                }

            }
            thumbnail.setOnClickListener(){
                Log.d("TagD","onClick")
                val config = PdfActivityConfiguration
                    .Builder(context)
                    .setThumbnailBarMode(ThumbnailBarMode.THUMBNAIL_BAR_MODE_NONE)
                    .build()
                /*val intent = PdfActivityIntentBuilder
                    .fromUri(context,Uri.parse(item.locationUri))
                    .configuration(config)
                    .activityClass(MyPdfActivity::class.java)
                    .build()*/

                val intent = Intent(context, ReaderActivity::class.java)
                val bundle = Bundle()
                bundle.putString(MainActivity.LOCATION_URI,item.locationUri)
                bundle.putString(MainActivity.THUMNAIL_URI,item.thumbnailUri)
                bundle.putString(MainActivity.LIBRARY_BOOK_NAME,item.name)
                bundle.putInt(MainActivity.ID,item.id)
                bundle.putInt(MainActivity.PAGE_COUNT,item.pageCount)
                bundle.putInt(MainActivity.CURRENT_PAGE,item.currentPage)
                bundle.putLong(MainActivity.TIME_SPENT,item.timeUsed)
                intent.putExtras(bundle)
                context.startActivity(intent)


            }

        }
    }

    companion object{
        private val DiffCallBack = object : DiffUtil.ItemCallback<LibraryBook>(){
            override fun areItemsTheSame(oldItem: LibraryBook, newItem: LibraryBook): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: LibraryBook, newItem: LibraryBook): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}