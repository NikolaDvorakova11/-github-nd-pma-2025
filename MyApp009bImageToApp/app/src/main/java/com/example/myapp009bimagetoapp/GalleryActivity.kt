package com.example.myapp009bimagetoapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp009bimagetoapp.databinding.ActivityGalleryBinding
import com.example.myapp009bimagetoapp.databinding.ItemImageBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class ImageItem(
    val id: String,
    val name: String,
    val path: String,
    val timestamp: Long
)

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter: ImageAdapter
    private val images = mutableListOf<ImageItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadImages()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = ImageAdapter(
            images,
            onEdit = { image -> editImage(image) },
            onDelete = { image -> deleteImage(image) }
        )

        binding.rvImages.layoutManager = GridLayoutManager(this, 2)
        binding.rvImages.adapter = adapter
    }

    private fun loadImages() {
        images.clear()

        val prefs = getSharedPreferences("image_metadata", MODE_PRIVATE)
        val ids = prefs.getStringSet("image_ids", emptySet()) ?: emptySet()

        ids.forEach { id ->
            val name = prefs.getString("$id-name", "") ?: ""
            val path = prefs.getString("$id-path", "") ?: ""
            val timestamp = prefs.getLong("$id-timestamp", 0L)

            if (File(path).exists()) {
                images.add(ImageItem(id, name, path, timestamp))
            }
        }

        images.sortByDescending { it.timestamp }

        adapter.notifyDataSetChanged()
        binding.tvEmptyMessage.visibility = if (images.isEmpty()) View.VISIBLE else View.GONE
        binding.tvImageCount.text = "Celkem obrázků: ${images.size}"
    }

    private fun editImage(image: ImageItem) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("image_path", image.path)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun deleteImage(image: ImageItem) {
        AlertDialog.Builder(this)
            .setTitle("Smazat obrázek")
            .setMessage("Opravdu chcete smazat ${image.name}?")
            .setPositiveButton("Smazat") { _, _ ->

                val file = File(image.path)
                if (file.exists()) file.delete()

                val prefs = getSharedPreferences("image_metadata", MODE_PRIVATE)
                val editor = prefs.edit()

                editor.remove("${image.id}-name")
                editor.remove("${image.id}-path")
                editor.remove("${image.id}-timestamp")

                val ids = prefs.getStringSet("image_ids", mutableSetOf())
                    ?.toMutableSet() ?: mutableSetOf()

                ids.remove(image.id)

                editor.putStringSet("image_ids", ids)
                editor.apply()

                loadImages()

                Toast.makeText(this, "Obrázek byl smazán", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }
}

class ImageAdapter(
    private val images: List<ImageItem>,
    private val onEdit: (ImageItem) -> Unit,
    private val onDelete: (ImageItem) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]

        with(holder.binding) {
            val bitmap = BitmapFactory.decodeFile(image.path)
            if (bitmap != null) ivThumbnail.setImageBitmap(bitmap)

            tvImageName.text = image.name
            tvImageId.text = "ID: ${image.id.take(8)}..."
            tvImageDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(Date(image.timestamp))

            btnEdit.setOnClickListener { onEdit(image) }
            btnDelete.setOnClickListener { onDelete(image) }
        }
    }

    override fun getItemCount() = images.size
}
