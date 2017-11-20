package rustycage.kt
import android.content.res.Resources
import android.graphics.Bitmap
import rustycage.*
import rustycage.util.PaintBuilder

/**
 * Created by Martin Brehovsky on 11/2/17.
 */

abstract class NodeBuilder<T : SgNode> {

    var tx : Number? = null
    var ty : Number? = null
    var r : Number? = null
    var s: Number? = null
    var sx: Number? = null
    var sy: Number? = null
    var px: Number? = null
    var py: Number? = null
    var opacity: Float? = null
    var id: String? = null
    

    protected open fun fillAttributes(node : T) {
        tx?.let {node.translationX = it as Float}
        ty?.let {node.translationY = it as Float}
        r?.let { node.rotation = it as Float}
        s?.let {node.scale = it as Float}
        sx?.let {node.setScale(it as Float, node.scaleY)}
        sy?.let {node.setScale(node.scaleX, it as Float)}
        px?.let { node.setPivot(it as Float, node.pivotY)}
        py?.let { node.setPivot(node.pivotX, it as Float)}
        opacity?.let {node.opacity = it}
        id?.let {node.id = it}


    }

    //abstract fun <B : SgNode.Builder<B,N>, N : SgNode> buildSceneGraphNodeBuilder() : SgNode.Builder<in B, in N>

    abstract fun buildSceneGraph() : T

}

class GroupBuilder : NodeBuilder<SgGroup>() {

    private val children = arrayListOf<NodeBuilder<out SgNode>>()

    /*override fun <B : SgNode.Builder<B, N>, N : SgNode> buildSceneGraphNodeBuilder(): SgNode.Builder<in B, in N> {
        return SgGroup.create()
    }*/

    override fun buildSceneGraph(): SgGroup {
        val nodeBuilder = SgGroup.create()
        children.forEach { nodeBuilder.add( it.buildSceneGraph() ) }
        val node = nodeBuilder.build()
        fillAttributes(node)
        return node
    }

}

inline fun group(init: GroupBuilder.() -> Unit) : GroupBuilder {
    val builder = GroupBuilder()
    builder.init()
    return builder
}



class ImageBuilder : NodeBuilder<SgImage>() {

    var bitmap : Bitmap? = null
    var resources: Resources? = null
    var resourceId: Int? = null

    override fun buildSceneGraph(): SgImage {
        var imageNode : SgImage? = null
        if (resources != null && resourceId != null) {
            imageNode = SgImage.createWithResource(resources as Resources, resourceId as Int).build()
        }
        bitmap?.let { imageNode = SgImage.createWithBitmap(it).build() }
        if (imageNode == null) {
            imageNode = SgImage.createEmpty().build()
        }
        fillAttributes(imageNode as SgImage)
        return imageNode as SgImage
    }
}

inline fun image(init: ImageBuilder.() -> Unit) : ImageBuilder {
    val builder = ImageBuilder()
    builder.init()
    return builder
}

inline fun image(bitmap : Bitmap) : ImageBuilder = image{this.bitmap = bitmap}

inline fun image(resources: Resources, id: Int) : ImageBuilder  = image{this.resources = resources; this.resourceId = id}

class PaintBuilderK {

    var paintBuilder: PaintBuilder? = null


}


abstract class ShapeBuilder<T : SgShape> : NodeBuilder<T>() {
    var paintBuilder : PaintBuilderK? = null

    override fun fillAttributes(node: T) {
        super.fillAttributes(node)
        paintBuilder?.let { node.paint = it.paintBuilder?.build()}
    }
}

class LineBuilder : ShapeBuilder<SgLine>() {

    var x1: Number = 0f
    var y1: Number = 0f
    var x2: Number? = null
    var y2: Number? = null

    var width: Number? = null
    var height: Number? = null


    override fun buildSceneGraph(): SgLine {
        if (x2 == null && width != null) {
            x2 = x1 as Float + width as Float
        }
        if (y2 == null && height != null) {
            y2 = y1 as Float + height as Float
        }
        val x1f = x1 as Float
        val x2f = x2 as Float? ?: x1f
        val y1f = y1 as Float
        val y2f = y2 as Float? ?: y1f

        val node = SgLine.createWithPoints(x1f, y1f, x2f, y2f).build()
        fillAttributes(node)
        return node
    }
}

inline fun line(init: LineBuilder.() -> Unit) : LineBuilder {
    val builder = LineBuilder()
    builder.init()
    return builder
}

class RectangleBuilder : ShapeBuilder<SgRectangle>() {

    var x1: Number = 0f
    var y1: Number = 0f
    var x2: Number? = null
    var y2: Number? = null

    var width: Number? = null
    var height: Number? = null

    var rx: Number? = null
    var ry: Number? = null



    override fun buildSceneGraph(): SgRectangle {
        if (x2 == null && width != null) {
            x2 = x1 as Float + width as Float
        }
        if (y2 == null && height != null) {
            y2 = y1 as Float + height as Float
        }
        val x1f = x1 as Float
        val x2f = x2 as Float? ?: x1f
        val y1f = y1 as Float
        val y2f = y2 as Float? ?: y1f

        val rxf = rx as Float? ?: 0f
        val ryf = ry as Float? ?: 0f

        val node = SgRectangle.createWithPoints(x1f, y1f, x2f, y2f, rxf, ryf).build()
        fillAttributes(node)
        return node
    }
}

inline fun rectangle(init: LineBuilder.() -> Unit) : LineBuilder {
    val builder = LineBuilder()
    builder.init()
    return builder
}



class EllipseBuilder : ShapeBuilder<SgEllipse>() {
    var rx: Number? = null
    var ry: Number? = null
    var cx: Number? = null
    var cy: Number? = null

    override fun buildSceneGraph(): SgEllipse {
        val node = SgEllipse.createEllipse((rx as Float?)?: 0f, (ry as Float?)?: 0f,
                (cx as Float?)?: 0f, (cy as Float?)?: 0f).build()
        fillAttributes(node)
        return node
    }
}

inline fun ellipse(init: EllipseBuilder.() -> Unit) : EllipseBuilder {
    val builder = EllipseBuilder()
    builder.init()
    return builder
}


class ArcBuilder: ShapeBuilder<SgArc>() {
    var left: Number = 0f
    var right: Number = 0f
    var top: Number = 0f
    var bottom: Number = 0f

    var startAngle: Number = 0f
    var sweepAngle: Number = 0f

    var useCenter: Boolean = true

    override fun buildSceneGraph(): SgArc {
        val arc =  SgArc.create(left as Float, top as Float, right as Float, bottom as Float,
                                startAngle as Float, sweepAngle as Float).build()
        arc.isUseCenter = useCenter
        fillAttributes(arc)
        return arc
    }
}

inline fun arc(init: ArcBuilder.() -> Unit) : ArcBuilder {
    val builder = ArcBuilder()
    builder.init()
    return builder
}

class PathBuilder: ShapeBuilder<SgPath>() {

    var path : SgPath.Builder = SgPath.create()

    override fun buildSceneGraph(): SgPath {
        val node = path.build()
        fillAttributes(node)
        return node;
    }
}

inline fun path(init: PathBuilder.() -> Unit) : PathBuilder {
    val builder = PathBuilder()
    builder.init()
    return builder
}



class TextBuilder : ShapeBuilder<SgText>() {

    var text : String? = null
    var x : Number? = null
    var y : Number? = null


    override fun buildSceneGraph(): SgText {
        var builder = SgText.create(text)
        x?.let { builder.x(it as Float) }
        y?.let { builder.y(it as Float) }
        return builder.build()
    }

}

inline fun text(init: TextBuilder.() -> Unit) : TextBuilder {
    val builder = TextBuilder()
    builder.init()
    return builder
}


class CustomNodeBuilder(val customNodeCreator : () -> SgCustomNode) : NodeBuilder<SgCustomNode>() {

    override fun buildSceneGraph(): SgCustomNode = customNodeCreator()

}


val xx = group {
    tx = 10f
    ty = 13f
    line {
        x1 = 10f
        x2 = 10f
        width = 30f
        height = 40f
        r = 12
        image {
            bitmap = null
        }
        text {
            text = "foo"
        }
        ellipse {
            rx = 10; ry = 30
        }
        path {
            path = SgPath.create().lineTo(20f,30f)
        }
    }
    group {
    }
}



