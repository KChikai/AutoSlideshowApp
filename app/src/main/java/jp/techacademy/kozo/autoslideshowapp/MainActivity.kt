package jp.techacademy.kozo.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE = 100
    private var slideShowTimer: Timer? = null
    private var slideShowTimerSec = 0
    private var slideShowHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // get images
        var imageContents: ImageContents = ImageContents(this)

        // set slide show
        start_pose_button.setOnClickListener {
            if (slideShowTimer == null){
                // set UI
                start_pose_button.text = "■"
                back_button.isEnabled = false
                forward_button.isEnabled = false

                // set timer
                slideShowTimer = Timer()
                slideShowTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        slideShowTimerSec += 1
                        if (slideShowTimerSec % 2 == 0) {
                            slideShowHandler.post {
                                var uri = imageContents.getNextUri(false)
                                if (uri != null){
                                    imageView.setImageURI(uri)
                                }
                            }
                        }
                    }
                }, 0, 1000)
            } else {
                // set UI
                start_pose_button.text = "▶"
                back_button.isEnabled = true
                forward_button.isEnabled = true

                // discord timer
                slideShowTimer!!.cancel()
                slideShowTimer = null
            }
        }

        back_button.setOnClickListener {
            if (slideShowTimer == null){
                var uri = imageContents.getNextUri(true)
                if (uri != null){
                    imageView.setImageURI(uri)
                }
            }
        }

        forward_button.setOnClickListener {
            if (slideShowTimer == null) {
                var uri = imageContents.getNextUri(false)
                if (uri != null){
                    imageView.setImageURI(uri)
                }
            }
        }
    }


}