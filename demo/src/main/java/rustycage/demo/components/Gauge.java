package rustycage.demo.components;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import rustycage.SgCustomNode;
import rustycage.SgEllipse;
import rustycage.SgGroup;
import rustycage.SgLine;
import rustycage.SgNode;
import rustycage.SgText;
import rustycage.animation.RotationTransition;
import rustycage.util.PaintBuilder;

/**
 * Created by breh on 5/24/17.
 */

public class Gauge extends SgCustomNode {

    private static final String TAG = "Gauge";

    private static final float DEFAULT_GAUGE_SIZE = 700;

    private SgNode needle;

    private final int minValue;
    private final int maxValue;
    private final int steps;
    private final float coveredAngle;
    private final float baseAngle;
    private float size = DEFAULT_GAUGE_SIZE;

    public Gauge(int minValue, int maxValue, int steps, float coveredAngle, float baseAngle) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.steps = steps;
        this.coveredAngle = coveredAngle;
        this.baseAngle = baseAngle;
    }


    private float value;

    public float getValue() {
        return value;
    }



    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }



    /**
     * Set value for the needle in the gauge. In the case
     * the needle is animated, the gauge will animate the needle
     * to the given position. Otherwise it just sets it.
     *
     * The value is in the
     * @param value
     */
    public void setValue(float value) {
        this.value = value;
        float angle = getNeedleRotationFromValue();
        // stuff
        if (needle != null) {
            if (needleAnimated) {
                RotationTransition.create(needle).to(angle).start();
            } else {
                needle.setRotation(angle);
            }
        }
    }

    private float getNeedleRotationFromValue() {
        float angle = (value - minValue)/maxValue * coveredAngle;
        return angle + baseAngle;
    }

    private boolean needleAnimated = true;

    /**
     * Sets needle to be animated ...
     * @param needleAnimated
     */
    public void setNeedleAnimated(boolean needleAnimated) {
        this.needleAnimated = needleAnimated;
    }

    public boolean isNeedleAnimated() {
        return needleAnimated;
    }


    @NonNull
    @Override
    protected SgNode createNode() {

        final SgGroup gauge = SgGroup.create()
                .add(SgEllipse.createCircle(DEFAULT_GAUGE_SIZE/2f)
                        .paint(PaintBuilder.create().color(Color.BLACK).strokeWidth(1)
                                .style(Paint.Style.FILL_AND_STROKE)
                                .shader(new RadialGradient(0, 0, DEFAULT_GAUGE_SIZE/2f, Color.argb(255,150,150,150),
                                        Color.BLACK, Shader.TileMode.MIRROR)
                                )
                        )
                )
                .attribute(PaintBuilder.createText().color(Color.WHITE).textSize(50)
                        .typeface(Typeface.DEFAULT_BOLD).textAlign(Paint.Align.CENTER))
                .build();

        double innerLabelCircleSize = DEFAULT_GAUGE_SIZE * 0.425;
        double coveredAngleInRadians = Math.toRadians(coveredAngle);
        double stepAngleInRadians = coveredAngleInRadians / steps;
        double baseAngleInRadians = Math.toRadians(baseAngle);
        int stepValue = (maxValue - minValue) / steps;
        int value = minValue;
        for (int i=0; i <= steps; i++) {
            String text = Integer.toString(value);
            value += stepValue;
            // compute position
            double angleInRadians = i * stepAngleInRadians + baseAngleInRadians;
            float x = (float)(innerLabelCircleSize * Math.cos(angleInRadians));
            float y = (float)(innerLabelCircleSize * Math.sin(angleInRadians) + 10);
            gauge.addNode(SgText.create(text,x,y).build());
        }

        float needleSize = DEFAULT_GAUGE_SIZE*0.5f;
        float needleOffset = - DEFAULT_GAUGE_SIZE * 0.1f;

        needle = SgGroup.create()
                .attribute(PaintBuilder.create().color( 0xFFA00000).strokeWidth(10))
                .add(SgLine.createWithSize(needleOffset,0,needleSize,0))
                .add(SgEllipse.createCircle(DEFAULT_GAUGE_SIZE * 0.03f))
                .pivot(0,0)
                .r(getNeedleRotationFromValue()).build();


        gauge.addNode(needle);

        float gaugeScaleFactor = size / DEFAULT_GAUGE_SIZE;

        gauge.setScale(gaugeScaleFactor);
        SgGroup root = SgGroup.create().add(gauge).build();

        return root;
    }
}
