package rustycage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;
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

    private SgScene sceneNode;
    private NodeHitPath nodeHitPath;
    private float[] touchPoint = new float[2];


    private final AttributesStack attributesStack = new AttributesStack();
    private final FloatStack opacityStack = new FloatStack();

    private final DisplayMetrics displayMetrics = new DisplayMetrics();

    public RustyCageView(@NonNull Context context) {
        super(context);
        initAttributeStack(attributesStack);
        opacityStack.push(1f); // start with full visibility
    }

    public RustyCageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        initAttributeStack(attributesStack);
        opacityStack.push(1f); // start with full visibility
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

        if (sceneNode != null) {
            SgNode rootNode = sceneNode.getSceneDelegate();

            getDisplay().getMetrics(displayMetrics);

            AbstractCanvasRenderer<SgNode> renderer = RendererProvider.getInstance().getRendererForNode(rootNode);
            renderer.render(canvas, rootNode, opacityStack, attributesStack, displayMetrics);
            sceneNode.clearDirty();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // capture and bubble through scenegraph
        if (sceneNode != null) {
            //Log.d(TAG,"onTouchEvent: localXY["+event.getX()+", "+event.getY()
            //        +"], raw:["+event.getRawX()+", "+event.getRawY()+"]");
            touchPoint[0] = event.getX();
            touchPoint[1] = event.getY();
            boolean foundHitPath = sceneNode.findHitPath(nodeHitPath, touchPoint);
            //Log.d(TAG,"found: "+foundHitPath+", hitPath: "+nodeHitPath);
            if (foundHitPath) {
                nodeHitPath.deliverEvent(event);
                nodeHitPath.clear();
            }
        }
        // FIXME?
        return true;
    }

    public void setRootNode(@Nullable SgNode rootNode) {
        if (rootNode != null) {
            sceneNode = new SgScene(rootNode);
            nodeHitPath = new NodeHitPath();
        } else {
            sceneNode = null;
            nodeHitPath = null;
        }
        invalidate();
    }


    private class SgScene extends SgGroup {


        private SgNode sceneDelegate;

        public SgScene(@NonNull SgNode sceneDelegate) {
            this.sceneDelegate = sceneDelegate;
            this.sceneDelegate.setParent(this);
        }

        public @NonNull
        SgNode getSceneDelegate() {
            return sceneDelegate;
        }

        @Override
        protected void onMarkedDirty() {
            //Log.d(TAG,"SgScene invalidated");
            RustyCageView.this.invalidate();
        }

        @Override
        void clearDirty() {
            //if (isDirty()) {
                super.clearDirty();
                if (sceneDelegate.isDirty()) {
                    sceneDelegate.clearDirty();
                }
            //}
        }

        @Override
        void searchForHitPath(@NonNull NodeHitPath nodeHitPath, final float[] touchPoint) {
            //Log.d(TAG,"delegate.searchForHitPath touchPoint: ["+touchPoint[0]+", "+touchPoint[1]+"]");
            sceneDelegate.findHitPath(nodeHitPath, touchPoint);
        }

        @Override
        protected void computeLocalBounds(@NonNull float[] bounds) {
            sceneDelegate.computeTransformedBounds(bounds);
        }
    }

}
