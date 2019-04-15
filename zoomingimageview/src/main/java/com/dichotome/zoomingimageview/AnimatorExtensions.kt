package com.dichotome.zoomingimageview


fun <H : AnimationHelper> H.addTo(collection: MutableCollection<H>) = apply {
    collection.add(this)
}

fun Collection<AnimationHelper>.evaluateAll() = forEach {
    it.end()
    when (it) {
        is LinearAnimationHelper -> it.evaluate()
    }
}

fun Collection<AnimationHelper?>.endAll() = forEach {
    it?.end()
}

fun Collection<AnimationHelper?>.cancelAll() = forEach {
    it?.cancel()
}