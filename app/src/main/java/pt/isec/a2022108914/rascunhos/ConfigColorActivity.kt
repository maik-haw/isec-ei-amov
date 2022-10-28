package pt.isec.a2022108914.rascunhos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import pt.isec.a2022108914.rascunhos.databinding.ActivityConfigColorBinding

class ConfigColorActivity : AppCompatActivity() {
    lateinit var binding: ActivityConfigColorBinding

    private val currentColor: Int
        get() = Color.rgb(
            binding.seekRed.progress,
            binding.seekGreen.progress,
            binding.seekBlue.progress
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigColorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.seekRed.run {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(processSeekBar)
        }

        binding.seekBlue.run {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(processSeekBar)
        }

        binding.seekGreen.run {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(processSeekBar)
        }
        updateView()
    }

    private val processSeekBar = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            updateView()
/*            val toast = Toast(this)
            toast.run {
                setGravity(Gravity.CENTER,0,0)
                show()
            }*/
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }
    }

    fun updateView() {
        binding.frPreview.setBackgroundColor(currentColor)
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

            val intent = Intent(this, DrawingAreaActivity::class.java)
            intent.putExtra("title", binding.edTitle.text.trim().toString())
            intent.putExtra("red", binding.seekRed.progress)
            intent.putExtra("green", binding.seekGreen.progress)
            intent.putExtra("blue", binding.seekBlue.progress)

            startActivity(
                DrawingAreaActivity.getIntent(
                this,
                binding.edTitle.text.toString(),
                currentColor
            ))
            finish() // removes this activity from the back-stack
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}