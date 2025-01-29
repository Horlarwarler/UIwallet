package com.crezent.finalyearproject.core.presentation.util

fun String.transformString(character: String, separatorIndex: Int): String {
    if (length <= separatorIndex) return this

    val repeatCount = if (length % separatorIndex == 0) {
        length / separatorIndex
    } else {
        (length / separatorIndex) + 1
    }

    var startIndex = 0
    var atLastIndex = false

    val substring = buildString {
        repeat(repeatCount) {
            println("Repeat count $repeatCount and current count is $it")

            val endIndex = if (it + 1 < repeatCount) {
                startIndex + separatorIndex
            } else {
                atLastIndex = true
                startIndex + (this@transformString.length - startIndex)
            }

            println("End Index $endIndex Start Index $startIndex")
            append(this@transformString.substring(startIndex, endIndex))

            if (!atLastIndex) {
                append(character)
                startIndex += separatorIndex
            }
        }
    }

    return substring
}

//fun String.transformString(character: String, separatorIndex: Int): String {
//    if (length <= separatorIndex) return this
//
//    val result = buildString {
//        var startIndex = 0
//        while (startIndex < this@transformString.length) {
//            val endIndex = (startIndex + separatorIndex).coerceAtMost(this@transformString.length)
//            append(this@transformString.substring(startIndex, endIndex))
//
//            // Add the separator only if it's not the last chunk
//            if (endIndex < this@transformString.length) {
//                append(character)
//            }
//
//            startIndex = endIndex
//        }
//    }
//    return result
//}


fun String.unTransformString(): String {
    return this.filter {
        it.isLetterOrDigit()
    }
}