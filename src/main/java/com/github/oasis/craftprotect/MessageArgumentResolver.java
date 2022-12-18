package com.github.oasis.craftprotect;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageArgumentResolver implements TagResolver {

    private final Object[] objects;

    public MessageArgumentResolver(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        int i = Integer.parseInt(name);
        return Tag.inserting(Component.text(objects[i].toString()));
    }

    @Override
    public boolean has(@NotNull String name) {
        try {
            int i = Integer.parseInt(name);
            return objects.length > i;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
