package net.weavemc.api.bytecode

import net.weavemc.loader.api.event.Event
import net.weavemc.loader.api.event.EventBus
import net.weavemc.internals.InsnBuilder
import net.weavemc.internals.internalNameOf

fun InsnBuilder.postEvent() {
    invokestatic(
        internalNameOf<EventBus>(),
        "postEvent",
        "(L${internalNameOf<Event>()};)V"
    )
}