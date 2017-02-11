package rustycage;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import rustycage.event.TouchEventListener;
import rustycage.impl.Bounds;

/**
 * Created by breh on 9/9/16.
 */
public abstract class BaseNode {

    private static final String TAG = "BaseNode";

    // FIXME - all these fields need memory optimization
    private String id;

    private float[] localBounds = new float[4];

    private boolean dirty = true;
    private boolean localBoundsDirty = true;

    private float opacity = 1f;

    BaseNode() {
    }

    public @Nullable String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public final void setTranslationX(float tx) {
        getOrCreateTransformationSupport().setTx(tx);
        markDirty();
    }

    public final void setTranslationY(float ty) {
        getOrCreateTransformationSupport().setTy(ty);
        markDirty();
    }


    public final void setTranslation(float tx, float ty) {
        NodeTransformationSupport support = getOrCreateTransformationSupport();
        support.setTx(tx);
        support.setTy(tx);
        markDirty();
    }

    public final void setScale(float s) {
        setScale(s,s);
    }

    public final void setScale(float sx, float sy) {
        NodeTransformationSupport support = getOrCreateTransformationSupport();
        support.setScaleX(sx);
        support.setScaleY(sy);
        markDirty();
    }

    public final void setRotation(float r) {
        getOrCreateTransformationSupport().setRotation(r);
        markDirty();
    }

    public void setPivot(float px, float py) {
        getOrCreateTransformationSupport().setPivot(px, py);
        markDirty();
    }

    public void resetPivot() {
        getOrCreateTransformationSupport().resetPivot();
        markDirty();
    }

    public float getPivotX() {
        return transformationSupport != null ? transformationSupport.getPivotX() : Float.NaN;
    }

    public float getPivotY() {
        return transformationSupport != null ? transformationSupport.getPivotY() : Float.NaN;
    }


    public final float getTranslationX() {
        return transformationSupport != null ? transformationSupport.getTx() : Float.NaN;
    }

    public final float getTranslationY() {
        return transformationSupport != null ? transformationSupport.getTy() : Float.NaN;
    }

    public final float getScaleX() {
        return transformationSupport != null ? transformationSupport.getSx() : Float.NaN;
    }

    public final float getScaleY() {
        return transformationSupport != null ? transformationSupport.getSy() : Float.NaN;
    }

    public final float getRotation() {
        return transformationSupport != null ? transformationSupport.getR() : Float.NaN;
    }


    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        if (opacity > 1f) opacity = 1f;
        if (opacity < 0f) opacity = 0f;
        this.opacity = opacity;
        markDirty();
    }


    /**
     * Marks item dirty. Typically not used by a library user, only when things like ShapeNode paint
     * object is updated / animated (there is no listener which would allow to listen on
     * changes to the paint object)
     */
    public final void markDirty() {
        if (!dirty) {
            dirty = true;
            if (parent != null) {
                parent.markDirty();
            }
            onMarkedDirty();
        }
    }

    void onMarkedDirty() {
    }


    void clearDirty() {
        if (dirty) {
            dirty = false;
        }
    }


    protected final boolean isDirty() {
        return dirty;
    }

    public final float getLeft() {
        refreshLocalBoundsIfNeeded();
        return localBounds[0];
    }

    public final float getRight() {
        refreshLocalBoundsIfNeeded();
        return localBounds[2];
    }

    public final float getTop() {
        refreshLocalBoundsIfNeeded();
        return localBounds[1];
    }

    public final float getBottom() {
        refreshLocalBoundsIfNeeded();
        return localBounds[3];
    }

    public final @NonNull Bounds getLocalBounds() {
        return getLocalBounds(null);
    }


    public final @NonNull Bounds getLocalBounds(@Nullable Bounds bounds) {
        if (bounds == null) {
            bounds = new Bounds();
        }
        refreshLocalBoundsIfNeeded();
        bounds.set(localBounds);
        return bounds;
    }


    protected final void markLocalBoundsDirty() {
        localBoundsDirty = true;
        if (transformationSupport != null) {
            transformationSupport.markTransformedBoundsDirty();
        }
        markDirty();
    }


    private void refreshLocalBoundsIfNeeded() {
        if (localBoundsDirty) {
            computeLocalBounds(localBounds);
            localBoundsDirty = false;
        }
    }

    protected abstract void computeLocalBounds(@NonNull float[] bounds);


    public final float getTransformedLeft() {
        return transformationSupport != null ? transformationSupport.getTransformedLeft(this) : getLeft();
    }

    public final float getTransformedRight() {
        return transformationSupport != null ? transformationSupport.getTransformedRight(this) : getRight();
    }

    public final float getTransformedTop() {
        return transformationSupport != null ? transformationSupport.getTransformedTop(this) : getTop();
    }

    public final float getTransformedBottom() {
        return transformationSupport != null ? transformationSupport.getTransformedBottom(this) : getBottom();
    }



    public final @NonNull Bounds getTransformedBounds() {
        return transformationSupport != null ? transformationSupport.getTransformedBounds(this, null) : getLocalBounds();
    }

    void computeTransformedBounds(@NonNull float[] bounds) {
        if (transformationSupport != null) {
            NodeTransformationSupport.computeTransformedBounds(localBounds, transformationSupport.getMatrix(this), bounds);
        } else {
            computeLocalBounds(bounds);
        }
    }

    public final @Nullable Matrix getMatrix() {
        return transformationSupport != null ? transformationSupport.getMatrix(this) : null;
    }
    // size

    public  final float getWidth() {
        return Math.abs(getRight() - getLeft());
    }

    public final float getHeight() {
        return Math.abs(getBottom() - getTop());
    }



    private NodeTransformationSupport transformationSupport;
    private NodeTransformationSupport getOrCreateTransformationSupport() {
        if (transformationSupport == null) {
            transformationSupport = new NodeTransformationSupport(localBounds);
        }
        return transformationSupport;
    }


    private EventSupport eventSupport;
    EventSupport getEventSupport() {
        if (eventSupport == null) {
            eventSupport = new EventSupport();
        }
        return eventSupport;
    }

    boolean hasCaptureListener() {
        return eventSupport != null && eventSupport.capturePhase;
    }

    boolean hasBubbleListener() {
        return eventSupport != null && !eventSupport.capturePhase;
    }

    public void setOnTouchEventListener(@Nullable TouchEventListener listener, boolean capturePhase) {
        getEventSupport().setTouchEventListener(listener, capturePhase);
        if (listener == null) {
            eventSupport = null;
        }
    }


    /**
     * Checks whether given point is within bounds of this node.
     * @param point in "parent" (transformed) coordinates
     * @return true if it is within bounds
     */
    private boolean isWithinBounds(@NonNull float[] point) {
        float x = point[0];
        float y = point[1];
        return (x >= getTransformedLeft() && x <= getTransformedRight()
                && y >= getTransformedTop() && y <= getTransformedBottom());
    }


    /**
     * Tests is the touch point is in the bounds of this node. If true, converts the touch point
     * to the local coordinates of this node (if applicable)
     * @param touchPoint
     * @return
     */
    final boolean findHitPath(@NonNull NodeHitPath nodeHitPath, float[] touchPoint) {
        //Log.d(TAG,"findHitPath: "+this+": touchPoint: ["+touchPoint[0]+", "+touchPoint[1]+"], tbounds: ["+getTransformedLeft()+", "+getTransformedRight()
        //        +"; "+getTransformedTop()+", "+getTransformedBottom()+"], lbounds: ["+getLeft()+", "+getRight()
        //        +"; "+getTop()+", "+getBottom()+"]");
        if (isWithinBounds(touchPoint)) {
            // succeeded hit test, adding to hit path
            if (transformationSupport != null) {
                // need to transform to local coordinates
                getOrCreateTransformationSupport().getInverseMatrix(this).mapPoints(touchPoint);
            }
            //Log.d(TAG,"within bounds: "+this+", localX: "+touchPoint[0]+", localY: "+touchPoint[1]);
            nodeHitPath.pushNode(this, touchPoint[0], touchPoint[1]);
            searchForHitPath(nodeHitPath, touchPoint);
            return true;
        } else {
            // failed hit test
            return false;
        }
    }

    // looks for children nodes
    void searchForHitPath(@NonNull NodeHitPath nodeHitPath, float[] touchPoint) {
    }

    private BaseNode parent;

    public final @Nullable BaseNode getParent() {
        return parent;
    }

    final void setParent(@Nullable BaseNode parent) {
        this.parent = parent;
    }



    public static abstract class Builder<B extends Builder<B,N>, N extends BaseNode> {

        private final N node;

        protected Builder(@NonNull N node) {
            this.node = node;
        }

        protected @NonNull N getNode() {
            return node;
        }

        @SuppressWarnings("unchecked")
        protected final B getBuilder() {
            return (B)this;
        }

        public final B txy(float x, float y) {
            getNode().setTranslation(x,y);
            return getBuilder();
        }

        public final B r(float r) {
            getNode().setRotation(r);
            return getBuilder();
        }

        public final B s(float s) {
            getNode().setScale(s);
            return getBuilder();
        }

        public final B id(@NonNull String id) {
            getNode().setId(id);
            return getBuilder();
        }

        public final B opacity(float opacity) {
            getNode().setOpacity(opacity);
            return getBuilder();
        }


        public final N build() {
            return node;
        }


    }


    // node transformation support
    private final static class NodeTransformationSupport {

        private float tx,ty;
        private float sx = 1f,sy = 1f;
        private float r;

        private float px = Float.NaN, py = Float.NaN;

        private Matrix matrix;
        private Matrix inverseMatrix;
        private static final Matrix IDENTITY_MATRIX = new Matrix();

        private float[] transformedBounds = new float[4];

        private boolean transformedBoundsDirty = true;

        public NodeTransformationSupport(@NonNull float[] localBounds) {
            transformedBounds[0] = localBounds[0];
            transformedBounds[1] = localBounds[1];
            transformedBounds[2] = localBounds[2];
            transformedBounds[3] = localBounds[3];
        }

        public final void setTx(float tx) {
            this.tx = tx;
            markTransformedBoundsDirty();
        }

        public final void setTy(float ty) {
            this.ty = ty;
            markTransformedBoundsDirty();
        }


        public final void setScaleX(float sx) {
            this.sx = sx;
            markTransformedBoundsDirty();
        }

        public final void setScaleY(float sy) {
            this.sy = sy;
            markTransformedBoundsDirty();
        }


        public final void setRotation(float r) {
            this.r = r;
            markTransformedBoundsDirty();
        }

        public void setPivot(float px, float py) {
            this.px = px;
            this.py = py;
            markTransformedBoundsDirty();
        }

        public void resetPivot() {
            this.px = Float.NaN;
            this.py = Float.NaN;
            markTransformedBoundsDirty();
        }

        public float getPivotX() {
            return px;
        }

        public float getPivotY() {
            return py;
        }


        protected float getActualPivotX(@NonNull BaseNode node) {
            if (Float.isNaN(px)) {
                return (node.getRight() + node.getLeft()) / 2f;
            } else {
                return px;
            }
        }

        protected float getActualPivotY(@NonNull BaseNode node) {
            if (Float.isNaN(py)) {
                return (node.getBottom() + node.getTop()) / 2f;
            } else {
                return py;
            }
        }

        public final float getTx() {
            return tx;
        }

        public final float getTy() {
            return ty;
        }

        public final float getSx() {
            return sx;
        }

        public final float getSy() {
            return sy;
        }

        public final float getR() {
            return r;
        }


        public final float getTransformedLeft(@NonNull BaseNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[0];
        }

        public final float getTransformedRight(@NonNull BaseNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[2];
        }

        public final float getTransformedTop(@NonNull BaseNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[1];
        }

        public final float getTransformedBottom(@NonNull BaseNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[3];
        }


        protected final void markTransformedBoundsDirty() {
            matrix = null;
            transformedBoundsDirty = true;
        }

        public final @NonNull Bounds getTransformedBounds(@NonNull BaseNode node, @Nullable Bounds bounds) {
            if (bounds == null) {
                bounds = new Bounds();
            }
            refreshTransformedBoundsIfNeeded(node);
            if (transformedBounds != null) {
                bounds.set(transformedBounds);
            } else {
                node.getLocalBounds(bounds);
            }
            return bounds;
        }


        private void refreshTransformedBoundsIfNeeded(@NonNull BaseNode node) {
            if (transformedBoundsDirty) {
                computeTransformedBounds(node.localBounds, getMatrix(node), transformedBounds);
                transformedBoundsDirty = false;
            }
        }


        static void computeTransformedBounds(@NonNull float[] localBounds, @NonNull Matrix matrix, @Nullable float[] transformedBounds) {
            if (! matrix.isIdentity()) {
                matrix.mapPoints(transformedBounds, localBounds);
            } else {
                transformedBounds[0] = localBounds[0];
                transformedBounds[1] = localBounds[1];
                transformedBounds[2] = localBounds[2];
                transformedBounds[3] = localBounds[3];
            }
        }


        protected final boolean isMatrixDirty() {
            return matrix == null;
        }

        private Matrix computeMatrix(@NonNull BaseNode node) {
            if (tx == 0 && ty == 0 && r == 0 && sx == 1 && sy == 1) {
                return IDENTITY_MATRIX;
            } // else
            Matrix m = new Matrix();
            if (sx != 1 || sy != 1) {
                m.postScale(sx, sy, getActualPivotX(node), getActualPivotY(node));
            }
            if (r != 0) {
                m.postRotate(r, getActualPivotX(node), getActualPivotY(node));
            }
            if (tx != 0 || ty != 0) {
                m.postTranslate(tx, ty);
            }
            return m;
        }

        public final @NonNull Matrix getMatrix(@NonNull BaseNode node) {
            if (matrix == null) {
                matrix = computeMatrix(node);
            }
            return matrix;
        }

        public final @Nullable Matrix getInverseMatrix(@NonNull BaseNode node) {
            if (inverseMatrix == null) {
                Matrix inverse = new Matrix();
                boolean inverted = getMatrix(node).invert(inverse);
                if (inverted) {
                    inverseMatrix = inverse;
                } else {
                    Log.w(TAG,"Cannot compute inverted matrix");
                }
            }
            return inverseMatrix;
        }
    }



    // node event support
    static class EventSupport {

        private boolean capturePhase;
        private TouchEventListener listener;


        public void setTouchEventListener(@Nullable TouchEventListener listener, boolean capturePhase) {
            this.listener = listener;
            this.capturePhase = capturePhase;
        }


        public boolean deliverEvent(@NonNull MotionEvent motionEvent, float localX, float localY, boolean isCapture) {
            if (capturePhase == isCapture && listener != null) {
                return listener.onTouchEvent(motionEvent, localX, localY, isCapture);
            } else {
                return false;
            }
        }
    }


}



