package rustycage.demo;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rustycage.GroupNode;
import rustycage.LineNode;
import rustycage.RectangleNode;
import rustycage.RustyCageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RustyCageView rcView = (RustyCageView)findViewById(R.id.rcView);

        LineNode ln = new LineNode(50,50,400,400);
        RectangleNode rn = new RectangleNode(30,530,700,700);
        Paint p = new Paint();
        p.setARGB(255,255,0,0);
        p.setStrokeWidth(3);
        p.setStyle(Paint.Style.STROKE);

        ln.setPaint(p);
        rn.setPaint(p);

        GroupNode gn = new GroupNode();
        gn.addNode(ln);
        gn.addNode(rn);
        rcView.setRootNode(gn);
        rcView.invalidate();
    }



}
