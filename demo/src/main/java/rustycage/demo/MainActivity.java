package rustycage.demo;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;

import rustycage.BaseNode;
import rustycage.EllipseNode;
import rustycage.GroupNode;
import rustycage.LineNode;
import rustycage.PaintAttribute;
import rustycage.RectangleNode;
import rustycage.RustyCageView;
import rustycage.TextNode;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {


    public BaseNode createTest1Node() {
        Paint redPaint = new Paint();
        redPaint.setARGB(255,255,0,0);
        redPaint.setStrokeWidth(3);
        redPaint.setStyle(Paint.Style.STROKE);

        Paint greenPaint = new Paint();
        greenPaint.setARGB(255,0,255,0);


        Paint bluePaint = new Paint();
        bluePaint.setARGB(255,0,0,255);
        bluePaint.setStrokeWidth(20);
        bluePaint.setStyle(Paint.Style.STROKE);

        TextPaint textPaint = new TextPaint();
        textPaint.set(greenPaint);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint circularGradient = new Paint();
        circularGradient.setColor(Color.BLACK);
        circularGradient.setStrokeWidth(1);
        circularGradient.setStyle(Paint.Style.FILL_AND_STROKE);
        circularGradient.setShader(new RadialGradient(400, 400, 200,
                Color.argb(255,200,200,200), Color.BLACK, Shader.TileMode.MIRROR));


        GroupNode gn2 = new GroupNode();
        for (int i=0; i < 100; i++) {
            gn2.addNode(LineNode.createWithPoints(300+i*5,500,300+i*5,600).build());
        }
        gn2.setAttribute(new PaintAttribute(redPaint));

        LineNode line1 = LineNode.createWithPoints(50,50,400,400).paint(redPaint).build();

        GroupNode gn = GroupNode.create()
                .add(GroupNode.create()
                        .add(line1)
                        .add(LineNode.createWithSize(700,400,400,100).paint(redPaint))
                        .add(RectangleNode.createWithSize(30,530,500,300))
                        .add(RectangleNode.createWithSize(530,930,500,300).paint(greenPaint))
                )
                .add(gn2)
                .add(EllipseNode.createCircle(400,400,200).paint(circularGradient))
                .add(EllipseNode.createEllipse(300,800,200,100))
                .add(TextNode.create("XXXX").textPaint(textPaint).xy(50,50))
                .attribute(new PaintAttribute(bluePaint))
                .build();


        GroupNode groot = new GroupNode();
        groot.addNode(gn);

        ObjectAnimator oa = new ObjectAnimator();
        oa.setTarget(line1);
        oa.setPropertyName("x1");
        oa.setDuration(1000);
        oa.setStartDelay(1000);
        oa.setFloatValues(50,500);
        oa.start();

        return groot;
    }


    private static final float GAUGE_CENTER_X = 400;
    private static final float GAUGE_CENTER_Y = 400;
    private static final float GAUGE_SIZE = 350;

    public BaseNode createGauge() {

        Paint circularGradient = new Paint();
        circularGradient.setColor(Color.BLACK);
        circularGradient.setStrokeWidth(1);
        circularGradient.setStyle(Paint.Style.FILL_AND_STROKE);
        circularGradient.setShader(new RadialGradient(GAUGE_CENTER_X, GAUGE_CENTER_Y, GAUGE_SIZE,
                Color.argb(255,150,150,150), Color.BLACK, Shader.TileMode.MIRROR));


        TextPaint whitePaint = new TextPaint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(50);
        whitePaint.setTypeface(Typeface.DEFAULT_BOLD);
        whitePaint.setTextAlign(Paint.Align.CENTER);


        GroupNode root = GroupNode.create()
                .add(EllipseNode.createCircle(GAUGE_CENTER_X,GAUGE_CENTER_Y,GAUGE_SIZE).paint(circularGradient))
                .build();


        double size = GAUGE_SIZE * 0.85;
        for (int i=0; i < 17; i++) {
            String text = Integer.toString(i*10);
            // compute position
            double angle = i / 3.0 - Math.toRadians(270);
            float x = (float)(size * Math.cos(angle) + GAUGE_CENTER_X);
            float y = (float)(size * Math.sin(angle) + GAUGE_CENTER_Y + 10);
            root.addNode(TextNode.create(text,x,y).build());
        }

        root.setAttribute(new PaintAttribute(whitePaint));

        return root;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RustyCageView rcView = (RustyCageView)findViewById(R.id.rcView);

        BaseNode root = createGauge();
        rcView.setRootNode(root);


    }



}
