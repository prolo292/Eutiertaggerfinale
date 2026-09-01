package com.xss1lent.universaltiertagger.data;

public enum GameMode {

    SWORD("Sword", "sword"),
    AXE("Axe", "axe"),
    POT("Pot", "pot"),
    VANILLA("Vanilla", "vanilla"),
    SMP("SMP", "smp"),
    NETHOP("NethOP", "nethop"),
    UHC("UHC", "uhc"),
    MACE("Mace", "mace"),
    CRYSTAL("Crystal", "crystal");

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
        if (value == null) {
            return CRYSTAL;
        }

        try {
            return GameMode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return CRYSTAL;
        }
    }
}
