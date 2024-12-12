package com.innoveworkshop.angryflappingbird.engine

import kotlin.math.sqrt

class Vector(@JvmField var x: Float, @JvmField var y: Float)

fun SomeVectors(
    vec1: Vector,
    vec2: Vector
) : Vector{
    return Vector(vec1.x + vec2.x, vec1.y + vec2.y)
}

fun SubtractingVectors(
    vec1: Vector,
    vec2: Vector
) : Vector{
    return Vector(vec1.x - vec2.x, vec1.y - vec2.y)
}

fun MultiplyVectors(
    vec1: Vector,
    vec2: Vector
) : Vector{
    return Vector(vec1.x * vec2.x, vec1.y * vec2.y)
}

fun MultiplyVector(
    vec1: Vector,
    num: Float
) : Vector{
    return Vector(vec1.x * num, vec1.y * num)
}

fun DivideVectors(
    vec1: Vector,
    vec2: Vector
) : Vector{
    return Vector(vec1.x / vec2.x, vec1.y / vec2.y)
}

fun DivideVector(
    vec1: Vector,
    num: Float
) : Vector{
    return Vector(vec1.x / num, vec1.y / num)
}

fun MagnitudeVector(
    vec: Vector
) : Float{
    return sqrt((vec.x * vec.x) + (vec.y * vec.y))
}

fun NormalizeVector(
    vec : Vector
) : Vector{
    return DivideVector(vec, MagnitudeVector(vec))
}
