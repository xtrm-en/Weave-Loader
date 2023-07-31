@file:Suppress("invisible_reference", "invisible_member")

package net.weavemc.weave.api.hooks

import net.weavemc.weave.api.Hook
import net.weavemc.weave.api.bytecode.asm
import net.weavemc.weave.api.bytecode.callEvent
import net.weavemc.weave.api.bytecode.internalNameOf
import net.weavemc.weave.api.bytecode.search
import net.weavemc.weave.api.event.EntityListEvent
import net.weavemc.weave.api.getMappedClass
import net.weavemc.weave.api.getMappedMethod
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

/**
 * @see net.minecraft.world.World.spawnEntityInWorld
 */
class EntityListEventAddHook : Hook(getMappedClass("net/minecraft/world/World")) {
    override fun transform(node: ClassNode, cfg: AssemblerConfig) {
        val mappedMethod = getMappedMethod(
            "net/minecraft/world/World",
            "spawnEntityInWorld",
            "(Lnet/minecraft/entity/Entity;)Z"
        ) ?: error("Failed to find mapping for World#spawnEntityInWorld")

        node.methods.search(mappedMethod.name, mappedMethod.descriptor).instructions.insert(asm {
            new(internalNameOf<EntityListEvent.Add>())
            dup
            aload(1)
            invokespecial(
                internalNameOf<EntityListEvent.Add>(),
                "<init>",
                "(L${getMappedClass("net/minecraft/entity/Entity")};)V"
            )
            callEvent()
        })
    }
}

/**
 * @see net.minecraft.client.multiplayer.WorldClient.removeEntityFromWorld
 */
class EntityListEventRemoveHook : Hook(getMappedClass("net/minecraft/client/multiplayer/WorldClient")) {
    override fun transform(node: ClassNode, cfg: AssemblerConfig) {
        val mappedMethod = getMappedMethod(
            "net/minecraft/client/multiplayer/WorldClient",
            "removeEntityFromWorld",
            "(I)Lnet/minecraft/entity/Entity;"
        ) ?: error("Failed to find mapping for WorldClient#removeEntityFromWorld")

        val mn = node.methods.search(mappedMethod.name, mappedMethod.descriptor)
        mn.instructions.insert(mn.instructions.find { it.opcode == Opcodes.IFNULL }, asm {
            new(internalNameOf<EntityListEvent.Remove>())
            dup
            aload(2)
            invokespecial(
                internalNameOf<EntityListEvent.Remove>(),
                "<init>",
                "(L${getMappedClass("net/minecraft/entity/Entity")};)V"
            )
            callEvent()
        })
    }
}