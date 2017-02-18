package rustycage.demo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import rustycage.SgCustomNode;
import rustycage.SgGroup;
import rustycage.SgNode;
import rustycage.SgPath;
import rustycage.animation.FadeTransition;
import rustycage.event.TouchEventListener;
import rustycage.util.PaintBuilder;

/**
 * Created by breh on 2/17/17.
 */

public class CustomNodeTest extends SgCustomNode {

    private CustomNodeTest() {}

    private SgPath.Builder createStrip(float r1, float r2) {
        return SgPath.create().arcTo(-r1,-r1,r1,r1,180,90)/*.rLineTo(0,40)*/
                .arcTo(-r2,-r2,r2,r2,270,-90).close()
                .pivot(0,0);
    }

    @NonNull
    @Override
    protected SgNode createNode() {
        final SgNode outerStrip = createStrip(580,400).paint(PaintBuilder.create().color(Color.RED)).opacity(0f).build();
        final SgNode middleStrip = createStrip(380, 200).paint(PaintBuilder.create().color(Color.BLUE)).opacity(0.5f).build();
        final SgNode innerStrip = createStrip(180,0).paint(PaintBuilder.create().color(Color.GREEN)).build();

        innerStrip.setOnTouchEventListener(new TouchEventListener() {
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent touchEvent, float localX, float localY, boolean isCapturePhase) {
                int action = touchEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    FadeTransition.create(middleStrip).to(1f).duration(300).start();
                } else if (action == MotionEvent.ACTION_UP) {
                    FadeTransition.create(middleStrip).to(0.5f).duration(300).start();
                }
                return true;
            }
        }, true);

        middleStrip.setOnTouchEventListener(new TouchEventListener() {
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent touchEvent, float localX, float localY, boolean isCapturePhase) {
                int action = touchEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    FadeTransition.create(outerStrip).to(1f).duration(300).start();
                } else if (action == MotionEvent.ACTION_UP) {
                    FadeTransition.create(outerStrip).to(0f).duration(300).start();
                }
                return true;
            }
        }, true);

        return SgGroup.create().add(outerStrip, middleStrip, innerStrip).build();

    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SgNode.Builder<Builder, CustomNodeTest> {
        private Builder() {
            super(new CustomNodeTest());
        }
    }

}
