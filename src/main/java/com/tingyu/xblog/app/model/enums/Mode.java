package com.tingyu.xblog.app.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;

/**
 * Halo runtime mode.
 *
 * @author johnniang
 * @date 19-6-10
 */
public enum Mode {
    PRODUCTION,
    DEVELOPMENT,
    DEMO,
    TEST;

    /**
     * Get mode from value.
     *
     * @param value mode value
     * @return runtime mode
     */
    @JsonCreator
    public static Mode valueFrom(@Nullable String value) {
        Mode modeResult = null;
        for (Mode mode : values()) {
            if (mode.name().equalsIgnoreCase(value)) {
                modeResult = mode;
                break;
            }
        }
        if (modeResult == null) {
            modeResult = PRODUCTION;
        }
        return modeResult;
    }

    @JsonValue
    String getValue() {
        return this.name().toLowerCase();
    }
}
