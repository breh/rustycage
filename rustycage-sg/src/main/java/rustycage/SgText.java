package rustycage;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

import rustycage.util.PaintBuilder;

/**
 * A node representing a single line of text
 *
 * Created by breh on 1/23/17.
 */
public final class SgText extends SgShape {

    private static final String TAG = "SgText";

    private static final TextPaint DEFAULT_TEXT_PAINT = new TextPaint();

    private float x,y;
    private CharSequence text;

    private SgText(@Nullable CharSequence text, float x, float y) {
        this.text = text;
        this.x = x;
        this.y = y;
        setPaint(DEFAULT_TEXT_PAINT);
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
        invalidateLocalBounds();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        invalidateLocalBounds();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        invalidateLocalBounds();
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        if (text != null) {
            Rect textBounds = new Rect();
            Paint textPaint = getPaint();
            textPaint.getTextBounds(text.toString(), 0, text.length(), textBounds);
            Paint.Align align = textPaint.getTextAlign();
            switch (align) {
                case LEFT:
                    bounds[0] = x + textBounds.left;
                    bounds[2] = x + textBounds.right;
                    break;
                case CENTER:
                    float halfTextWidth = (textBounds.right - textBounds.left) / 2f;
                    bounds[0] = x - halfTextWidth;
                    bounds[2] = x + halfTextWidth;
                    break;
                case RIGHT:
                    bounds[0] = x - textBounds.left;
                    bounds[2] = x - textBounds.right;
                    break;
            }
            bounds[1] = y + textBounds.top;
            bounds[3] = y + textBounds.bottom;
        } else {
            bounds[0] = 0;
            bounds[1] = 0;
            bounds[2] = 0;
            bounds[3] = 0;
        }
    }

// builder

    public static SgText.Builder create(@Nullable CharSequence text) {
        return new SgText.Builder(text, 0, 0);
    }

    public static SgText.Builder create(@Nullable CharSequence text, float x, float y) {
        return new SgText.Builder(text, x, y);
    }


    public static class Builder extends  SgShape.Builder<SgText.Builder, SgText> {

        private Builder(@Nullable CharSequence text, float x, float y) {
            super(new SgText(text, x, y));
        }

        public Builder x(float x) {
            getNode().setX(x);
            return getBuilder();
        }

        public Builder y(float y) {
            getNode().setY(y);
            return getBuilder();
        }

        public Builder xy(float x, float y) {
            getNode().setX(x);
            getNode().setY(y);
            return getBuilder();
        }

    }

}
