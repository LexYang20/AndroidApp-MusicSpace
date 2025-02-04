package com.example.musicspace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicspace.R
import com.example.musicspace.model.SongModel

class SongAdapter(private val context: Context, songModelArrayList: ArrayList<SongModel>) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private val songModelArrayList: ArrayList<SongModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.song_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: SongModel = songModelArrayList[position]
        holder.textViewSongName.setText("" + model.getSongName())
        holder.imageViewSong.setImageResource(model.getSongImage())
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model )
            }
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: SongModel)
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return songModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSong: ImageView
        val textViewSongName: TextView

        init {
            textViewSongName = itemView.findViewById(R.id.textViewSongName)
            imageViewSong = itemView.findViewById(R.id.imageViewSong)
        }
    }

    // Constructor
    init {
        this.songModelArrayList = songModelArrayList
    }
}