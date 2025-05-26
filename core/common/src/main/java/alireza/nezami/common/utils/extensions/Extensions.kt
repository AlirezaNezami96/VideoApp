package alireza.nezami.common.utils.extensions

import java.util.Locale

/**
 * Returns this [Int] value if it is not null, or 0 if it is null.
 *
 * @return The non-null [Int] value or 0.
 */
fun Int?.orZero(): Int {
    return this ?: 0
}

/**
 * Returns this [Double] value if it is not null, or 0.0 if it is null.
 *
 * @return The non-null [Double] value or 0.0.
 */
fun Double?.orZero(): Double {
    return this ?: 0.0
}

/**
 * Returns this [Boolean] value if it is not null, or false if it is null.
 *
 * @return The non-null [Boolean] value or false.
 */
fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

/**
 * Separates the digits of an integer value with commas every three digits.
 *
 * @return The string representation of the integer with comma-separated digits,
 * or an empty string if the input value is null.
 */
fun Int?.formatWithCommas(): String {
    if (this == null) {
        return ""
    }

    val formattedValue = StringBuilder()
    val valueString = this.toString()
    val length = valueString.length

    for (i in 0 until length) {
        formattedValue.append(valueString[i])
        if ((length - i - 1) % 3 == 0 && i < length - 1) {
            formattedValue.append(",")
        }
    }

    return formattedValue.toString()
}

fun String.toTagList(): List<String> = this.split(",").map { it.trim() }.filter { it.isNotEmpty() }

fun Int?.toAbbreviatedString(): String {
    if (this == null) return "0"

    return when {
        this >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format(Locale.getDefault(), "%.1fK", this / 1_000.0)
        else -> this.toString()
    }.replace(".0", "") // Remove .0 if present
}