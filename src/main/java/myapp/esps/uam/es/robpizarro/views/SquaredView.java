package myapp.esps.uam.es.robpizarro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import es.uam.eps.multij.Tablero;
import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.TableroOthello;

/**
 * Created by e268930 on 8/03/17.
 */

public class SquaredView extends View {
    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Tablero tablero;


    public SquaredView(Context context, AttributeSet attrs, Tablero tab, int size) {
        super(context, attrs);
        tablero=tab;
        this.size =size;
        init();
    }
    private void init() {
        backgroundPaint.setColor(getResources().getColor(R.color.Blue));
        linePaint.setStrokeWidth(2);
    }

    public SquaredView(Context context, Tablero tab, int size) {
        super(context);
        this.size =size;
        tablero=tab;
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        widthOfTile = w / size;
        heightOfTile = h / size;
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f;
        else
            radio = heightOfTile * 0.3f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 500;
        String wMode, hMode;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        drawCircles(canvas, linePaint);
    }

    private void drawCircles(Canvas canvas, Paint paint) {
        float centerRaw, centerColumn;
        for (int i = 0; i < size; i++) {
            int pos = size - i - 1;
            centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
            for (int j = 0; j < size; j++) {
                centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                setPaintColor(paint, i, j);
                canvas.drawCircle(centerColumn, centerRaw, radio, paint);
            }
        }
    }

    private void setPaintColor(Paint paint, int i, int j) {
        if (((TableroOthello) tablero).getTablero(i,j) == TableroOthello.jugador1)
            paint.setColor(getResources().getColor(R.color.Green));
        else if (((TableroOthello) tablero).getTablero(i,j) == TableroOthello.libre)
            paint.setColor(getResources().getColor(R.color.Blue));
        else
            paint.setColor(getResources().getColor(R.color.Red));
    }
}
