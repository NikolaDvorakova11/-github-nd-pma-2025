package com.example.myapp009bimagetoapp
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp009bimagetoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var originalBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Výběr obrázku
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
                        (binding.ivImage.drawable as? BitmapDrawable)?.bitmap
                    }

                    // vytvoř mutovatelnou kopii pro úpravy
                    originalBitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
                    binding.ivImage.setImageBitmap(originalBitmap)
                }
            }

        binding.btnTakeImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btnApply.setOnClickListener {
            applyBrightnessAndContrast()
        }

        binding.btnGrayscale.setOnClickListener {
            applyGrayscale()
        }

        setupSeekBars()
    }

    private fun setupSeekBars() {
        binding.seekBarBrightness.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val brightnessPercent = progress - 100
                binding.tvBrightnessValue.text = "Jas: $brightnessPercent%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarContrast.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val contrastValue = progress / 50f // 0–2.0
                binding.tvContrastValue.text = "Kontrast: ${"%.2f".format(contrastValue)}x"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun applyBrightnessAndContrast() {
        originalBitmap?.let { bitmap ->
            val brightnessValue = binding.seekBarBrightness.progress - 100
            val contrastValue = binding.seekBarContrast.progress / 50f

            val adjustedBitmap = adjustBitmapBrightnessContrast(bitmap, brightnessValue.toFloat(), contrastValue)
            binding.ivImage.setImageBitmap(adjustedBitmap)
        }
    }

    private fun adjustBitmapBrightnessContrast(bitmap: Bitmap, brightness: Float, contrast: Float): Bitmap {
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

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    private fun applyGrayscale() {
        originalBitmap?.let { bitmap ->
            val grayBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(grayBitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            val grayMatrix = ColorMatrix()
            grayMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(grayMatrix)

            paint.colorFilter = filter
            canvas.drawBitmap(bitmap, 0f, 0f, paint)

            binding.ivImage.setImageBitmap(grayBitmap)
        }
    }
}