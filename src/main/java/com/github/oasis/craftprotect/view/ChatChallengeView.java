package com.github.oasis.craftprotect.view;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.model.ChallengeModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.util.ChatPaginator;

import java.text.DecimalFormat;
import java.util.Arrays;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

public class ChatChallengeView {

    public static Component getDisplay(CraftProtect protect, ChallengeModel model, int width, int leftPadding) {

        float progress = Math.min(((float) model.getCurrent()) / (float) model.getGoal(), 1f);

        int leftWidth = width - leftPadding;

        String[] strings = ChatPaginator.wordWrap(model.getName(), leftWidth);
        Component heading = Component.join(JoinConfiguration.builder()
                        .separator(newline())
                        .prefix(text(" ".repeat(leftPadding)))
                        .build(), Arrays.stream(strings)
                        .map(s -> text().content(s).decorate(TextDecoration.UNDERLINED))
                        .toArray(ComponentLike[]::new))
                .color(NamedTextColor.GREEN);


        strings = ChatPaginator.wordWrap(model.getDescription(), leftWidth);
        Component description = Component.join(JoinConfiguration.builder()
                .separator(newline().append(text(" ".repeat(leftPadding))))
                .prefix(text(" ".repeat(leftPadding)))
                .build(), Arrays.stream(strings)
                .map(s -> text().content(s))
                .toArray(ComponentLike[]::new)).color(NamedTextColor.GRAY);

        DecimalFormat format = new DecimalFormat("0.00");
        return text()
                .append(protect.getFullPrefix())
                .appendNewline()
                //.append(Component.text(" ".repeat(leftPadding)))
                .appendNewline()
                .append(heading)
                .appendNewline()
                .appendNewline()
                //.append(Component.text(" ".repeat(leftPadding)))
                .append(description)
                .appendNewline()
                .appendNewline()
                .append(text(" ".repeat(leftPadding)))
                .append(text("|".repeat((int) (leftWidth * progress))).color(NamedTextColor.GREEN))
                .append(text("|".repeat((int) (leftWidth * (1f - progress)))).color(NamedTextColor.GRAY))
                .appendSpace()
                .append(text().color(NamedTextColor.GRAY)
                        .append(text("(")
                                .append(text(model.getCurrent()))
                                .append(text("/"))
                                .append(text(model.getGoal()))
                                .append(text(")"))
                                .appendSpace()
                                .append(text("["))
                                .append(text(format.format(progress * 100) + "%").color(NamedTextColor.GREEN))
                                .append(text("]"))))
                .appendNewline()
                .asComponent();
    }

}
