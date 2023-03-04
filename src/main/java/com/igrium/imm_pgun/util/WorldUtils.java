package com.igrium.imm_pgun.util;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;

public class WorldUtils {
    private static final TypeFilter<Entity, Entity> ALL_ENTITIES = new TypeFilter<>() {

        @Override
        public Entity downcast(Entity ent) {
            return ent;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
        
    };

    /**
     * For some reason, world.collectEntitiesByType requires the type filter extend
     * Entity, which makes things really difficult when checking for interfaces.
     * This method uses some generic fuckary to override that.
     * 
     * @param <T>       The type of interface to test against.
     * @param clazz     The interface class.
     * @param world     The world to look in.
     * @param predicate A predicate to test all entities against.
     * @param result    Where to put all the found entities.
     */
    @SuppressWarnings("unchecked")
    public static <T> void collectEntitiesbyInterface(Class<T> clazz, ServerWorld world, Predicate<T> predicate, List<? super T> result) {
        world.collectEntitiesByType(ALL_ENTITIES, ent -> clazz.isInstance(ent) && predicate.test((T) ent), (List<Entity>) result);
    }
}
