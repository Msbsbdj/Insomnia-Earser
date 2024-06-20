package com.marco.ensominaearser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentPlayerBinding
import com.marco.ensominaearser.utilites.MyExoplayer


class PlayerFragment : Fragment() {
  private lateinit var binding:FragmentPlayerBinding
  private lateinit var exoPlayer: ExoPlayer
  var playerListener = object:Player.Listener{
      override fun onIsPlayingChanged(isPlaying: Boolean) {
          super.onIsPlayingChanged(isPlaying)
          showGif(isPlaying)
      }
  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlayerBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupExoPlayer()
        return binding.root
    }


    @OptIn(UnstableApi::class)
    private fun setupExoPlayer(){
        MyExoplayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = this.title
            Glide.with(requireContext()).load(this.coverUrl).circleCrop()
                .into(binding.songCoverImageView)
            exoPlayer = MyExoplayer.getInstance()!!
            exoPlayer.addListener(playerListener)
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
        }
    }
private fun showGif(show:Boolean){
    if (show)
        binding.songGifImageView.visibility = View.VISIBLE
    else
        binding.songGifImageView.visibility = View.INVISIBLE
}

}