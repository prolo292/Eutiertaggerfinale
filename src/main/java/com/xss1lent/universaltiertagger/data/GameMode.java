package com.xss1lent.universaltiertagger.data;

public enum GameMode {

    // Universal / European
    SWORD("Sword", "sword"),
    AXE("Axe", "axe"),
    POT("Pot", "pot"),
    VANILLA("Vanilla", "vanilla"),
    SMP("SMP", "smp"),
    NETHOP("NethOP", "nethop"),
    UHC("UHC", "uhc"),
    MACE("Mace", "mace"),
    CRYSTAL("Crystal", "crystal"),

    // MCPVP Beta
    SHIELD("Shield", "shield"),
    EARLY_GAME("Early Game", "early_game"),
    END_GAME("End Game", "end_game"),
    LATE_GAME("Late Game", "late_game"),
    SPEAR("Spear", "spear"),

    // Additional common modes
    DIAMOND_POT("Diamond Pot", "diamond_pot"),
    CART("Cart", "cart"),
    BED("Bed", "bed");

    private final String displayName;
    private final String iconName;

    GameMode(String displayName, String iconName) {
        this.displayName = displayName;
        this.iconName = iconName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconName() {
        return iconName;
    }

    public static GameMode fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value
                .trim()
                .toUpperCase()
                .replace("-", "_")
                .replace(" ", "_");

        // Aliases used by different tierlists
        switch (normalized) {
            case "NETHERPOT":
            case "NETHPOT":
            case "NETHER_POT":
                normalized = "NETHOP";
                break;

            case "CRYSTALPVP":
            case "CRYSTAL_PVP":
                normalized = "CRYSTAL";
                break;

            case "POTION":
                normalized = "POT";
                break;
        }

        try {
            return GameMode.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
