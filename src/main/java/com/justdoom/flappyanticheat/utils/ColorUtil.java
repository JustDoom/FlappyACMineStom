package com.justdoom.flappyanticheat.utils;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtil {

    public static TextComponent translate(String message) {
        return LegacyComponentSerializer.legacy('&').deserialize(message);
    }
}