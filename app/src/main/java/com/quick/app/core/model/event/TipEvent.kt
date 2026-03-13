package com.quick.app.core.model.event

import cn.qhplus.emo.core.EmoEventProp
import kotlinx.coroutines.channels.BufferOverflow

@EmoEventProp(
    // 是否是 sticky 事件， 默认为 false
    sticky = false,
    // emo 是每个事件类型一个 channel，如果长时间没使用这个 channel， 是否要释放？默认 fasle 表示释放
    keepChannelAlive = false,
    // extraBufferCapacity 与 onBufferOverflow 配合使用，为了处理背压问题
    extraBufferCapacity = 1,
    // 当消息产生速度大于消耗速度时，默认 drop 掉旧的还没消耗的消息
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
class TipEvent(
    val tipErrorRes: Int? = null,
    val tipError: String? = null,
    val tipSuccessRes: Int? = null,
) {
}