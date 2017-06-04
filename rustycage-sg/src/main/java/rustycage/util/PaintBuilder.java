package rustycage.util;

import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

import rustycage.Attribute;
import rustycage.PaintAttribute;

/**
 * Created by breh on 2/17/17.
 */

public final class PaintBuilder implements Attribute.AttributeBuilder<PaintAttribute> {

    private Paint paint;

    private PaintBuilder(@NonNull Paint paint) {
        this.paint = paint;
    }

    public static PaintBuilder create() {
        return new PaintBuilder(new Paint());
    }

    public static PaintBuilder createText() {
        return new PaintBuilder(new TextPaint());
    }

    public static PaintBuilder createFromPaint(@NonNull Paint paint) {
        return new PaintBuilder(new Paint(paint));
    }

    public PaintBuilder from(@NonNull Paint paint) {
        this.paint.set(paint);
        return this;
    }

    public PaintBuilder alpha(int a) {
        paint.setAlpha(a);
        return this;
    }


    public PaintBuilder color(@ColorInt int color) {
        paint.setColor(color);
        return this;
    }

    public PaintBuilder argb(int a, int r, int g, int b) {
        paint.setARGB(a, r, g, b);
        return this;
    }

    public PaintBuilder strokeWidth(float width) {
        paint.setStrokeWidth(width);
        return this;

    }

    public PaintBuilder strokeCap(@Nullable Paint.Cap cap) {
        paint.setStrokeCap(cap);
        return this;
    }

    public PaintBuilder strokeJoin(@Nullable Paint.Join join) {
        paint.setStrokeJoin(join);
        return this;
    }

    public PaintBuilder strokeMiter(float miter) {
        paint.setStrokeMiter(miter);
        return this;
    }


    public PaintBuilder style(@Nullable Paint.Style style) {
        paint.setStyle(style);
        return this;
    }


    public PaintBuilder shader(@Nullable Shader shader) {
        paint.setShader(shader);
        return this;
    }


    public PaintBuilder xfermode(@Nullable Xfermode xfermode) {
        paint.setXfermode(xfermode);
        return this;
    }



    // text
    public PaintBuilder textSize(float size) {
        paint.setTextSize(size);
        return this;
    }

    public PaintBuilder textAlign(@Nullable Paint.Align align) {
        paint.setTextAlign(align);
        return this;
    }

    public PaintBuilder typeface(@Nullable Typeface typeface) {
        paint.setTypeface(typeface);
        return this;
    }

    public PaintBuilder textLetterSpacing(float letterSpacing) {
        paint.setLetterSpacing(letterSpacing);
        return this;
    }

    public PaintBuilder textStrikeThrough(boolean strikeThru) {
        paint.setStrikeThruText(strikeThru);
        return this;
    }

    public PaintBuilder textUnderline(boolean underline) {
        paint.setUnderlineText(underline);
        return this;
    }


    public Paint build() {
        return paint;
    }

    public PaintAttribute buildAttribute() {
        return new PaintAttribute(build());
    }

}
