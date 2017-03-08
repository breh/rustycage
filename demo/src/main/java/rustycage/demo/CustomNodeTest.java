package rustycage.demo;

import android.graphics.Color;
import android.support.annotation.NonNull;

import rustycage.SgCustomNode;
import rustycage.SgGroup;
import rustycage.SgNode;
import rustycage.SgPath;
import rustycage.animation.OpacityTransition;
import rustycage.animation.GroupTransition;
import rustycage.animation.RotationTransition;
import rustycage.util.PaintBuilder;

/**
 * Created by breh on 2/17/17.
 */

public class CustomNodeTest extends SgCustomNode {

    private static final String TAG = "CustomNodeTest";

    private CustomNodeTest() {}

    private SgPath.Builder createStrip(float r1, float r2) {
        return SgPath.create().arcTo(-r1,-r1,r1,r1,180,90)/*.rLineTo(0,40)*/
                .arcTo(-r2,-r2,r2,r2,270,-90).close()
                .pivot(0,0);
    }

    @NonNull
    @Override
    protected SgNode createNode() {
        SgNode outerStrip = createStrip(580,400).paint(PaintBuilder.create().color(Color.RED)).opacity(0f).r(-90).id("outer").build();
        SgNode middleStrip = createStrip(380, 200).paint(PaintBuilder.create().color(Color.BLUE)).opacity(0.5f).id("middle")
                .onTouchDownTransition(GroupTransition.createParallel(outerStrip)
                        .add(OpacityTransition.create(outerStrip).duration(1000).to(1f))
                        .add(RotationTransition.create(outerStrip).duration(500).from(-90f).to(0f)))
                .onTouchUpTransition(GroupTransition.createParallel(outerStrip).duration(1000)
                        .add(OpacityTransition.create(outerStrip).to(0f))
                        .add(RotationTransition.create(outerStrip).by(270))
                )
                .build();
        SgNode innerStrip = createStrip(180,0).paint(PaintBuilder.create().color(Color.GREEN)).id("inner")
                .onTouchDownTransition(OpacityTransition.create(middleStrip).to(1f).duration(300))
                .onTouchUpTransition(OpacityTransition.create(middleStrip).to(0.5f).duration(300))
                .build();

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
