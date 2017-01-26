package rustycage;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

/**
 * Created by breh on 1/23/17.
 */

public final class TextNode extends BaseNode {

    private float x,y;
    private TextPaint textPaint;
    private CharSequence text;

    private TextNode(@Nullable CharSequence text, float x, float y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public void setTextPaint(@Nullable TextPaint textPaint) {
        this.textPaint = textPaint;
        markDirty();
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
        markDirty();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        markDirty();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        markDirty();
    }

    @Override
    public float getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getLeft() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getRight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getTop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getBottom() {
        throw new UnsupportedOperationException();
    }

// builder

    public static TextNode.Builder create(@Nullable CharSequence text) {
        return new TextNode.Builder(text, 0, 0);
    }

    public static TextNode.Builder create(@Nullable CharSequence text, float x, float y) {
        return new TextNode.Builder(text, x, y);
    }


    public static class Builder extends BaseNode.Builder<Builder,TextNode> {

        private Builder(@Nullable CharSequence text, float x, float y) {
            super(new TextNode(text, x, y));
        }

        public Builder textPaint(@NonNull TextPaint paint) {
            getNode().setTextPaint(paint);
            return getBuilder();
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
