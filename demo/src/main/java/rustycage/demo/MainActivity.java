package rustycage.demo;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rustycage.EllipseNode;
import rustycage.GroupNode;
import rustycage.LineNode;
import rustycage.PaintAttribute;
import rustycage.RectangleNode;
import rustycage.RustyCageView;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RustyCageView rcView = (RustyCageView)findViewById(R.id.rcView);

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




        GroupNode gn2 = new GroupNode();
        for (int i=0; i < 100; i++) {
            gn2.addNode(LineNode.createWithPoints(300+i*5,500,300+i*5,600).build());
        }
        gn2.setAttribute(new PaintAttribute(redPaint));

        GroupNode gn = GroupNode.create()
                .add(GroupNode.create()
                    .add(LineNode.createWithPoints(50,50,400,400).paint(redPaint))
                    .add(LineNode.createWithSize(700,400,400,100).paint(redPaint))
                    .add(RectangleNode.createWithSize(30,530,500,300))
                    .add(RectangleNode.createWithSize(530,930,500,300).paint(greenPaint))
                )
                .add(gn2)
                .add(EllipseNode.createCircle(400,400,200))
                .add(EllipseNode.createEllipse(300,800,200,100))
                .attribute(new PaintAttribute(bluePaint))
                .build();


        GroupNode groot = new GroupNode();
        groot.addNode(gn);

        rcView.setRootNode(groot);
        rcView.invalidate();
    }



}
