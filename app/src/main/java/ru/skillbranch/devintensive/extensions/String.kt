package ru.skillbranch.devintensive.extensions

fun String.truncate(maxLength: Int = 16): String {
    var resultString = this.trim()
    if (resultString.length > maxLength) {
        resultString = resultString.substring(0, maxLength).trim() + "..."
    }
    return resultString
}

fun String.stripHtml(): String {
    val htmlRegexp = Regex("(<.*?>)|(&[a-z]{1,4}?;)")
    val spaceRegexp = Regex("\\s{2,}")
    return this.replace(htmlRegexp, "").replace(spaceRegexp, " ")
}
