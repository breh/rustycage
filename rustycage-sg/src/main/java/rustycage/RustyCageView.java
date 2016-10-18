package rustycage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import rustycage.impl.AttributesStack;
import rustycage.impl.renderer.AbstractCanvasRenderer;
import rustycage.impl.renderer.RendererProvider;

/**
 * Created by breh on 9/12/16.
 */
public class RustyCageView extends View {

    private static final String TAG = "RustyCageView";

    private static boolean DRAW_OUTLINE_FRAME = true;
    private static Paint OUTLINE_PAINT = new Paint();
    static {
        OUTLINE_PAINT.setARGB(255,100,100,100);
    }

    private static Paint DEFAULT_PAINT = new Paint();
    static {
        DEFAULT_PAINT.setARGB(255,30,30,30);
    }

    private int width;
    private int height;

    private final AttributesStack attributesStack = new AttributesStack();

    private final DisplayMetrics displayMetrics = new DisplayMetrics();

    public RustyCageView(@NonNull Context context) {
        super(context);
        initAttributeStack(attributesStack);
    }

    public RustyCageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        initAttributeStack(attributesStack);
    }


    private static void initAttributeStack(@NonNull AttributesStack attributesStack) {
        attributesStack.push(Paint.class,DEFAULT_PAINT);
        attributesStack.push(ResolutionUnit.class,ResolutionUnit.PX);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (DRAW_OUTLINE_FRAME) {
            int lx = 0;
            int rx = width -1;
            int ty = 0;
            int by = height -1;
            canvas.drawLine(lx,ty,rx,ty, OUTLINE_PAINT);
            canvas.drawLine(rx,ty,rx,by, OUTLINE_PAINT);
            canvas.drawLine(rx,by,lx,by, OUTLINE_PAINT);
            canvas.drawLine(lx,by,lx,ty, OUTLINE_PAINT);
        }

        getDisplay().getMetrics(displayMetrics);
        if (rootNode != null) {

            AbstractCanvasRenderer<BaseNode> renderer = RendererProvider.getInstance().getRendererForNode(rootNode);
            Log.d(TAG,"renderer = "+renderer);
            renderer.render(canvas,rootNode, attributesStack, displayMetrics);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    private BaseNode rootNode;


    public void setRootNode(@Nullable BaseNode rootNode) {
        this.rootNode = rootNode;
        invalidate();
    }
}
