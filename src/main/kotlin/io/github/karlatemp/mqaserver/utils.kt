package io.github.karlatemp.mqaserver

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

val Array<BaseComponent>.component: TextComponent get() = TextComponent(*this)


fun String.insert(index: Int, value: Any, pre: Any = "", suf: Any = ""): String = buildString {
    append(pre)
    append(this@insert, 0, index)
    append(value)
    append(this@insert, index, this@insert.length)
    append(suf)
}
