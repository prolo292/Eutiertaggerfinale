package com.xss1lent.universaltiertagger.data;

public enum TierlistType {

    EUROPEAN("European Tierlist"),
    MCTIERS("MCTiers"),
    PVPTIERS("PvPTiers"),
    MCPVP("MCPvP");

    private final String displayName;

    TierlistType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TierlistType fromString(String value) {
        if (value == null) {
            return EUROPEAN;
        }

        try {
            return TierlistType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return EUROPEAN;
        }
    }
}
