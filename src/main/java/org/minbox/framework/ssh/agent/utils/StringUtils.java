package org.minbox.framework.ssh.agent.utils;

/**
 * The {@link String} util class
 *
 * @author 恒宇少年
 */
public class StringUtils {
    /**
     * Determine whether the given string is null
     *
     * @param value The string value
     * @return Return true for null
     */
    public static boolean isNull(String value) {
        return value == null;
    }

    /**
     * Determine whether the string is empty
     *
     * @param value The string value
     * @return Return true for empty
     */
    public static boolean isEmpty(String value) {
        return isNull(value) || value.trim().length() == 0;
    }
}
