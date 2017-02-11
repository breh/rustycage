package rustycage.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;

import rustycage.BaseNode;
import rustycage.EllipseNode;
import rustycage.GroupNode;
import rustycage.ImageNode;
import rustycage.LineNode;
import rustycage.PaintAttribute;
import rustycage.RectangleNode;
import rustycage.RustyCageView;
import rustycage.TextNode;
import rustycage.animation.FadeTransition;
import rustycage.animation.GroupTransition;
import rustycage.animation.RotationTransition;
import rustycage.animation.ScaleTransition;
import rustycage.animation.TranslationTransition;
import rustycage.event.TouchEventListener;
import rustycage.util.Walker;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    public BaseNode createTest1Node() {
        Paint redPaint = new Paint();
        redPaint.setARGB(255,255,0,0);
        redPaint.setStrokeWidth(3);
        redPaint.setStyle(Paint.Style.STROKE);

        Paint greenPaint = new Paint();
        greenPaint.setARGB(255,0,255,0);


        Paint bluePaint = new Paint();
        bluePaint.setARGB(200,0,0,255);
        bluePaint.setStrokeWidth(20);
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        TextPaint textPaint = new TextPaint();
        textPaint.set(greenPaint);
        textPaint.setAlpha(100);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint circularGradient = new Paint();
        circularGradient.setColor(Color.BLACK);
        circularGradient.setStrokeWidth(1);
        circularGradient.setStyle(Paint.Style.FILL_AND_STROKE);
        circularGradient.setShader(new RadialGradient(400, 400, 200,
                Color.argb(255,200,200,200), Color.BLACK, Shader.TileMode.MIRROR));


        GroupNode gn2 = GroupNode.create().build();
        for (int i=0; i < 100; i++) {
            gn2.addNode(LineNode.createWithPoints(300+i*5,500,300+i*5,600).build());
        }
        gn2.setAttribute(new PaintAttribute(redPaint));
        gn2.setOpacity(0.3f);

        LineNode line1 = LineNode.createWithPoints(50,50,400,400).paint(redPaint).build();

        TextNode textNode = null;

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        GroupNode gn = GroupNode.create()
                .add(GroupNode.create()
                        .add(line1)
                        .add(LineNode.createWithSize(700,400,400,100).paint(redPaint))
                        .add(RectangleNode.createWithSize(30,530,500,300))
                        .add(RectangleNode.createWithSize(530,930,500,300).paint(greenPaint).opacity(0.5f))
                        .add(ImageNode.createWithBitmap(bitmap1))
                )
                .add(gn2)
                .add(EllipseNode.createCircle(200, 400,400).paint(circularGradient).opacity(0.7f))
                .add(EllipseNode.createEllipse(200,100,300,800))
                .add(textNode = TextNode.create("XXXX").textPaint(textPaint).xy(300,300).build())
                .attribute(new PaintAttribute(bluePaint))
                .opacity(0f)
                .build();




        LineNode l1 = LineNode.createWithPoints(0,0,80,0).build();
        LineNode l2 = LineNode.createWithPoints(0,40,80,40).build();
        LineNode l3 = LineNode.createWithPoints(0,80,80,80).build();
        GroupNode button = GroupNode.create().add(l1, l2, l3).attribute(new PaintAttribute(redPaint)).txy(400,400).build();

        // button animation
        ObjectAnimator l1Anim = new ObjectAnimator();
        l1Anim.setTarget(l1);
        l1Anim.setPropertyName("y2");
        l1Anim.setFloatValues(0,80);

        ObjectAnimator l2Anim = new ObjectAnimator();
        l2Anim.setTarget(l2);
        l2Anim.setPropertyName("opacity");
        l2Anim.setFloatValues(1,0);


        ObjectAnimator l3Anim = new ObjectAnimator();
        l3Anim.setTarget(l3);
        l3Anim.setPropertyName("y2");
        l3Anim.setFloatValues(80,0);

        AnimatorSet buttonAnimator = new AnimatorSet();
        buttonAnimator.setDuration(300);
        buttonAnimator.setStartDelay(1000);
        buttonAnimator.playTogether(l1Anim, l2Anim, l3Anim);
        buttonAnimator.start();


        GroupNode groot = GroupNode.create().add(gn, button).build();

        ObjectAnimator oa = new ObjectAnimator();
        oa.setTarget(line1);
        oa.setPropertyName("x1");
        oa.setDuration(1000);
        oa.setStartDelay(1000);
        oa.setFloatValues(50,500);
        oa.start();

        ObjectAnimator rotationA = new ObjectAnimator();
        rotationA.setTarget(textNode);
        rotationA.setPropertyName("rotation");
        rotationA.setDuration(2000);
        rotationA.setStartDelay(1000);
        rotationA.setFloatValues(0,720);
        rotationA.start();


        ObjectAnimator opacityAnim = new ObjectAnimator();
        opacityAnim.setTarget(gn);
        opacityAnim.setPropertyName("opacity");
        opacityAnim.setFloatValues(0, 1);
        opacityAnim.setDuration(1000);
        opacityAnim.setStartDelay(700);
        opacityAnim.start();


        Walker.forEachLeaf(groot, new Walker.Action() {
            @Override
            public void onNode(final @NonNull BaseNode node) {
                node.setOnTouchEventListener(new TouchEventListener() {
                    @Override
                    public boolean onTouchEvent(@NonNull MotionEvent touchEvent, float localX, float localY, boolean isCapturePhase) {
                        Log.d(TAG,"onTouchEvent node: "+node+",: "+touchEvent+" localXY:"+localX+", "+localY+", isCapturePhase:"+isCapturePhase);
                        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            // scale the gauge
                            ScaleTransition.create(node).to(1.5f).duration(300).start();
                            FadeTransition.create(node).to(0.5f).duration(300).start();
                        } else if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
                            ScaleTransition.create(node).to(1f).duration(300).start();
                            FadeTransition.create(node).to(1f).duration(300).start();
                        }
                        return false;
                    }
                }, false);
            }
        });

        return groot;
    }


    private static final float GAUGE_SIZE = 350;

    public BaseNode createGauge() {

        Paint circularGradient = new Paint();
        circularGradient.setColor(Color.BLACK);
        circularGradient.setStrokeWidth(1);
        circularGradient.setStyle(Paint.Style.FILL_AND_STROKE);
        circularGradient.setShader(new RadialGradient(0, 0, GAUGE_SIZE,
                Color.argb(255,150,150,150), Color.BLACK, Shader.TileMode.MIRROR));


        TextPaint whitePaint = new TextPaint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(50);
        whitePaint.setTypeface(Typeface.DEFAULT_BOLD);
        whitePaint.setTextAlign(Paint.Align.CENTER);


        final GroupNode gauge = GroupNode.create()
                .add(EllipseNode.createCircle(GAUGE_SIZE).paint(circularGradient))
                .build();


        double size = GAUGE_SIZE * 0.85;
        for (int i=0; i < 17; i++) {
            String text = Integer.toString(i*10);
            // compute position
            double angle = i / 3.0 - Math.toRadians(270);
            float x = (float)(size * Math.cos(angle));
            float y = (float)(size * Math.sin(angle) + 10);
            gauge.addNode(TextNode.create(text,x,y).r(i*15).build());
        }

        gauge.setAttribute(new PaintAttribute(whitePaint));

        Log.w(TAG," gauge local bounds: "+gauge.getLocalBounds());
        Log.w(TAG," gauge transformed bounds: "+gauge.getTransformedBounds());

        GroupNode root = GroupNode.create().add(gauge).build();

        Log.w(TAG," root local bounds: "+root.getLocalBounds());

        root.setRotation(-20);

        GroupTransition.createParallel()
                .add(TranslationTransition.create(root).byX(300).toY(500))
                .add(RotationTransition.create(root).to(720))
                .add(ScaleTransition.create(root).from(0.5f).to(1f))
                .duration(2000)
                .delay(1000)
                .start();

        gauge.setOnTouchEventListener(new TouchEventListener() {
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent touchEvent, float localX, float localY, boolean isCapturePhase) {
                Log.d(TAG,"onTouchEvent gauge: "+touchEvent+" localXY:"+localX+", "+localY+", isCapturePhase:"+isCapturePhase);
                if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // scale the gauge
                    ScaleTransition.create(gauge).to(1.5f).duration(300).start();
                } else if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
                    ScaleTransition.create(gauge).to(1f).duration(300).start();
                }
                return false;
            }
        }, false);

        return root;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RustyCageView rcView = (RustyCageView)findViewById(R.id.rcView);
        BaseNode root = createTest1Node();
        //BaseNode root = createGauge();
        rcView.setRootNode(root);


    }



}
