package pt.isec.a2022108914.rascunhos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import pt.isec.a2022108914.rascunhos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnColor.setOnClickListener {
            intent = Intent(this, ConfigColorActivity::class.java)
            startActivity(intent)
        }
        binding.btnGallery.setOnClickListener {
            startActivity(ConfigImageActivity.getGalleryIntent(this))
        }
        binding.btnPhoto.setOnClickListener {
            startActivity(ConfigImageActivity.getPhotoIntent(this))
        }
        binding.btnHistory.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_soon), Toast.LENGTH_SHORT).show()
        }
        val p = resources.getStringArray(R.array.option_list)
        p.sort()
    }

    private fun dialog() {
        var dialog = AlertDialog.Builder(ContextThemeWrapper(this, R.style.MyDialogTheme))
            .setTitle("Title 1")
            .setMessage("Message 1")
            .setItems(R.array.option_list) { d,b -> Log.i("option", b.toString() + " selected") }
            //.setSingleChoiceItems(arrayOf("Option 1", "Option 2", "Option 3")) { Toast.makeText(this, "option x", Toast.LENGTH_SHORT).show() }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Ok") { d,b -> Toast.makeText(this,"dialog1::ok", Toast.LENGTH_SHORT).show()}
            .setNeutralButton("Maybe") { d,b -> Toast.makeText(this,"dialog1::maybe", Toast.LENGTH_SHORT).show()}
            .setNegativeButton("Cancel") { d,b -> Toast.makeText(this,"dialog1::cancel", Toast.LENGTH_SHORT).show()}
            .setCancelable(true)
            .create()
        dialog.show()
    }
}

/*
class MyDialogFragment : DialogFragment() {
    lateinit var editText: EditText
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editText = EditText(requireActivity())
        return AlertDialog.Builder
    }
}*/
