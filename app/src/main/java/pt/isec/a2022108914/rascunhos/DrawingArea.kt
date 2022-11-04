package pt.isec.a2022108914.rascunhos

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class DrawingArea @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), GestureDetector.OnGestureListener {

    private var color: Int = Color.WHITE
    private var imageFile: String? = null

    val paint = Paint(Paint.DITHER_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 4.0f
        style = Paint.Style.FILL_AND_STROKE
    }

    private var gestureDetector = GestureDetector(context, this)

    var lineColor: Int = Color.BLACK
        set(value) {
            field = value
            drawing.add(Line(Path(), value))
        }

    private val drawing = arrayListOf<Line>(
        Line(Path(), lineColor)
    )

    private val path: Path
        get() = drawing.last().path

    constructor(context: Context, color: Int): this(context) {
        this.color = color
        setBackgroundColor(color)
    }

    constructor(context: Context, imageFile: String) : this(context) {
        this.imageFile = imageFile
        setPic(this, imageFile)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for(line in drawing) {
            canvas?.drawPath(line.path, paint.apply { color = line.color })
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageFile?.let { setPic(this, it) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "View clicked")
        if(gestureDetector.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    companion object {
        const val TAG = "DrawingArea"
    }

    override fun onDown(e: MotionEvent): Boolean {
        Log.d(TAG, "DrawingArea::onDown")
        path.moveTo(e.x, e.y)
        invalidate()
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        Log.d(TAG, "DrawingArea::onShowPress")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        Log.d(TAG, "DrawingArea::onSingleTapUp")
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, p2: Float, p3: Float): Boolean {
        Log.i(TAG, "DrawingArea::onScroll: ${e1.x} ${e2.x}")
        path.lineTo(e2.x, e2.y)
        path.moveTo(e2.x, e2.y)
        invalidate()
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        Log.d(TAG, "DrawingArea::onLongPress")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(TAG, "DrawingArea::onFling")
        return false
    }

    data class Line(val path: Path, val color: Int)

}