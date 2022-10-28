package pt.isec.a2022108914.rascunhos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pt.isec.a2022108914.rascunhos.databinding.ActivityConfigImageBinding

class ConfigImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigImageBinding
    private var mode: Int = GALLERY

    companion object {
        const val TAG = "ConfigImageActivity"
        val GALLERY = 1
        val CAMERA = 2
        private const val TYPE_KEY = "type"

        fun getGalleryIntent(context: Context): Intent {
            val intent = Intent(context, ConfigImageActivity::class.java)
            intent.putExtra(TYPE_KEY, GALLERY)
            return intent
        }

        fun getPhotoIntent(context: Context): Intent {
            val intent = Intent(context, ConfigImageActivity::class.java)
            intent.putExtra(TYPE_KEY, CAMERA)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getIntExtra(TYPE_KEY, GALLERY)
        when(mode) {
            GALLERY -> {
                binding.btnImage.text = getString(R.string.btn_choose_image)
                binding.btnImage.setOnClickListener { onGalleryPressed() }
            }
            CAMERA -> {
                binding.btnImage.text = getString(R.string.btn_take_photo)
                binding.btnImage.setOnClickListener { onPhotoPressed() }
            }
        }
    }

    private fun onPhotoPressed() {
        Log.d(TAG, "Photo pressed")
        TODO("Not yet implemented")
    }

    private fun onGalleryPressed() {
        Log.d(TAG, "Gallery pressed")
        TODO("Not yet implemented")
    }
}