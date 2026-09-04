package com.xss1lent.universaltiertagger.render;

import com.xss1lent.universaltiertagger.data.GameMode;

public class ModeIcon {

    private ModeIcon() {
    }

    /**
     * Returns the custom font character for each game mode.
     */
    public static String getIcon(GameMode mode) {

        if (mode == null) {
            return "\uE009";
        }

        return switch (mode) {

            case SWORD -> "\uE001";
            case AXE -> "\uE002";
            case POT -> "\uE003";
            case VANILLA -> "\uE004";
            case SMP -> "\uE005";
            case NETHOP -> "\uE006";
            case UHC -> "\uE007";
            case MACE -> "\uE008";
            case CRYSTAL -> "\uE009";

            default -> "\uE009";
        };
    }

    /**
     * Returns an icon for MCPVP-specific mode names.
     */
    public static String getMCPVPIcon(String modeName) {

        if (modeName == null) {
            return "\uE009";
        }

        String mode = modeName.toLowerCase()
                .replace(" ", "")
                .replace("-", "");

        return switch (mode) {

            case "shield" -> "\uE002";

            case "endfight" -> "\uE004";

            case "cpvplategame" -> "\uE009";

            case "smpearly" -> "\uE007";

            case "spear" -> "\uE010";

            default -> "\uE009";
        };
    }
}
