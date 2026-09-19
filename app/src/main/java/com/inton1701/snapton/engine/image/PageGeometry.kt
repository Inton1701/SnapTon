package com.inton1701.snapton.engine.image

import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Float, val y: Float) {
    init {
        require(x in 0f..1f && y in 0f..1f) { "Point must be normalized to [0, 1]" }
    }
}

data class PageCorners(
    val topLeft: Point,
    val topRight: Point,
    val bottomRight: Point,
    val bottomLeft: Point,
) {
    val points: List<Point> get() = listOf(topLeft, topRight, bottomRight, bottomLeft)

    init {
        require(area() >= MIN_AREA) { "Page polygon is too small" }
    }

    fun area(): Float {
        var sum = 0f
        points.forEachIndexed { index, point ->
            val next = points[(index + 1) % points.size]
            sum += point.x * next.y - next.x * point.y
        }
        return kotlin.math.abs(sum) / 2f
    }

    companion object {
        const val MIN_AREA = 0.04f

        fun fromUnordered(points: List<Point>): PageCorners {
            require(points.size == 4) { "A page must have four corners" }
            val centerX = points.map { it.x }.average().toFloat()
            val centerY = points.map { it.y }.average().toFloat()
            val clockwise = points.sortedBy { atan2((it.y - centerY).toDouble(), (it.x - centerX).toDouble()) }
            val topLeftIndex = clockwise.indices.minBy { clockwise[it].x + clockwise[it].y }
            val rotated = (clockwise.drop(topLeftIndex) + clockwise.take(topLeftIndex))
            val result = PageCorners(rotated[0], rotated[1], rotated[2], rotated[3])
            require(result.isConvex()) { "Page polygon must be convex" }
            return result
        }
    }

    fun isConvex(): Boolean {
        val signs = points.indices.map { index ->
            val a = points[index]
            val b = points[(index + 1) % points.size]
            val c = points[(index + 2) % points.size]
            (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x)
        }
        return signs.all { it >= 0f } || signs.all { it <= 0f }
    }

    fun maxDisplacement(other: PageCorners): Float = points.zip(other.points).maxOf { (a, b) ->
        max(kotlin.math.abs(a.x - b.x), kotlin.math.abs(a.y - b.y))
    }

    fun clamped(): PageCorners = PageCorners(
        Point(min(1f, max(0f, topLeft.x)), min(1f, max(0f, topLeft.y))),
        Point(min(1f, max(0f, topRight.x)), min(1f, max(0f, topRight.y))),
        Point(min(1f, max(0f, bottomRight.x)), min(1f, max(0f, bottomRight.y))),
        Point(min(1f, max(0f, bottomLeft.x)), min(1f, max(0f, bottomLeft.y))),
    )
}
