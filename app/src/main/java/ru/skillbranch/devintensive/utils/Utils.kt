package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.trim()?.split(" ")
        var firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        if (firstName?.length == 0 ) {
            firstName = null
        }

        // return Pair(firstName, lastName)
        return firstName to lastName
    }
}
