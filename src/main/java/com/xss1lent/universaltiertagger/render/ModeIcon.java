package com.xss1lent.universaltiertagger.render;

import com.xss1lent.universaltiertagger.data.GameMode;

public class ModeIcon {

    private ModeIcon() {
    }

    /**
     * Returns the icon identifier used for a game mode.
     */
    public static String getIcon(GameMode mode) {

        if (mode == null) {
            return "crystal";
        }

        return switch (mode) {

            case SWORD -> "sword";
            case AXE -> "axe";
            case POT -> "pot";
            case VANILLA -> "vanilla";
            case SMP -> "smp";
            case NETHOP -> "nethop";
            case UHC -> "uhc";
            case MACE -> "mace";
            case CRYSTAL -> "crystal";

            default -> "crystal";
        };
    }

    /**
     * MCPVP mode aliases.
     */
    public static String getMCPVPIcon(String modeName) {

        if (modeName == null) {
            return "crystal";
        }

        String mode = modeName.toLowerCase()
                .replace(" ", "")
                .replace("-", "");

        return switch (mode) {

            case "shield" -> "axe";

            case "endfight" -> "vanilla";

            case "cpvplategame" -> "crystal";

            case "smpearly" -> "uhc";

            case "spear" -> "spear";

            default -> "crystal";
        };
    }
}
