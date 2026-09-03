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

        };
    }

    /**
     * MCPVP mode aliases.
     *
     * These modes reuse existing icons.
     */
    public static String getMCPVPIcon(String modeName) {

        if (modeName == null) {
            return "crystal";
        }

        String mode = modeName.toLowerCase()
                .replace(" ", "")
                .replace("-", "");

        return switch (mode) {

            // Shield uses Axe icon
            case "shield" -> "axe";

            // End Fight uses Vanilla icon
            case "endfight" -> "vanilla";

            // CPVP Late Game uses Crystal icon
            case "cpvplategame" -> "crystal";

            // SMP Early uses UHC icon
            case "smpearly" -> "uhc";

            // Spear will use its own icon later
            case "spear" -> "spear";

            default -> "crystal";
        };
    }
}
