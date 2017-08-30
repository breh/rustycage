package rustycage;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import rustycage.animation.AbstractTransition;
import rustycage.event.SgEvent;
import rustycage.event.SgEventListener;
import rustycage.event.TouchDownEvent;
import rustycage.event.TouchEnterEvent;
import rustycage.event.TouchEvent;
import rustycage.event.TouchEventListener;
import rustycage.event.TouchExitEvent;
import rustycage.event.TouchMoveEvent;
import rustycage.event.TouchUpEvent;
import rustycage.impl.Bounds;
import rustycage.util.Preconditions;

/**
 *
 * A class representing a node in a scene graph. A node can be transformed using affine transformation
 * and can be made translucent using opacity attribute.
 *
 * Created by breh on 9/9/16.
 */
public abstract class SgNode {

    private static final String TAG = "SgNode";

    // FIXME - all these fields need memory optimization
    private String id;
    private SgNode parent;

    private float[] localBounds = new float[4];

    private boolean dirty = true;
    private boolean localBoundsDirty = true;

    private float opacity = 1f;

    SgNode() {
    }

    /**
     * Gets parent of this node. Can be null.
     * @return
     */
    public final @Nullable SgNode getParent() {
        return parent;
    }

    /**
     * package private - sets parent
     * @param parent
     */
    final void setParent(@Nullable SgNode parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (id != null) {
            sb.append(", id: '").append(id).append("'");
        }
        return sb.toString();
    }


    /**
     * Gets id of this node, or null if no id has been assigned
     * @return id or null
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     * Sets id to this node. If set to null, this node no longer has any id
     * @param id id, can be null.
     */
    public void setId(@Nullable String id) {
        this.id = id;
    }


    /**
     * Translated node in X direction
     * @param tx
     */
    public final void setTranslationX(float tx) {
        getOrCreateTransformationSupport().setTx(tx);
        invalidate();
    }

    /**
     * Translates node in Y direction
     * @param ty
     */
    public final void setTranslationY(float ty) {
        getOrCreateTransformationSupport().setTy(ty);
        invalidate();
    }


    /**
     * Translates node in X and Y directions.
     * @param tx
     * @param ty
     */
    public final void setTranslation(float tx, float ty) {
        NodeTransformationSupport support = getOrCreateTransformationSupport();
        support.setTx(tx);
        support.setTy(ty);
        invalidate();
    }

    /**
     * Scales this node by given factor (applied in both x an y direction). Scaling
     * is happening around specified pivot point (if not specified, center of
     * the node is used)
     * @param s
     */
    public final void setScale(float s) {
        setScale(s, s);
    }

    /**
     * Scales this node by given factor in x and y directions.  Scaling
     * is happening around specified pivot point (if not specified, center of
     * the node is used)
     * @param sx
     * @param sy
     */
    public final void setScale(float sx, float sy) {
        NodeTransformationSupport support = getOrCreateTransformationSupport();
        support.setScaleX(sx);
        support.setScaleY(sy);
        invalidate();
    }

    /**
     * Rotates node by given amount around its pivot point (if not specified, center of
     * the node is used).
     * @param r rotation in degrees
     */
    public final void setRotation(float r) {
        getOrCreateTransformationSupport().setRotation(r);
        invalidate();
    }

    /**
     * Sets pivot point to a speficif value.
     * @param px
     * @param py
     */
    public void setPivot(float px, float py) {
        getOrCreateTransformationSupport().setPivot(px, py);
        invalidate();
    }

    /**
     * Resets pivot point to its default value (center of th node)
     */
    public void resetPivot() {
        getOrCreateTransformationSupport().resetPivot();
        invalidate();
    }

    /**
     * Returns X coordinate of the pivot point. If not set, Float.NaN is returned
     * @return
     */
    public float getPivotX() {
        return transformationSupport != null ? transformationSupport.getPivotX() : Float.NaN;
    }

    /**
     * Returns Y coordinate of the pivot point. If not set, Float.NaN is returned
     * @return
     */
    public float getPivotY() {
        return transformationSupport != null ? transformationSupport.getPivotY() : Float.NaN;
    }


    /**
     * Returns X coordinate of the node translation
     * @return
     */
    public final float getTranslationX() {
        return transformationSupport != null ? transformationSupport.getTx() : 0f;
    }

    /**
     * Returns Y coordinate of the node translation
     * @return
     */
    public final float getTranslationY() {
        return transformationSupport != null ? transformationSupport.getTy() : 0f;
    }

    /**
     * Gets current scale factor. If not uniform, only X value is returned
     * @return
     */
    public final float getScale() {
        return getScaleX();
    }

    /**
     * Gets X value of the scale factor
     * @return
     */
    public final float getScaleX() {
        return transformationSupport != null ? transformationSupport.getSx() : 1f;
    }

    /**
     * Gets Y value of the scale factor
     * @return
     */
    public final float getScaleY() {
        return transformationSupport != null ? transformationSupport.getSy() : 1f;
    }

    /**
     * Gets rotation angle in degrees
     * @return
     */
    public final float getRotation() {
        return transformationSupport != null ? transformationSupport.getR() : 0f;
    }


    /**
     * Gets current opacity of this node
     * @return
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Sets opacity value to this node. Expected range 0-1
     * @param opacity
     */
    public void setOpacity(float opacity) {
        if (opacity > 1f) opacity = 1f;
        if (opacity < 0f) opacity = 0f;
        this.opacity = opacity;
        invalidate();
    }


    /**
     * Invalidates the node. Typically not used by a library user, only when things like SgShape paint
     * object is updated / animated (there is no listener which would allow to listen on
     * changes to the paint object)
     */
    public final void invalidate() {
        if (!dirty) {
            dirty = true;
            if (parent != null) {
                parent.invalidate();
            }
            onInvalidated();
        }
    }

    /**
     * Package private - onInvalidated callback
     */
    void onInvalidated() {
    }


    /**
     * Clears invalidated state
     */
    void clearInvalidated() {
        if (dirty) {
            dirty = false;
        }
    }

    /**
     * Check whether this node has been invalidated
     * @return
     */
    protected final boolean isInvalidated() {
        return dirty;
    }

    /**
     * Gets local left bound of this node (X)
     * @return
     */
    public final float getLocalBoundsLeft() {
        return getLocalBoundsArray()[0];
    }

    /**
     * Gets local right bound of this node (X)
     * @return
     */
    public final float getLocalBoundsRight() {
        return getLocalBoundsArray()[2];
    }

    /**
     * Gets local top bound of this node (Y)
     * @return
     */
    public final float getLocalBoundsTop() {
        return getLocalBoundsArray()[1];
    }

    /**
     * Gets local bottom bound of this node (Y)
     * @return
     */
    public final float getLocalBoundsBottom() {
        return getLocalBoundsArray()[3];
    }


    /**
     * Gets local bounds of this node. Please note, this causes creation
     * of the Bounds object. For more efficient see getLocalBounds({@link Bounds}) method
     * @return
     * // FIXME - should not be public
     */
    @NonNull
    public final Bounds getLocalBounds() {
        return getLocalBounds(null);
    }


    /**
     * Gets local bounds of this node.
     * @param outBounds - a optionally specified bounds object which gets filled by the
     *                  bounds value. Can be null (in which case the bounds object
     *                  gets created and returned)
     * @return bounds of this object
     * // FIXME - should  not be public
     */
    @NonNull
    public final Bounds getLocalBounds(@Nullable Bounds outBounds) {
        if (outBounds == null) {
            outBounds = new Bounds();
        }
        outBounds.set(getLocalBoundsArray());
        return outBounds;
    }

    private final float[] getLocalBoundsArray() {
        refreshLocalBoundsIfNeeded();
        return localBounds;
    }


    /**
     * Invalidates local bounds (should be called when a geometry of this node changes)
     */
    protected final void invalidateLocalBounds() {
        localBoundsDirty = true;
        if (transformationSupport != null) {
            transformationSupport.invalidateTransformedBounds();
        }
        invalidate();
    }


    private void refreshLocalBoundsIfNeeded() {
        if (localBoundsDirty) {
            computeLocalBounds(localBounds);
            localBoundsDirty = false;
        }
    }

    /**
     * Computes local bounds of this node
     * @param bounds - an array representing the local bounds as [left,top,right,bottom] (FIXME - verify)
     */
    protected abstract void computeLocalBounds(@NonNull float[] bounds);


    /**
     * Gets transformed left bound of this node (X)
     * @return
     */
    public final float getTransformedBoundsLeft() {
        return transformationSupport != null ? transformationSupport.getTransformedLeft(this) : getLocalBoundsLeft();
    }

    /**
     * Gets transformed right bound of this node (X)
     * @return
     */
    public final float getTransformedBoundsRight() {
        return transformationSupport != null ? transformationSupport.getTransformedRight(this) : getLocalBoundsRight();
    }

    /**
     * Gets transformed top bound of this node (Y)
     * @return
     */
    public final float getTransformedBoundsTop() {
        return transformationSupport != null ? transformationSupport.getTransformedTop(this) : getLocalBoundsTop();
    }

    /**
     * Gets transformed bottom bounds of this node (Y)
     * @return
     */
    public final float getTransformedBoundsBottom() {
        return transformationSupport != null ? transformationSupport.getTransformedBottom(this) : getLocalBoundsBottom();
    }


    /**
     * Gets transformed bounds of this node
     * @return
     * FIXME - should not be public
     */
    @NonNull
    public final Bounds getTransformedBounds() {
        return transformationSupport != null ? transformationSupport.getTransformedBounds(this, null) : getLocalBounds();
    }

    /**
     * Package private - computes transformed bounds of this object
     * @param transformedBounds
     */
    void computeTransformedBounds(@NonNull float[] transformedBounds) {
        if (transformationSupport != null) {
            NodeTransformationSupport.computeTransformedBounds(getLocalBoundsArray(), transformationSupport.getMatrix(this), transformedBounds);
        } else {
            computeLocalBounds(transformedBounds);
        }
    }


    /**
     * Gets transformation matrix for this node.
     * FIXME - should not be public
     * @return
     */
    @Nullable
    public final Matrix getMatrix() {
        return transformationSupport != null ? transformationSupport.getMatrix(this) : null;
    }


    /**
     * Gets width of this node (in local coordinate system)
     * @return
     */
    public final float getWidth() {
        return Math.abs(getLocalBoundsRight() - getLocalBoundsLeft());
    }

    /**
     * Gets height of this node (in local coordinate system)
     * @return
     */
    public final float getHeight() {
        return Math.abs(getLocalBoundsBottom() - getLocalBoundsTop());
    }


    /**
     * Node transformation support. The main idea is it is instantiated only when
     * there is a transformation applied to this node. If there is no changes, no
     * need ot allocate a new object as default values are being used by the renderer
     */

    private NodeTransformationSupport transformationSupport;

    private NodeTransformationSupport getOrCreateTransformationSupport() {
        if (transformationSupport == null) {
            transformationSupport = new NodeTransformationSupport(getLocalBoundsArray());
        }
        return transformationSupport;
    }


    /**
     * Event delivery support. As with the transformation support, the idea is
     * to instantiate this support only in the case there is event listeners attached
     * to this node.
     */


    /**
     * package private - checks if there is any capture listener on this node
     * @return
     */
    final boolean hasCaptureListener() {
        return captureEventDeliverySupport != null && captureEventDeliverySupport.hasEventListeners();
    }

    /**
     * package private - checks if there is any bubble listener on this tnode
     * @return
     */
    final boolean hasBubbleListener() {
        return eventDeliverySupport != null && eventDeliverySupport.hasEventListeners();
    }


    private SgNodeEventDeliverySupport captureEventDeliverySupport;
    @NonNull
    final SgNodeEventDeliverySupport getCaptureEventDeliverySupport() {
        if (captureEventDeliverySupport == null) {
            captureEventDeliverySupport = new SgNodeEventDeliverySupport();
        }
        return captureEventDeliverySupport;
    }

    private SgNodeEventDeliverySupport eventDeliverySupport;
    @NonNull
    final SgNodeEventDeliverySupport getEventDeliverySupport() {
        if (eventDeliverySupport == null) {
            eventDeliverySupport = new SgNodeEventDeliverySupport();
        }
        return eventDeliverySupport;
    }


    /**
     * Adds a bubbling listener to this node. The specified event class "filters"
     * the events being delivered (only events of this class or its subclasses get delivered to the listener)
     * @param eventClass a class of the listener event - cannot be null
     * @param listener the listener instance - needs to be of specified class or its super class - cannot be null
     * @param <T>
     */
    public final <T extends SgEvent> void addListener(@NonNull Class<T> eventClass, @NonNull SgEventListener<? super T> listener) {
        getEventDeliverySupport().addEventListner(eventClass, listener);
    }

    /**
     * Adds capturing listener to this node. The specified event class "filters"
     * the events being delivered (only events of this class or its subclasses get delivered to the listener)
     * @param eventClass a class of the listener event - cannot be null
     * @param listener the listener instance - needs to be of specified class or its super class - cannot be null
     * @param <T>
     */
    public final <T extends SgEvent> void addCapturingListener(@NonNull Class<T> eventClass, @NonNull SgEventListener<? super T> listener) {
        getCaptureEventDeliverySupport().addEventListner(eventClass, listener);
    }

    /**
     * Removes listener from this node.
     * @param eventClass
     * @param listener
     * @param <T>
     * @return true if removed, false if not found
     */
    public final <T extends SgEvent> boolean removeListener(@NonNull Class<T> eventClass, @NonNull SgEventListener<? super T> listener) {
        boolean result = false;
        if (eventDeliverySupport != null) {
            result = eventDeliverySupport.removeEventListener(eventClass, listener);
        }
        if (captureEventDeliverySupport != null) {
            result |= captureEventDeliverySupport.removeEventListener(eventClass, listener);
        }
        return result;
    }


    /**
     * Dispatches event originating at this node
     * @param event
     */
    public final void dispatchEvent(@NonNull SgEvent event) {
        Preconditions.assertNotNull(event,"event");
        SgNodeEventDeliverySupport.deliverEvent(event, this);
    }



    /**
     * Checks whether given point is within bounds of this node.
     *
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
     * Checks if the given point is in hit target - by default returns true
     * as it assumes if point is withing bounds it is hit target
     * but can be cusomized
     *
     * @param point
     * @return
     */
    boolean isPointInHitTarget(@NonNull float[] point) {
        return true;
    }

    /**
     * Tests is the touch point is in the bounds of this node. If true, converts the touch point
     * to the local coordinates of this node (if applicable)
     *
     * @param touchPoint
     * @return
     */
    final boolean findHitPath(@NonNull SgNodeHitPath nodeHitPath, float[] touchPoint) {
        //Log.d(TAG,"findHitPath: "+this+": touchPoint: ["+touchPoint[0]+", "+touchPoint[1]+"], tbounds: ["+getTransformedBoundsLeft()+", "+getTransformedBoundsRight()
        //        +"; "+getTransformedBoundsTop()+", "+getTransformedBoundsBottom()+"], lbounds: ["+getLocalBoundsLeft()+", "+getLocalBoundsRight()
        //        +"; "+getLocalBoundsTop()+", "+getLocalBoundsBottom()+"]");
        if (isWithinBounds(touchPoint)) {
            // succeeded hit test, now check if it the event hits the node
            // adding to hit path
            float tpX = 0;
            float tpY = 0;
            if (transformationSupport != null) {
                // need to transform to local coordinates
                tpX = touchPoint[0];
                tpY = touchPoint[1];
                getOrCreateTransformationSupport().getInverseMatrix(this).mapPoints(touchPoint);
            }
            //Log.d(TAG,"within bounds: "+this+", localX: "+touchPoint[0]+", localY: "+touchPoint[1]);
            if (isPointInHitTarget(touchPoint)) {
                nodeHitPath.pushNode(this, touchPoint[0], touchPoint[1]);
                searchForHitPath(nodeHitPath, touchPoint);
                return true;
            } else {
                // restore touch point values
                if (transformationSupport != null) {
                    touchPoint[0] = tpX;
                    touchPoint[1] = tpY;
                }
            }
        } // else failed hit test
        return false;
    }

    // looks for children nodes
    void searchForHitPath(@NonNull SgNodeHitPath nodeHitPath, float[] touchPoint) {
    }

    /*
     * Builder
     */


    /**
     * Abstract builder for all nodes
     * @param <B>
     * @param <N>
     */
    public static abstract class Builder<B extends Builder<B, N>, N extends SgNode> {

        private final N node;

        protected Builder(@NonNull N node) {
            this.node = node;
        }

        protected
        @NonNull
        N getNode() {
            return node;
        }

        @NonNull
        @SuppressWarnings("unchecked")
        protected final B getBuilder() {
            return (B) this;
        }

        /**
         * translates the node to x and y
         * @param x
         * @param y
         * @return
         */
        @NonNull
        public final B txy(float x, float y) {
            getNode().setTranslation(x, y);
            return getBuilder();
        }

        /**
         * translates the node to x
         * @param x
         * @return
         */
        @NonNull
        public final B tx(float x) {
            getNode().setTranslationX(x);
            return getBuilder();
        }

        /**
         * translates the node to y
         * @param y
         * @return
         */
        @NonNull
        public final B ty(float y) {
            getNode().setTranslationY(y);
            return getBuilder();
        }

        /**
         * Rotates the node to given angle (degrees)
         * @param r
         * @return
         */
        @NonNull
        public final B r(float r) {
            getNode().setRotation(r);
            return getBuilder();
        }

        /**
         * Scales node by given scale factor
         * @param s
         * @return
         */
        @NonNull
        public final B s(float s) {
            getNode().setScale(s);
            return getBuilder();
        }

        /**
         * Sets id to given node
         * @param id  - cannot be null
         * @return
         */
        @NonNull
        public final B id(@NonNull String id) {
            getNode().setId(id);
            return getBuilder();
        }

        /**
         * Sets pivot point of this node
         * @param px
         * @param py
         * @return
         */
        @NonNull
        public final B pivot(float px, float py) {
            getNode().setPivot(px, py);
            return getBuilder();
        }

        /**
         * Sets opacity (value 0-1)
         * @param opacity
         * @return
         */
        @NonNull
        public final B opacity(float opacity) {
            getNode().setOpacity(opacity);
            return getBuilder();
        }

        /**
         * Adds on touch listener
         * @param listener
         * @return
         */
        @NonNull
        public final B onTouch(@NonNull SgEventListener<TouchEvent> listener) {
            Preconditions.assertNotNull(listener, "listener");
            getNode().addListener(TouchEvent.class, listener);
            return getBuilder();
        }


        /**
         * Adds on touch down listener
         * @param listener
         * @return
         */
        @NonNull
        public final B onTouchDown(@NonNull SgEventListener<? super TouchDownEvent> listener) {
            Preconditions.assertNotNull(listener, "listener");
            getNode().addListener(TouchDownEvent.class, listener);
            return getBuilder();
        }

        /**
         * Adds on touch up listener
         * @param listener
         * @return
         */
        @NonNull
        public final B onTouchUp(@NonNull SgEventListener<? super TouchUpEvent> listener) {
            Preconditions.assertNotNull(listener, "listener");
            getNode().addListener(TouchUpEvent.class, listener);
            return getBuilder();
        }

        /**
         * Adds on touch move listener
         * @param listener
         * @return
         */
        @NonNull
        public final B onTouchMove(@NonNull SgEventListener<? super TouchMoveEvent> listener) {
            Preconditions.assertNotNull(listener, "listener");
            getNode().addListener(TouchMoveEvent.class, listener);
            return getBuilder();
        }

        /**
         * Adds on touch enter listener
         * @param listener
         * @return
         */
        @NonNull
        public final B onTouchEnter(@NonNull SgEventListener<? super TouchEnterEvent> listener) {
            Preconditions.assertNotNull(listener, "listener");
            getNode().addListener(TouchEnterEvent.class, listener);
            return getBuilder();
        }

        /**
         * Adds on touch exit listener
         * @param listener
         * @return
         */
        @NonNull
        public final B onTouchExit(@NonNull SgEventListener<? super TouchExitEvent> listener) {
            Preconditions.assertNotNull(listener, "listener");
            getNode().addListener(TouchExitEvent.class, listener);
            return getBuilder();
        }

        /**
         * Specifies transition started on touch down
         * @param transition
         * @return
         */
        @NonNull
        public final B onTouchDownTransition(final @NonNull AbstractTransition<?,?>  transition) {
            Preconditions.assertNotNull(transition, "transition");
            onTouchDown(new TouchEventListener() {
                @Override
                public void onEvent(@NonNull TouchEvent event, @NonNull SgNode currentNode, boolean isCapturePhase) {
                    transition.start();
                }
            });
            return getBuilder();
        }

        /**
         * Sppecifies transition started on touch up
         * @param transition
         * @return
         */
        @NonNull
        public final B onTouchUpTransition(final @NonNull AbstractTransition<?,?>  transition) {
            Preconditions.assertNotNull(transition, "transition");
            onTouchUp(new TouchEventListener() {
                @Override
                public void onEvent(@NonNull TouchEvent event, @NonNull SgNode currentNode, boolean isCapturePhase) {
                    transition.start();
                }
            });
            return getBuilder();
        }

        /**
         * Builds the node
         * @return
         */
        public N build() {
            return node;
        }


    }


    /*
     * Node transformation support
     */


    private final static class NodeTransformationSupport {

        private float tx, ty = 0f;
        private float sx = 1f, sy = 1f;
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
            invalidateTransformedBounds();
        }

        public final void setTy(float ty) {
            this.ty = ty;
            invalidateTransformedBounds();
        }


        public final void setScaleX(float sx) {
            this.sx = sx;
            invalidateTransformedBounds();
        }

        public final void setScaleY(float sy) {
            this.sy = sy;
            invalidateTransformedBounds();
        }


        public final void setRotation(float r) {
            this.r = r;
            invalidateTransformedBounds();
        }

        public void setPivot(float px, float py) {
            this.px = px;
            this.py = py;
            invalidateTransformedBounds();
        }

        public void resetPivot() {
            this.px = Float.NaN;
            this.py = Float.NaN;
            invalidateTransformedBounds();
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


        protected final void invalidateTransformedBounds() {
            matrix = null;
            transformedBoundsDirty = true;
        }

        @NonNull
        public final Bounds getTransformedBounds(@NonNull SgNode node, @Nullable Bounds bounds) {
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
                computeTransformedBounds(node.getLocalBoundsArray(), getMatrix(node), transformedBounds);
                transformedBoundsDirty = false;
            }
        }


        private static void computeTransformedBounds(@NonNull float[] localBounds, @NonNull Matrix matrix, @Nullable float[] transformedBounds) {
            if (!matrix.isIdentity()) {
                matrix.mapPoints(transformedBounds, localBounds);
            } else {
                transformedBounds[0] = localBounds[0];
                transformedBounds[1] = localBounds[1];
                transformedBounds[2] = localBounds[2];
                transformedBounds[3] = localBounds[3];
            }
        }


        protected final boolean isMatrixValid() {
            return matrix == null;
        }

        @NonNull
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

        @NonNull
        public final Matrix getMatrix(@NonNull SgNode node) {
            if (matrix == null) {
                matrix = computeMatrix(node);
            }
            return matrix;
        }

        @Nullable
        public final Matrix getInverseMatrix(@NonNull SgNode node) {
            if (inverseMatrix == null) {
                Matrix inverse = new Matrix();
                boolean inverted = getMatrix(node).invert(inverse);
                if (inverted) {
                    inverseMatrix = inverse;
                } else {
                    Log.w(TAG, "Cannot compute inverted matrix");
                }
            }
            return inverseMatrix;
        }
    }


}



