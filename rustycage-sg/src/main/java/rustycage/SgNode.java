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
public abstract class SgNode {

    private static final String TAG = "SgNode";

    // FIXME - all these fields need memory optimization
    private String id;

    private float[] localBounds = new float[4];

    private boolean dirty = true;
    private boolean localBoundsDirty = true;

    private float opacity = 1f;

    SgNode() {
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
        support.setTy(ty);
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
        return transformationSupport != null ? transformationSupport.getTx() :0f;
    }

    public final float getTranslationY() {
        return transformationSupport != null ? transformationSupport.getTy() : 0f;
    }

    public final float getScaleX() {
        return transformationSupport != null ? transformationSupport.getSx() : 1f;
    }

    public final float getScaleY() {
        return transformationSupport != null ? transformationSupport.getSy() : 1f;
    }

    public final float getRotation() {
        return transformationSupport != null ? transformationSupport.getR() : 0f;
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
     * Marks item dirty. Typically not used by a library user, only when things like SgShape paint
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

    public final float getLocalBoundsLeft() {
        refreshLocalBoundsIfNeeded();
        return localBounds[0];
    }

    public final float getLocalBoundsRight() {
        refreshLocalBoundsIfNeeded();
        return localBounds[2];
    }

    public final float getLocalBoundsTop() {
        refreshLocalBoundsIfNeeded();
        return localBounds[1];
    }

    public final float getLocalBoundsBottom() {
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


    public final float getTransformedBoundsLeft() {
        return transformationSupport != null ? transformationSupport.getTransformedLeft(this) : getLocalBoundsLeft();
    }

    public final float getTransformedBoundsRight() {
        return transformationSupport != null ? transformationSupport.getTransformedRight(this) : getLocalBoundsRight();
    }

    public final float getTransformedBoundsTop() {
        return transformationSupport != null ? transformationSupport.getTransformedTop(this) : getLocalBoundsTop();
    }

    public final float getTransformedBoundsBottom() {
        return transformationSupport != null ? transformationSupport.getTransformedBottom(this) : getLocalBoundsBottom();
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
        return Math.abs(getLocalBoundsRight() - getLocalBoundsLeft());
    }

    public final float getHeight() {
        return Math.abs(getLocalBoundsBottom() - getLocalBoundsTop());
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
        return (x >= getTransformedBoundsLeft() && x <= getTransformedBoundsRight()
                && y >= getTransformedBoundsTop() && y <= getTransformedBoundsBottom());
    }


    /**
     * Tests is the touch point is in the bounds of this node. If true, converts the touch point
     * to the local coordinates of this node (if applicable)
     * @param touchPoint
     * @return
     */
    final boolean findHitPath(@NonNull NodeHitPath nodeHitPath, float[] touchPoint) {
        //Log.d(TAG,"findHitPath: "+this+": touchPoint: ["+touchPoint[0]+", "+touchPoint[1]+"], tbounds: ["+getTransformedBoundsLeft()+", "+getTransformedBoundsRight()
        //        +"; "+getTransformedBoundsTop()+", "+getTransformedBoundsBottom()+"], lbounds: ["+getLocalBoundsLeft()+", "+getLocalBoundsRight()
        //        +"; "+getLocalBoundsTop()+", "+getLocalBoundsBottom()+"]");
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

    private SgGroup parent;

    public final @Nullable
    SgGroup getParent() {
        return parent;
    }

    final void setParent(@Nullable SgGroup parent) {
        this.parent = parent;
    }



    public static abstract class Builder<B extends Builder<B,N>, N extends SgNode> {

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

        private float tx,ty = 0f;
        private float sx = 1f,sy = 1f;
        private float r = 0f;

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


        protected float getActualPivotX(@NonNull SgNode node) {
            if (Float.isNaN(px)) {
                return (node.getLocalBoundsRight() + node.getLocalBoundsLeft()) / 2f;
            } else {
                return px;
            }
        }

        protected float getActualPivotY(@NonNull SgNode node) {
            if (Float.isNaN(py)) {
                return (node.getLocalBoundsBottom() + node.getLocalBoundsTop()) / 2f;
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


        public final float getTransformedLeft(@NonNull SgNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[0];
        }

        public final float getTransformedRight(@NonNull SgNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[2];
        }

        public final float getTransformedTop(@NonNull SgNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[1];
        }

        public final float getTransformedBottom(@NonNull SgNode node) {
            refreshTransformedBoundsIfNeeded(node);
            return transformedBounds[3];
        }


        protected final void markTransformedBoundsDirty() {
            matrix = null;
            transformedBoundsDirty = true;
        }

        public final @NonNull Bounds getTransformedBounds(@NonNull SgNode node, @Nullable Bounds bounds) {
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


        private void refreshTransformedBoundsIfNeeded(@NonNull SgNode node) {
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

        private Matrix computeMatrix(@NonNull SgNode node) {
            if (tx == 0f && ty == 0f && r == 0f && sx == 1f && sy == 1f) {
                return IDENTITY_MATRIX;
            } // else
            Matrix m = new Matrix();
            if (sx != 1f || sy != 1f) {
                m.postScale(sx, sy, getActualPivotX(node), getActualPivotY(node));
            }
            if (r != 0f) {
                m.postRotate(r, getActualPivotX(node), getActualPivotY(node));
            }
            if (tx != 0f || ty != 0f) {
                m.postTranslate(tx, ty);
            }
            return m;
        }

        public final @NonNull Matrix getMatrix(@NonNull SgNode node) {
            if (matrix == null) {
                matrix = computeMatrix(node);
            }
            return matrix;
        }

        public final @Nullable Matrix getInverseMatrix(@NonNull SgNode node) {
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



