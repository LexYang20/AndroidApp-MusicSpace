package com.example.musicspace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicspace.R
import com.example.musicspace.model.Song

class PlaylistAdapter(
    private val songs: MutableList<Song>,
    private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    fun updatePlaylist(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songImageView: ImageView = itemView.findViewById(R.id.songImage)
        private val songTitleTextView: TextView = itemView.findViewById(R.id.songTitle)
        private val removeButton: Button = itemView.findViewById(R.id.removeButton)

        fun bind(song: Song) {
            songTitleTextView.text = song.title
            songImageView.setImageResource(song.imageResId) // Set the image resource
            removeButton.setOnClickListener {
                onRemoveClick(song.id)
            }
        }
    }
}
