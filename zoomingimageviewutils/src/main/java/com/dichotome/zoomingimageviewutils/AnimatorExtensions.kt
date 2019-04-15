package com.dichotome.zoomingimageviewutils


fun <H : AnimationHelper> H.addTo(collection: MutableCollection<H>) = apply {
    collection.add(this)
}

fun Collection<AnimationHelper>.evaluateAll() = forEach {
    it.end()
    when (it) {
        is SimpleAnimationHelper -> it.evaluate()
        is LinearAnimationHelper -> it.evaluate()
        is PlainAnimationHelper -> it.evaluateXY()
    }
}

fun Collection<AnimationHelper?>.endAll() = forEach {
    it?.end()
}

fun Collection<AnimationHelper?>.cancelAll() = forEach {
    it?.cancel()
}