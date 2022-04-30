package com.rivaldo.screeningtestsuitmedia

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.rivaldo.screeningtestsuitmedia.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val GALLERY = 1
    private val CAMERA = 2
    private var photosProfilePath: String? = null
    private var imageBitmap : Bitmap? = null

    companion object{
        private val IMAGE_DIRECTORY = "/screeningtestsuitmedia"
        private val PHOTOS_PROFILE = "photos_profile"
        private val NAMES = "names"
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initClickListener()
        loadPhotosIfExist()
    }


    private fun loadPhotosIfExist(){
            val image = CommonUtil.getSharedPrefs(this, PHOTOS_PROFILE)
            if (image != null) {
                if (image.isNotEmpty()){
                    binding.circleImageViewMain.setImageBitmap(BitmapFactory.decodeFile(image))
                }
            }
    }

    fun String.isValidName(): Boolean {
        return this.matches(Regex("[a-zA-Z ]+"))
    }

    fun TextInputEditText.validate(message: String, predicate: (String) -> Boolean): Boolean {
        val text = this.text.toString()
        return if (predicate(text)) {
            this.error = null
            true
        } else {
            this.error = message
            false
        }
    }



    private fun initClickListener() {

        binding.btnCheck.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.circleImageViewMain.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnCheck -> {
                val str = binding.editTextPalindrome.text.toString().lowercase()
                if (str.isEmpty()) {
                    Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show()
                } else {
                    val isPalindrome = checkIsPalindrome(str)
                    if (isPalindrome) {
                        Toast.makeText(this, "Palindrome", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Not Palindrome", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.btnNext -> {
                if (binding.editTextName.validate("Please enter a valid name", { it.isValidName() }) &&
                    binding.editTextPalindrome.validate("Please enter a word",{it.isNotEmpty()})) {
                        CommonUtil.saveSharedPrefs(this, NAMES, binding.editTextName.text.toString())
                    imageBitmap?.let { saveImageOnly(it) }
                    Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Screen2Activity::class.java)
                    intent.putExtra(CommonUtil.NAMES, binding.editTextName.text.toString())
                    startActivity(intent)
                    //next activity
                }

            }
            R.id.circleImageViewMain -> {
                showPictureDialog()
            }
        }
    }

    private fun checkIsPalindrome(str: String): Boolean {
        var isPalindrome = true
        val strLength = str.length
        for (i in 0 until strLength / 2) {
            if (str[i] != str[strLength - i - 1]) {
                isPalindrome = false
                break
            }
        }
        return isPalindrome
    }

    private fun showPictureDialog(){
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()

    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    imageBitmap = bitmap
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    binding.circleImageViewMain.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            binding.circleImageViewMain.setImageBitmap(thumbnail)
            imageBitmap = thumbnail
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImageOnly(bitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        // have the object build the directory structure, if needed.
        var photosDirectory = File(this.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath)
            if (photosDirectory.exists().not()){
                photosDirectory.mkdirs()
            }
        try {
            Log.d("heel", photosDirectory.toString())
            val f = File(photosDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            CommonUtil.saveSharedPrefs(this, PHOTOS_PROFILE, f.absolutePath)
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    fun saveSharedPrefs(key: String, value: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }
}