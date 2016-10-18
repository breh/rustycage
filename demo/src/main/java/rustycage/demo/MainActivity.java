package rustycage.demo;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rustycage.GroupNode;
import rustycage.LineNode;
import rustycage.PaintAttribute;
import rustycage.RectangleNode;
import rustycage.RustyCageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RustyCageView rcView = (RustyCageView)findViewById(R.id.rcView);

        LineNode ln = new LineNode(50,50,400,400);
        RectangleNode rn1 = new RectangleNode(30,530,700,700);

        RectangleNode rn2 = new RectangleNode(530,930,1200,1200);
        Paint redPaint = new Paint();
        redPaint.setARGB(255,255,0,0);
        redPaint.setStrokeWidth(3);
        redPaint.setStyle(Paint.Style.STROKE);

        Paint greenPaint = new Paint();
        greenPaint.setARGB(255,0,255,0);

        ln.setPaint(redPaint);
        rn1.setPaint(redPaint);

        GroupNode gn1 = new GroupNode();
        gn1.addNode(ln);
        gn1.addNode(rn1);
        gn1.addNode(rn2);

        GroupNode gn2 = new GroupNode();
        for (int i=0; i < 100; i++) {
            LineNode l = new LineNode(300+i*5,1500,300+i*5,2000);
            gn2.addNode(l);
        }
        gn2.setAttribute(new PaintAttribute(greenPaint));


        GroupNode gnx = new GroupNode();
        gnx.addNode(gn1);
        gnx.addNode(gn2);
        rcView.setRootNode(gnx);
        rcView.invalidate();
    }



}
