package com.example.myapp009bimagetoapp

import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp009bimagetoapp.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var originalBitmap: Bitmap? = null
    private var currentBitmap: Bitmap? = null

    private var editingImagePath: String? = null  // ← přidáno

    private var currentBrightness = 0f
    private var currentContrast = 1f
    private var currentSaturation = 1f
    private var isGrayscale = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupImagePicker()
        setupSeekBars()
        setupButtons()

        // Pokud přichází obrázek z galerie → zapamatujeme si jeho cestu
        intent.getStringExtra("image_path")?.let { path ->
            editingImagePath = path

            val bitmap = BitmapFactory.decodeFile(path)
            originalBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)
            currentBitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)

            binding.ivImage.setImageBitmap(currentBitmap)
            resetValues()
        }
    }

    private fun setupImagePicker() {
        val getContent =
            registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    originalBitmap = try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(contentResolver, it)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            @Suppress("DEPRECATION")
                            MediaStore.Images.Media.getBitmap(contentResolver, it)
                        }
                    } catch (e: Exception) {
                        null
                    }

                    editingImagePath = null  // ← nový obrázek nemá původní cestu

                    originalBitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
                    currentBitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)

                    binding.ivImage.setImageBitmap(currentBitmap)
                    resetValues()
                }
            }

        binding.btnTakeImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun resetValues() {
        currentBrightness = 0f
        currentContrast = 1f
        currentSaturation = 1f
        isGrayscale = false

        binding.seekBarBrightness.progress = 100
        binding.seekBarContrast.progress = 50
        binding.seekBarSaturation.progress = 100
    }

    private fun setupSeekBars() {
        binding.seekBarBrightness.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentBrightness = (progress - 100).toFloat()
                binding.tvBrightnessValue.text = "Jas: ${progress - 100}%"
                if (fromUser) updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarContrast.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentContrast = progress / 50f
                binding.tvContrastValue.text = "Kontrast: ${"%.2f".format(currentContrast)}x"
                if (fromUser) updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarSaturation.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentSaturation = progress / 100f
                binding.tvSaturationValue.text = "Sytost: ${progress}%"
                if (fromUser) updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupButtons() {

        binding.btnGrayscale.setOnClickListener {
            isGrayscale = !isGrayscale
            updatePreview()
            binding.btnGrayscale.text = if (isGrayscale) "✓ Černobílý" else "⚫ Černobílý"
        }

        binding.btnSave.setOnClickListener {
            saveEditedImage()
        }

        binding.btnReset.setOnClickListener {
            resetValues()
            currentBitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
            binding.ivImage.setImageBitmap(currentBitmap)
            binding.btnGrayscale.text = "⚫ Černobílý"
            Toast.makeText(this, "Úpravy byly resetovány", Toast.LENGTH_SHORT).show()
        }

        binding.btnGallery.setOnClickListener {
            startActivity(android.content.Intent(this, GalleryActivity::class.java))
        }
    }

    private fun updatePreview() {
        originalBitmap?.let { bitmap ->
            currentBitmap = adjustBitmap(
                bitmap,
                currentBrightness,
                currentContrast,
                if (isGrayscale) 0f else currentSaturation
            )
            binding.ivImage.setImageBitmap(currentBitmap)
        }
    }

    private fun adjustBitmap(
        bitmap: Bitmap,
        brightness: Float,
        contrast: Float,
        saturation: Float
    ): Bitmap {
        val adjustedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(adjustedBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val colorMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )

        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturation)

        colorMatrix.postConcat(saturationMatrix)

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    // ⬇⬇⬇ TADY JE HLAVNÍ ZMĚNA – PŘEPISOVÁNÍ PŮVODNÍHO SOUBORU
    private fun saveEditedImage() {
        currentBitmap?.let { bitmap ->

            editingImagePath?.let { path ->        // ← pokud upravuji existující fotku
                try {
                    FileOutputStream(File(path)).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                    }

                    Toast.makeText(this, "✓ Fotografie byla aktualizována", Toast.LENGTH_LONG).show()

                    originalBitmap = currentBitmap?.copy(Bitmap.Config.ARGB_8888, true)
                    return
                } catch (e: Exception) {
                    Toast.makeText(this, "Chyba při ukládání: ${e.message}", Toast.LENGTH_LONG).show()
                    return
                }
            }

            // ↓↓↓ TOTO SE PROVEDE JEN POKUD JDE O NOVÝ OBRÁZEK ↓↓↓

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "IMG_$timestamp.jpg"

            val directory = File(filesDir, "edited_images")
            if (!directory.exists()) directory.mkdirs()

            val file = File(directory, fileName)

            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }

                saveMetadata(fileName, file.absolutePath)
                Toast.makeText(this, "✓ Upravená fotografie je uložena", Toast.LENGTH_LONG).show()

                originalBitmap = currentBitmap?.copy(Bitmap.Config.ARGB_8888, true)

            } catch (e: Exception) {
                Toast.makeText(this, "Chyba při ukládání: ${e.message}", Toast.LENGTH_LONG).show()
            }

        } ?: Toast.makeText(this, "Nejprve vyberte obrázek", Toast.LENGTH_SHORT).show()
    }

    private fun saveMetadata(name: String, path: String) {
        val prefs = getSharedPreferences("image_metadata", MODE_PRIVATE)
        val editor = prefs.edit()

        val id = UUID.randomUUID().toString()

        editor.putString("$id-name", name)
        editor.putString("$id-path", path)
        editor.putLong("$id-timestamp", System.currentTimeMillis())

        val ids = prefs.getStringSet("image_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        ids.add(id)

        editor.putStringSet("image_ids", ids)
        editor.apply()
    }
}
