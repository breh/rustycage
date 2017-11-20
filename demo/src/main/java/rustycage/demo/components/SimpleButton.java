package rustycage.demo.components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import rustycage.event.SgEvent;
import rustycage.event.TouchEvent;
import rustycage.event.TouchEventListener;
import rustycage.util.PaintBuilder;

/**
 * Created by breh on 3/7/17.
 */

public final class SimpleButton extends SgCustomNode {

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
    private SgNode outlineNode;


    private SimpleButton(@Nullable String text, float width, float height) {
        this.text = text;
        this.width = width;
        this.height = height;
    }


    public String getText() {
        return text;
    }

    private static void animatePaint(final @NonNull SgNode node, @NonNull Paint paint, int toColor, int duration) {
        ObjectAnimator colorAnimator = ObjectAnimator.ofArgb(paint, "color", paint.getColor(), toColor);
        colorAnimator.setDuration(duration);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                node.invalidate();
            }
        });

        colorAnimator.start();
    }

    @NonNull
    @Override
    protected SgNode createNode() {
        bgPaint = PaintBuilder.create().color(bgColor).style(Paint.Style.FILL).build();
        outlinePaint = PaintBuilder.create().color(outlineColor).style(Paint.Style.STROKE)
                .strokeWidth(10f).strokeJoin(Paint.Join.ROUND).build();
        textPaint = PaintBuilder.create().color(textColor).textAlign(Paint.Align.CENTER)
                .textSize(70f).build();
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        //float textHeight = fm.bottom - fm.top + fm.leading;
        float textY = height/2f + fm.descent;
        return SgGroup.create()
                .add(SgRectangle.createWithSize(0,0,width,height,10, 10).paint(bgPaint))
                .add(outlineNode = SgRectangle.createWithSize(0,0,width,height,10, 10).paint(outlinePaint).build())
                .add(textNode = SgText.create(text,width/2, textY).paint(textPaint).build())
                .onTouchDown(new TouchEventListener() {
                    @Override
                    public void onEvent(@NonNull TouchEvent touchEvent, @NonNull SgNode currentNode, boolean isCapturePhase) {
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

                        animatePaint(outlineNode, outlinePaint, Color.RED, 300);

                        touchEvent.consume();


                    }
                })
                .onTouchUpTransition(GroupTransition.createParallel().duration(300)
                        .add(TranslationTransition.create(textNode).toX(0))
                        .add(ScaleTransition.create(textNode).to(1)))
                .onTouchUp(new TouchEventListener() {
                    public void onEvent(@NonNull TouchEvent event, @NonNull SgNode currentNode, boolean isCapturePhase) {
                        animatePaint(outlineNode, outlinePaint, Color.GRAY, 300);
                        // now dispatch button clicked event
                        SimpleButton.this.dispatchEvent(new SimpleButtonClickedEvent(SimpleButton.this));
                    }
                })
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


    public static class SimpleButtonClickedEvent extends SgEvent {

        private final SimpleButton button;

        private SimpleButtonClickedEvent(SimpleButton button) {
            super(button);
            this.button = button;
        }

        public SimpleButton getButton() {
            return button;
        }

    }
}
