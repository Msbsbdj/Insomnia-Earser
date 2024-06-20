package com.marco.ensominaearser.ui.adapters
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.marco.ensominaearser.databinding.SongListItemRecyclerRowBinding
import com.marco.ensominaearser.data.pojo.SongModel
import com.marco.ensominaearser.utilites.MyExoplayer
import com.marco.ensominaearser.utilites.onSongClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongsAdapter(var onSongClickListener: onSongClickListener):RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {
    lateinit var onSongClick:((String)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return (oldItem==newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this,diffUtil)

     class SongsViewHolder(var binding: SongListItemRecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
       return  SongsViewHolder(SongListItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
    return diff.currentList.size
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
         val mFireStore = FirebaseFirestore.getInstance()

            mFireStore.collection("songs").document(diff.currentList[position]).get().addOnSuccessListener {
                val song = it.toObject(SongModel::class.java)
                CoroutineScope(Dispatchers.Main).launch{
                    Glide.with(holder.binding.root.context).load(song!!.coverUrl).into(holder.binding.songCoverImageView)
                    holder.binding.songTitleTextView.text = song.title
                    holder.binding.root.setOnClickListener {
                        MyExoplayer.startPlaying(holder.binding.root.context,song)
                        onSongClick.invoke("")
                    }
                }
            }.addOnFailureListener {
                Log.d("Error",it.message.toString())
            }


    }
}