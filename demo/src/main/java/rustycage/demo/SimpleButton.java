package rustycage.demo;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import rustycage.SgCustomNode;
import rustycage.SgGroup;
import rustycage.SgNode;
import rustycage.SgRectangle;
import rustycage.SgText;
import rustycage.animation.FloatPropertyTransition;
import rustycage.animation.GroupTransition;
import rustycage.animation.ScaleTransition;
import rustycage.animation.TranslationTransition;
import rustycage.event.TouchEvent;
import rustycage.event.TouchEventListener;
import rustycage.util.PaintBuilder;

/**
 * Created by breh on 3/7/17.
 */

public class SimpleButton extends SgCustomNode {

    private static final String TAG = "SimpleButton";


    private String text;
    private float width;
    private float height;
    private int bgColor = Color.DKGRAY;
    private int outlineColor = Color.GRAY;
    private int textColor = Color.WHITE;
    private Paint bgPaint;
    private Paint outlinePaint;
    private Paint textPaint;

    private SgText textNode;


    private SimpleButton(@Nullable String text, float width, float height) {
        this.text = text;
        this.width = width;
        this.height = height;
    }


    @NonNull
    @Override
    protected SgNode createNode() {
        bgPaint = PaintBuilder.create().color(bgColor).style(Paint.Style.FILL).build();
        outlinePaint = PaintBuilder.create().color(outlineColor).style(Paint.Style.STROKE)
                .strokeWidth(10f).strokeJoin(Paint.Join.ROUND).build();
        textPaint = PaintBuilder.create().color(textColor).textAlign(Paint.Align.CENTER)
                .textSize(80f).build();
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        //float textHeight = fm.bottom - fm.top + fm.leading;
        float textY = height/2f + fm.descent;
        return SgGroup.create()
                .add(SgRectangle.createWithSize(0,0,width,height,10, 10).paint(bgPaint))
                .add(SgRectangle.createWithSize(0,0,width,height,10, 10).paint(outlinePaint))
                .add(textNode = SgText.create(text,width/2, textY).textPaint(textPaint).build())
                .onTouchDown(new TouchEventListener() {
                    @Override
                    public boolean onTouchEvent(@NonNull TouchEvent touchEvent) {
                        Log.d(TAG,"button touched!!!: "+text+" lx: "+touchEvent.getLocalX());
                        float targetX = 0f;
                        if (touchEvent.getLocalX() < width / 2) {
                            targetX = width / 4;
                        } else {
                            targetX = -width / 4;
                        }
                        GroupTransition.createParallel().duration(300)
                                .add(TranslationTransition.create(textNode).toX(targetX))
                                //.add(ScaleTransition.create(textNode).to(1.5f))
                                .add(FloatPropertyTransition.create(textNode,"scale").to(1.5f))
                                .start();
                        return true;
                    }
                })
                .onTouchUpTransition(GroupTransition.createParallel().duration(300)
                        .add(TranslationTransition.create(textNode).toX(0))
                        .add(ScaleTransition.create(textNode).to(1)))
                .build();
    }


    public static SimpleButton.Builder create(String text, float width, float height) {
        return new SimpleButton.Builder(text, width, height);
    }

    public static class Builder extends SgNode.Builder<SimpleButton.Builder, SimpleButton> {
        private Builder(@Nullable String text, float width, float height) {
            super(new SimpleButton(text, width, height));
        }

        public Builder bgColor(int color) {
            getNode().bgColor = color;
            return getBuilder();
        }

        public Builder outlineColor(int color) {
            getNode().outlineColor = color;
            return getBuilder();
        }

        public Builder textColor(int color) {
            getNode().textColor = color;
            return getBuilder();
        }
    }
}
