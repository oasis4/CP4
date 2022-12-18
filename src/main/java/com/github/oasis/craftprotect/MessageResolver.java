package com.github.oasis.craftprotect;

import com.github.oasis.craftprotect.api.CraftProtect;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageResolver implements TagResolver {

    private final CraftProtect protect;

    public MessageResolver(CraftProtect protect) {
        this.protect = protect;
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        //noinspection DataFlowIssue
        return Tag.preProcessParsed(protect.getUnformattedMessages().getString(name));
    }

    @Override
    public boolean has(@NotNull String name) {
        return protect.getUnformattedMessages().isString(name);
    }
}
