package com.rohan.pixd.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rohan.pixd.R
import com.rohan.pixd.ui.sticker.StickerBottomSheet
import com.rohan.pixd.ui.text.TextBottomSheet
import com.rohan.pixd.utils.FilterHelper
import com.rohan.pixd.utils.MeasureCropView
import com.rohan.pixd.utils.MoveableStickerForegroundView
import com.rohan.pixd.utils.PermissionHelper
import com.rohan.pixd.utils.StickerHelper
import com.rohan.pixd.utils.MovableTextviewContainer


class MainActivity : AppCompatActivity(), MeasureCropView.OnMeasureChangeListener,
    StickerBottomSheet.BottomSheetListener, TextBottomSheet.BottomSheetListener,
    MovableTextviewContainer.TextClickListener {

    private val brightness: String = "brightness"
    private val crop: String = "crop"
    private val filter: String = "filter"
    private val sticker: String = "sticker"
    private val text: String = "text"
    private var currentNav: String? = null
    private var somethingChanged: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showBrightness: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showCrop: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showFilter: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showSticker: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var showText: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var filterNumber: Int = 0;
    private var realBitmap: Bitmap? = null
    private var workingBitmap: Bitmap? = null
    private lateinit var imageView: ImageView
    private lateinit var featureFilter: ImageView
    private lateinit var loadButton: Button
    private lateinit var applyButton: Button
    private lateinit var filterButton: Button
    private lateinit var featureBrightness: ImageView
    private lateinit var featureCrop: ImageView
    private lateinit var featureSticker: ImageView
    private lateinit var featureText: ImageView
    private lateinit var seekBarBrightness: SeekBar
    private lateinit var seekBarText: SeekBar
    private lateinit var frameControllerBrightness: FrameLayout
    private lateinit var frameControllerText: FrameLayout
    private lateinit var frameProgress: FrameLayout
    private var seekBarValue: Int? = 0
    private var measureBoxViewx: MeasureCropView? = null
    private var editzone: RelativeLayout? = null
    private var frame: ConstraintLayout? = null
    private var stickerBs: StickerBottomSheet? = null
    private var textBs: TextBottomSheet? = null
    private var moveableStickerForegroundView: MoveableStickerForegroundView? = null
    private var stickerX: Int = 0;
    private var stickerY: Int = 0;
    private var foregroundStickerBitmap: Bitmap? = null;
    private var movableTextviewContainer: MovableTextviewContainer? = null;

    companion object{
        var stickerWidth = 500;
        var stickerHeight = 500;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listener()

        imageView = findViewById(R.id.imageView)
        loadButton = findViewById(R.id.loadButton)
        applyButton = findViewById(R.id.applyButton)
        //filterButton = findViewById(R.id.filterButton)
        featureFilter = findViewById(R.id.imageFilter)
        featureBrightness = findViewById(R.id.imageBrightness)
        featureCrop = findViewById(R.id.imageCrop)
        featureSticker = findViewById(R.id.imageSticker)
        featureText = findViewById(R.id.imageText)
        seekBarBrightness = findViewById(R.id.seekBarBrightness)
        seekBarText = findViewById(R.id.seekBarText)
        frameControllerBrightness = findViewById(R.id.frameControllerBrightness)
        frameControllerText = findViewById(R.id.frameControllerText)
        measureBoxViewx = findViewById(R.id.measureCropView)
        editzone = findViewById(R.id.editzone)
        frame = findViewById(R.id.frame)
        moveableStickerForegroundView = findViewById(R.id.moveableForegroundView)
        frameProgress = findViewById(R.id.frameProgress)
        movableTextviewContainer = findViewById(R.id.movableTextviewContainer)

        loadButton.setOnClickListener {
            if (PermissionHelper.checkPermissions(this)) {
                openGallery()
            } else {
                PermissionHelper.requestStoragePermission(this)
            }
        }

        featureFilter.setOnClickListener {
            applyChanges()
        }

        applyButton.setOnClickListener {
            applyChanges()
        }

        seekBarBrightness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValue = seekBar!!.progress
                val bitmap = workingBitmap
                if(bitmap != null && seekBarValue?.toFloat() != null) {
                    val adjustedBitmap = adjustBrightness(bitmap, seekBarValue!!.toFloat());
                    imageView.setImageBitmap(adjustedBitmap)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        seekBarText.progress = MovableTextviewContainer.Companion.defaultTextSize
        seekBarText.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                movableTextviewContainer?.setTextSize(this@MainActivity, progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        featureBrightness.setOnClickListener {
            changingNavigation(brightness)
        }

        featureCrop.setOnClickListener {
            changingNavigation(crop)
        }

        featureFilter.setOnClickListener {
            changingNavigation(filter)
        }

        featureSticker.setOnClickListener {
            changingNavigation(sticker)
        }

        featureText.setOnClickListener {
            changingNavigation(text)
        }

        measureBoxViewx?.setMeasureChangeListener(this);

        movableTextviewContainer?.setTextClickListner(this);

    }


    private fun resetThings() {
        showBrightness.postValue(false)
        showCrop.postValue(false)
        showFilter.postValue(false)
        showSticker.postValue(false)
        showText.postValue(false)
        imageView.setImageBitmap(workingBitmap)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PermissionHelper.READ_STORAGE_PERMISSION_REQUEST_CODE)
    }

    private fun applyChanges() {
        //val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        val bitmap = workingBitmap
        when(currentNav){
            brightness ->{
                workingBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
                somethingChanged.postValue(false)
                resetThings()
            }
            filter ->{
                if (bitmap != null ) {
                    workingBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
                    somethingChanged.postValue(false)
                    resetThings()
                    //saveImageToGallery(filteredBitmap)
                } else {
                    Toast.makeText(this, "No image loaded", Toast.LENGTH_SHORT).show()
                }
            }
            crop ->{
                val metrics = resources.displayMetrics
                val screenWidth = metrics.widthPixels
                val rect = measureBoxViewx?.getCropCoordinates()

                System.out.println("Screen Width: " + screenWidth);
                System.out.println("Bitmap Width: " + workingBitmap?.width);
                System.out.println("Bitmap Height: " + workingBitmap?.height);
                System.out.println("Rect Right: " + rect?.right);
                System.out.println("Rect Width: " + rect?.width());
                System.out.println("Rect Height: " + rect?.height());
                System.out.println("Rect Top: " + rect?.top);

                if(rect != null && workingBitmap != null) {
                    var croppedBitmap = cropBitmap(
                        workingBitmap,
                        rect.left.toInt(),
                        rect.top.toInt(),
                        rect.width().toInt(),
                        rect.height().toInt()
                    );
                    croppedBitmap?.let {
                        croppedBitmap = upscaleBitmapToFitScreenWidth(it, this)
                    }
                    workingBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
                    somethingChanged.postValue(false)
                    resetThings()
                }
            }
            sticker -> {
                workingBitmap?.let {
                    foregroundStickerBitmap?.let {
                        workingBitmap = StickerHelper().combineBitmaps(
                            workingBitmap!!, foregroundStickerBitmap!!, stickerX, stickerY)
                        loader(true);
                        Handler().postDelayed({
                            loader(false);
                            imageView.setImageBitmap(workingBitmap)
                            somethingChanged.postValue(false)
                            resetThings()
                        },2000)
                    }
                }
            }
            text -> {
                workingBitmap?.let {
                    workingBitmap = movableTextviewContainer?.getBitmapWithText(workingBitmap!!)
                    imageView.setImageBitmap(workingBitmap)
                    somethingChanged.postValue(false)
                    resetThings()
                }
            }
        }

    }

    fun loader(show: Boolean){
        if(show){
            frameProgress.visibility = View.VISIBLE
        } else {
            frameProgress.visibility = View.GONE
        }
    }

    private fun applyFilterEffect(bitmap: Bitmap): Bitmap {
        somethingChanged.postValue(true)
        if(filterNumber == 8){
            print("original")
            filterNumber = 0;
        } else {
            filterNumber += 1;
        }
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = FilterHelper().getFilter(filterNumber);//filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscaleBitmap
    }


    fun adjustBrightness(bitmap: Bitmap, brightness: Float): Bitmap? {
        somethingChanged.postValue(true)
        val adjustedBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return adjustedBitmap
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionHelper.READ_STORAGE_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap?.let {
                    //resizeBitmap(it)
                    workingBitmap = upscaleBitmapToFitScreenWidth(it, this)
                    realBitmap = workingBitmap
                    imageView.setImageBitmap(workingBitmap)
                }
            }
        }
    }


    private fun listener() {
        somethingChanged.observe(this, Observer {
            if(it){
                applyButton.visibility = View.VISIBLE
                loadButton.visibility = View.GONE
            } else {
                applyButton.visibility = View.GONE
                loadButton.visibility = View.VISIBLE
            }
        })

        showBrightness.observe(this, Observer {
            if(it){
                frameControllerBrightness.visibility = View.VISIBLE;
            } else {
                frameControllerBrightness.visibility = View.GONE;
            }
        })

        showCrop.observe(this, Observer {
            if(it){
                workingBitmap?.let {
                    //resizeBitmap(it)
                    workingBitmap = upscaleBitmapToFitScreenWidth(it, this)
                }
                loader(true);
                Handler().postDelayed({
                    loader(false);
                    measureBoxViewx?.setDefaultBoxSize(
                        imageView.left.toFloat(),
                        imageView.top.toFloat(),
                        imageView.right.toFloat(),
                        imageView.bottom.toFloat()
                    )
                    measureBoxViewx?.visibility = View.VISIBLE
                },1500)
            } else {
                measureBoxViewx?.visibility = View.GONE
            }
        })

        showFilter.observe(this, Observer {
            if(it){
                //applyChanges()
                workingBitmap?.let {
                    val filteredBitmap = applyFilterEffect(workingBitmap!!);
                    imageView.setImageBitmap(filteredBitmap)
                }
            } else {

            }
        })

        showSticker.observe(this, Observer {
            if(it){
                stickerBs = StickerBottomSheet();
                stickerBs?.show(supportFragmentManager, "")
            } else {
                moveableStickerForegroundView?.visibility = View.GONE
            }
        })

        showText.observe(this, Observer {
            if(it){
                textBs = TextBottomSheet(
                    string = "",
                    fonts = null,
                    color = null,
                    bgcolor = null
                );
                textBs?.show(supportFragmentManager, "")
                movableTextviewContainer?.visibility = View.VISIBLE
                frameControllerText.visibility = View.VISIBLE;
            } else {
                moveableStickerForegroundView?.visibility = View.GONE
                movableTextviewContainer?.visibility = View.GONE
                frameControllerText.visibility = View.GONE;
            }
        })
    }

    private fun changingNavigation(value: String){
        if(workingBitmap != null) {
            resetThings()
            when (value) {
                brightness -> {
                    currentNav = brightness;
                    showBrightness.postValue(true)
                }

                crop -> {
                    currentNav = crop;
                    showCrop.postValue(true)
                }

                filter -> {
                    currentNav = filter;
                    showFilter.postValue(true)
                }

                sticker -> {
                    currentNav = sticker;
                    showSticker.postValue(true)
                }

                text -> {
                    currentNav = text;
                    showText.postValue(true)
                }
            }
        } else {
            Toast.makeText(this, "Please add a image to edit", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMeasureChanged() {
        applyButton.visibility = View.VISIBLE
    }

    fun cropBitmap(originalBitmap: Bitmap?, left: Int, top: Int, width: Int, height: Int): Bitmap? {
        return Bitmap.createBitmap(originalBitmap!!, left, top, width, height)
    }

    fun upscaleBitmapToFitScreenWidth(
        originalBitmap: Bitmap,
        context: Context
    ) : Bitmap{
        // Get the screen width
        val displayMetrics = DisplayMetrics()
        val windowManager =
            context.getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val availableHeight = frame?.height;

        // Calculate the scaling factor
        val scale = screenWidth.toFloat() / originalBitmap.width

        // Calculate the new height based on the aspect ratio
        var newHeight = Math.round(originalBitmap.height * scale)

        // Upscale the bitmap to fit the screen width
        workingBitmap = Bitmap.createScaledBitmap(originalBitmap, screenWidth, newHeight, true)

        imageView.setImageBitmap(workingBitmap)

        if(newHeight>(availableHeight?:0)){
            editzone?.layoutParams?.height = availableHeight
        } else {
            editzone?.layoutParams?.height = workingBitmap?.height
        }

        return workingBitmap!!
    }

    override fun onStickerDataReceived(stickerDrawable: Int) {
        stickerBs?.dismissAllowingStateLoss();
        moveableStickerForegroundView?.visibility = View.VISIBLE
        somethingChanged.postValue(true)
        foregroundStickerBitmap = getBitmapFromDrawable(this, stickerDrawable)

        workingBitmap?.let {
            foregroundStickerBitmap?.let {
                moveableStickerForegroundView?.initBitmaps(workingBitmap!!, foregroundStickerBitmap!!,
                    object: MoveableStickerForegroundView.OnMovementDoneListener{
                    override fun onMovementChanged(x: Int, y: Int) {
                        stickerX = x;
                        stickerY = y;
                    }
                })
            }
        }
    }

    fun getBitmapFromDrawable(context: Context, drawableResId: Int): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableResId)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable != null) {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        return null
    }

    override fun onTextDataReceived(string: String, font: Int?, color: Int?, bgColor: Int?) {
        somethingChanged.postValue(true)
        movableTextviewContainer?.setTextViewText(string)
        font?.let {
            movableTextviewContainer?.setFontFamily(this, font)
        }
        color?.let {
            movableTextviewContainer?.setColor(this, color)
        }
        movableTextviewContainer?.addBackgroundColor(bgColor)
        textBs?.dismissAllowingStateLoss()

    }

    override fun onTextClicked() {
        textBs = TextBottomSheet(
            string = movableTextviewContainer?.getTextViewText(),
            fonts = movableTextviewContainer?.getFontFamily(),
            color = movableTextviewContainer?.getColor(),
            bgcolor = movableTextviewContainer?.getBackgroundColor()
        );
        textBs?.show(supportFragmentManager, "")
    }


}

