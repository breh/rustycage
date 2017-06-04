package rustycage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import rustycage.impl.AttributesStack;
import rustycage.impl.Bounds;
import rustycage.impl.FloatStack;
import rustycage.impl.renderer.AbstractCanvasRenderer;
import rustycage.impl.renderer.RendererProvider;

/**
 * Created by breh on 9/12/16.
 */
public class RustyCageView extends View {

    private static final String TAG = "RustyCageView";

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
    private SgNodeHitPath nodeHitPath;
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


    private boolean outlineVisible = false;

    public void setOutlineVisible(boolean outlineVisible) {
        this.outlineVisible = outlineVisible;
        invalidate();
    }

    public final boolean isOutlineVisible() {
        return outlineVisible;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (outlineVisible) {
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

            float canvasWidth = canvas.getWidth();
            float canvasHeight = canvas.getHeight();
            // center the scene node;

            float offsetX = (canvasWidth - sceneNode.getWidth()) / 2f - sceneNode.getLocalBoundsLeft();
            float offsetY = (canvasHeight - sceneNode.getHeight()) / 2f - sceneNode.getLocalBoundsTop();
            sceneNode.setTranslation(offsetX, offsetY);
            //Log.d(TAG,"offsetX/Y: "+offsetX+", "+offsetY);

            //SgNode rootNode = sceneNode.getSceneDelegate();

            getDisplay().getMetrics(displayMetrics);

            AbstractCanvasRenderer<SgScene> renderer = RendererProvider.getInstance().getRendererForNode(sceneNode);
            renderer.render(canvas, sceneNode, opacityStack, attributesStack, displayMetrics);
            sceneNode.clearInvalidated();
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
            //Log.d(TAG,"onEvent: localXY["+event.getX()+", "+event.getY()
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
            nodeHitPath = new SgNodeHitPath();
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
            this.addNode(sceneDelegate);
        }

        public @NonNull
        SgNode getSceneDelegate() {
            return sceneDelegate;
        }

        @Override
        protected void onInvalidated() {
            RustyCageView.this.invalidate();
        }

        @Override
        void clearInvalidated() {
            //if (isInvalidated()) {
                super.clearInvalidated();
                if (sceneDelegate.isInvalidated()) {
                    sceneDelegate.clearInvalidated();
                }
            //}
        }

        @Override
        void searchForHitPath(@NonNull SgNodeHitPath nodeHitPath, final float[] touchPoint) {
            //Log.d(TAG,"delegate.searchForHitPath touchPoint: ["+touchPoint[0]+", "+touchPoint[1]+"]");
            sceneDelegate.findHitPath(nodeHitPath, touchPoint);
        }

        @Override
        protected void computeLocalBounds(@NonNull float[] bounds) {
            sceneDelegate.computeTransformedBounds(bounds);
        }
    }

}
