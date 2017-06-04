package rustycage.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rustycage.PaintAttribute;
import rustycage.RustyCageView;
import rustycage.SgArc;
import rustycage.SgEllipse;
import rustycage.SgGroup;
import rustycage.SgImage;
import rustycage.SgLine;
import rustycage.SgNode;
import rustycage.SgPath;
import rustycage.SgRectangle;
import rustycage.SgText;
import rustycage.animation.GroupTransition;
import rustycage.animation.OpacityTransition;
import rustycage.animation.RotationTransition;
import rustycage.animation.ScaleTransition;
import rustycage.animation.TranslationTransition;
import rustycage.demo.components.Gauge;
import rustycage.demo.components.RadialSelector;
import rustycage.demo.components.SimpleButton;
import rustycage.event.TouchEvent;
import rustycage.event.TouchEventListener;
import rustycage.util.PaintBuilder;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";



    private String[] buttonNames = new String[] {
            "One", "Two", "Three"
    };


    public SgNode createMainMenu() {
        float yOffset = 0;
        int transitionDelay = 500;
        final float buttonWidth = 800;
        final float buttonHeight = 200;
        final float space = 20;
        SgGroup.Builder groupBuilder = SgGroup.create();
        GroupTransition groupTransition = GroupTransition.createParallel();
        for (String buttonName : buttonNames) {
            SimpleButton button = SimpleButton.create(buttonName, buttonWidth, buttonHeight).ty(yOffset).build();
            groupBuilder.add(button);
            button.setOpacity(0f);

            groupTransition.add(TranslationTransition.create(button).fromX(-100).toX(0).delay(transitionDelay));
            groupTransition.add(OpacityTransition.create(button).from(0).to(1).duration(1000).delay(transitionDelay));


            transitionDelay += 300;

            yOffset += buttonHeight + space;
        }

        groupTransition.delay(500).start();

        return groupBuilder.build();
    }


    public SgNode createTest1Node() {
        Paint redPaint = PaintBuilder.create().argb(255,255,0,0).strokeWidth(3).style(Paint.Style.STROKE).build();
        Paint greenPaint = PaintBuilder.create().argb(255,0,255,0).build();
        Paint bluePaint = PaintBuilder.create().argb(200,0,0,255).strokeWidth(20).style(Paint.Style.FILL_AND_STROKE).build();
        Paint textPaint = PaintBuilder.createText().from(greenPaint).alpha(100).textSize(50).textAlign(Paint.Align.CENTER).build();
        Paint circularGradient = PaintBuilder.create().color(Color.BLACK).strokeWidth(1).style(Paint.Style.FILL_AND_STROKE)
                .shader(new RadialGradient(400, 400, 200, Color.argb(255,200,200,200), Color.BLACK, Shader.TileMode.MIRROR))
                .build();


        SgGroup gn2 = SgGroup.create().build();

        for (int i=0; i < 100; i++) {
            gn2.addNode(SgLine.createWithPoints(300+i*5,500,300+i*5,600).build());
        }
        gn2.setAttribute(new PaintAttribute(redPaint));
        gn2.setOpacity(0.3f);

        SgLine line1 = SgLine.createWithPoints(50,50,400,400).paint(redPaint).build();

        SgText textNode = null;

        final SgPath sgPath = SgPath.create().arcTo(-250,-250,250,250,180,90)/*.rLineTo(0,40)*/
                .arcTo(-150,-150,150,150,270,-90).close().txy(800,300).s(1)
                        .paint(greenPaint).pivot(0,0).build();

        SgImage imageNode;

        SgGroup gn = SgGroup.create()
                .add(SgGroup.create()
                        .add(line1)
                        .add(SgLine.createWithSize(700,400,400,100).paint(redPaint))
                        .add(SgRectangle.createWithSize(30,530,500,300, 30, 30))
                        .add(SgRectangle.createWithSize(530,930,500,300).paint(greenPaint).opacity(0.5f))
                        .add(SgImage.createWithResource(getResources(), R.mipmap.ic_launcher)
                                .onTouch(null, new TouchEventListener() {
                                    private float lastX, lastY;
                                    @Override
                                    public boolean onEvent(@NonNull TouchEvent touchEvent) {
                                        Log.d(TAG,"bitmap touchevent: "+touchEvent);
                                        SgNode node = touchEvent.getCurrentNode();
                                        float localX = touchEvent.getLocalX();
                                        float localY = touchEvent.getLocalY();
                                        if (touchEvent.getTouchType() == TouchEvent.TouchType.DOWN) {
                                            // scale the gauge
                                            ScaleTransition.create(node).to(1.5f).duration(300).start();
                                            OpacityTransition.create(node).to(0.5f).duration(300).start();
                                            lastX = localX;
                                            lastY = localY;
                                            ((SgGroup)node.getParent()).moveToFront(node);
                                        } else if (touchEvent.getTouchType() == TouchEvent.TouchType.UP) {
                                            ScaleTransition.create(node).to(1f).duration(300).start();
                                            OpacityTransition.create(node).to(1f).duration(300).start();
                                        } else if (touchEvent.getTouchType() == TouchEvent.TouchType.MOVE) {
                                            float newX = localX - lastX;
                                            float newY = localY - lastY;
                                            Log.d(TAG," lastXY: "+lastX+", "+lastY+", newXY: "+newX+", "+newY);
                                            //lastX = newX;
                                            //lastY = newY;
                                            node.setTranslation(newX, newY);
                                        }
                                        return false;
                                    }
                                })
                        )
                        .add(SgArc.create(100,100,300,300,220,30))
                )
                .add(gn2)
                .add(SgEllipse.createCircle(200, 400,400).paint(circularGradient).opacity(0.7f))
                .add(SgEllipse.createEllipse(200,100,300,800))
                .add(textNode = SgText.create("XXXX").textPaint(textPaint).xy(300,300).build())
                .add(sgPath)
                .add(SgEllipse.createCircle(100).txy(800,300).onTouchDown(new TouchEventListener() {
                    @Override
                    public boolean onEvent(@NonNull TouchEvent touchEvent) {
                        Log.d(TAG,"onEvent: "+touchEvent);
                        RotationTransition.create(sgPath).by(360).duration(1000).start();
                        return true;
                    }
                }))
                .add(RadialSelector.create().txy(600, 1350))
                .attribute(new PaintAttribute(bluePaint))
                .opacity(0f)
                .build();




        SgLine l1 = SgLine.createWithPoints(0,0,80,0).build();
        SgLine l2 = SgLine.createWithPoints(0,40,80,40).build();
        SgLine l3 = SgLine.createWithPoints(0,80,80,80).build();
        SgGroup button = SgGroup.create().add(l1, l2, l3).attribute(new PaintAttribute(redPaint)).txy(400,400).build();

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


        //RotationTransition.create(sgPath).by(360).duration(1000).delay(2000).start();

        SgGroup groot = SgGroup.create().add(gn, button).build();


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


        /*
        Walker.forEachLeaf(groot, new Walker.Action() {
            @Override
            public void onNode(final @NonNull SgNode node) {
                node.setOnTouchEventListener(new SgEventListener() {
                    private float lastX, lastY;

                    @Override
                    public boolean onEvent(@NonNull MotionEvent touchEvent, float localX, float localY, boolean isCapturePhase) {
                        Log.d(TAG,"onEvent node: "+node+",: "+touchEvent+" localXY:"+localX+", "+localY+", isCapturePhase:"+isCapturePhase);
                        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            // scale the gauge
                            ScaleTransition.create(node).to(1.5f).duration(300).start();
                            OpacityTransition.create(node).to(0.5f).duration(300).start();
                            lastX = localX;
                            lastY = localY;
                            node.getParent().moveToFront(node);
                        } else if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
                            ScaleTransition.create(node).to(1f).duration(300).start();
                            OpacityTransition.create(node).to(1f).duration(300).start();
                        } else if (touchEvent.getAction() == MotionEvent.ACTION_MOVE) {
                            node.setTranslation(localX - lastX, localY - lastY);
                        }
                        return false;
                    }
                }, false);
            }
        });*/

        return groot;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RustyCageView rcView = (RustyCageView)findViewById(R.id.rcView);
        //SgNode root = createTest1Node();

        TouchEventListener gaugeListener = new TouchEventListener() {
            @Override
            public boolean onEvent(@NonNull TouchEvent touchEvent) {
                float lx = touchEvent.getLocalX()*1.2f;
                Log.d(TAG,"local X: "+lx);
                SgNode currentNode = touchEvent.getCurrentNode();
                float gaugeWidth = currentNode.getWidth();
                float normalizedX =  (gaugeWidth / 2 + lx) / gaugeWidth;
                Log.d(TAG,"normalized X: "+normalizedX);
                float value = 200*normalizedX;
                Log.d(TAG,"value X: "+value);
                ((Gauge)currentNode).setValue(value);
                return true;
            }
        };
        final Gauge gauge1 = new Gauge(0,200, 10, 270, 135);
        gauge1.setSize(500);
        gauge1.addOnTouchListener(TouchEvent.TouchType.DOWN, gaugeListener);

        final Gauge gauge2 = new Gauge(0,200, 10, 300, 90);
        gauge2.setSize(500);
        gauge2.addOnTouchListener(TouchEvent.TouchType.DOWN, gaugeListener);
        gauge2.setTranslationY(600);


        //SgGroup root = SgGroup.create().add(gauge1).add(gauge2).build();
        SgNode root = createMainMenu();
        rcView.setRootNode(root);
        rcView.setBackgroundColor(Color.BLACK);


    }



}
