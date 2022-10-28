package pt.isec.a2022108914.rascunhos

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import pt.isec.a2022108914.rascunhos.databinding.ActivityDrawingAreaBinding

class DrawingAreaActivity : AppCompatActivity() {
    lateinit var binding: ActivityDrawingAreaBinding
    private lateinit var drawingArea: DrawingArea

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title = intent.getStringExtra(TITLE_KEY) ?: getString(R.string.str_no_name)
        supportActionBar?.title = getString(R.string.app_name) +
                " " + title

        drawingArea = DrawingArea(
            this,
            intent.getIntExtra(COLOR_KEY, Color.WHITE)
        )

        binding.frDrawingArea.addView(drawingArea)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_drawing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.mnSave -> {
                Log.d(TAG, "Save")
            }
            item.groupId == R.id.grpColors -> {
                item.isChecked = true
                drawingArea.lineColor = when(item.itemId) {
                    R.id.mnWhite -> Color.WHITE
                    R.id.mnBlue -> Color.rgb(0x00, 0x00, 0xff)
                    R.id.mnYellow -> Color.YELLOW
                    else -> Color.BLACK
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        const val TAG = "DrawingAreaActivity"
        const val TITLE_KEY = "title"
        const val COLOR_KEY = "color"

        fun getIntent(context: Context, title: String, color: Int): Intent {
            val intent = Intent(context, DrawingAreaActivity::class.java)
            intent.putExtra(TITLE_KEY, title)
            intent.putExtra(COLOR_KEY, color)
            return intent
        }
    }
}