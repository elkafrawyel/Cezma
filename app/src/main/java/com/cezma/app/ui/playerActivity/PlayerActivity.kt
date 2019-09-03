package com.cezma.app.ui.playerActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.cezma.app.R
import com.cezma.app.utiles.changeLanguage
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity(), Player.EventListener, VideoListener {

    private var BANDWIDTH = DefaultBandwidthMeter()
    var player: SimpleExoPlayer? = null

    companion object {

        const val videoUrlExtra = "videoUrl"

        fun start(context: Context, videoUrl: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(
                videoUrlExtra,
                videoUrl
            )
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)


        changeLanguage()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        initiatePlayer()


        val mUrl = intent?.extras?.getString(videoUrlExtra,"")

        play(mUrl!!)

        backImgv.setOnClickListener {
            release()
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        if (player != null) {
            player?.playWhenReady = true
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    private fun release() {
        if (player != null) {
            player?.removeListener(this)
            player?.removeVideoListener(this)
            player?.release()
            player = null
        }
    }
    override fun onStart() {
        hideSystemUI()
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player?.playWhenReady = false
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUI() {
        playerRootView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun initiatePlayer() {

        val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory()

        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(
                this,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            ),
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )

        video_view.useController = false

        if (video_view.player == null)
            video_view.player = player

        player?.addListener(this)

        player?.addVideoListener(this)

    }

    private fun play(url: String) {

        video_view.visibility = View.VISIBLE

        val videoUri = Uri.parse(url)

//        Log.i(TAG, "Url to play : $videoUri")

        val mediaSource = buildMediaSource(videoUri, resources.getString(R.string.app_name))

        player?.prepare(mediaSource)

        player?.playWhenReady = true
    }

    private fun buildMediaSource(uri: Uri, randomAppName: String): MediaSource {
        val defaultExtractorsFactory = DefaultExtractorsFactory()
        defaultExtractorsFactory.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS)
        defaultExtractorsFactory.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)

        return ExtractorMediaSource.Factory {
            DefaultDataSourceFactory(
                this,
                BANDWIDTH,
                DefaultHttpDataSourceFactory(
                    Util.getUserAgent(this, randomAppName),
                    BANDWIDTH
                )
            ).createDataSource()
        }.setExtractorsFactory(defaultExtractorsFactory)
            .createMediaSource(uri)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                loading.visibility = View.GONE
            }
            Player.STATE_BUFFERING -> {
                loading.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                loading.visibility = View.GONE
            }
            Player.STATE_ENDED -> {
                loading.visibility = View.GONE
                finish()
            }
            else -> {
                loading.visibility = View.GONE
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        loading.visibility = View.GONE

    }
}
