package pt.isec.a2022108914.rascunhos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import pt.isec.a2022108914.rascunhos.databinding.ActivityConfigImageBinding
import java.io.File

class ConfigImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigImageBinding
    private var mode: Int = GALLERY

    private var imagePath: String? = null

    private var startActivityForGalleryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultIntent = result.data
            resultIntent?.data?.let { uri ->
                imagePath = createFileFromUri(this, uri)
                updatePreview()
            }
        }
    }

    private var startActivityForTakePhotoResult = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {success ->
        if(!success) {
            imagePath = null
        }
        updatePreview()
    }

    private var permissionsGranted: Boolean = false
        set(value) {
            field = value
            binding.btnImage.isEnabled = value
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            permissionsGranted = isGranted
        }
    }


    companion object {
        const val TAG = "ConfigImageActivity"
        val GALLERY = 1
        val CAMERA = 2
        private const val TYPE_KEY = "type"
        private const val PERMISSION_CODE = 1234
        private const val GALLERY_REQUEST_CODE = 10

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
        //checkPermissions_V1()
        checkPermissions_V2()
        updatePreview()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.mnCreate) {
            if (binding.edTitle.text.isEmpty()) {
                Snackbar.make(binding.edTitle,
                    R.string.msg_empty_title, Snackbar.LENGTH_LONG)
                    .show()
                binding.edTitle.requestFocus()
                return true
            }

            if (imagePath == null) {
                Snackbar.make(
                    binding.edTitle,
                    getString(R.string.img_missing),
                    Snackbar.LENGTH_LONG)
                    .show()
                binding.edTitle.requestFocus()
                return true
            }

            val intent = Intent(
                DrawingAreaActivity.getIntent(
                    this,
                    binding.edTitle.text.trim().toString(),
                    imagePath!!
                )
            )
            startActivity(intent)
            finish() // removes this activity from the back-stack
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissions_V1() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSION_CODE
                )
            }
        }
    }

    fun checkPermissions_V2() {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return
        }

        val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
                            else android.Manifest.permission.READ_EXTERNAL_STORAGE */

        requestPermissionLauncher.launch(permission)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_CODE){
            // verify permissions ...
            permissionsGranted = grantResults
                .all { it == PackageManager.PERMISSION_GRANTED }
            return
        }
    }

    private fun onPhotoPressed() {
        imagePath = getTempFilename(this)
        startActivityForTakePhotoResult.launch(
            FileProvider.getUriForFile(this,
            "pt.isec.a2022108914.rascunhos.android.fileprovider",
            File(imagePath)
            )
        )
    }

    private fun onGalleryPressed() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForGalleryResult.launch(intent)
        //startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun updatePreview() {
        if(imagePath == null) {
            binding.frPreview.background = ResourcesCompat.getDrawable(
                resources, R.drawable.trees, null)
        } else {
            setPic(binding.frPreview, imagePath!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, info: Intent?) {
        if (requestCode == GALLERY_REQUEST_CODE &&
            resultCode == Activity.RESULT_OK &&
            info != null
        ) {
            info.data?.let {
                val cursor = contentResolver.query(
                    it,
                    arrayOf(MediaStore.Images.ImageColumns.DATA),
                    null,
                    null,
                    null
                )
                if(cursor != null && cursor.moveToFirst()) {
                    imagePath = cursor.getString(0)
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, info)
    }
}