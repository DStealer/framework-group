package cn.com.coderd.framework.common.util;

import cn.com.coderd.framework.common.support.EnumVO;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class ComponentUtils {
    private static final Map<Object, Object> ENUM_HASHMAP = new ConcurrentHashMap<>();

    /**
     * 分解从0到max的前后区间
     *
     * @param max  最大值,不包括
     * @param step 步长
     * @return
     */
    public static int[][] range(int max, int step) {
        int size = max / step + (max % step == 0 ? 0 : 1);
        if (size < 1) {
            return new int[0][];
        } else {
            int[][] result = new int[size][2];
            for (int i = 0; i < result.length; i++) {
                int[] tmp = new int[2];
                tmp[0] = i * step;
                tmp[1] = (i + 1) * step;
                result[i] = tmp;
            }
            if (result[result.length - 1][1] > max) {
                result[result.length - 1][1] = max;
            }
            return result;
        }
    }

    /**
     * 折叠文本
     *
     * @param text
     * @param len
     * @return
     */
    public static String wrap(String text, int len) {
        if (text == null || text.length() <= len) {
            return text;
        }
        int startIndex = 0, endIndex = text.length() - 1;
        StringBuilder builder = new StringBuilder();
        while (startIndex < endIndex) {
            int indexEndAt = startIndex + len;
            if (indexEndAt > endIndex) {
                builder.append(text, startIndex, endIndex + 1);
                break;
            }
            boolean lineSeparatorFound = false;
            while (indexEndAt >= startIndex) {
                if ('\n' == text.charAt(indexEndAt)) {
                    lineSeparatorFound = true;
                    break;
                }
                if (!Character.isUnicodeIdentifierPart(text.charAt(indexEndAt))) {
                    break;
                }
                indexEndAt--;
            }
            if (indexEndAt <= startIndex) {
                builder.append(text, startIndex, Math.min(startIndex + len, endIndex + 1));
                if (!lineSeparatorFound) {
                    builder.append(System.lineSeparator());
                }
                startIndex = Math.min(startIndex + len, endIndex + 1);
            } else {
                builder.append(text, startIndex, indexEndAt);
                if (!lineSeparatorFound) {
                    builder.append(System.lineSeparator());
                }
                startIndex = indexEndAt;
            }
        }
        return builder.toString();
    }

    /**
     * 移除换行
     *
     * @param text
     * @return
     */
    public static String unwrap(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(text.length());
        int fromIndex = 0;
        while (fromIndex < text.length()) {
            int indexOf = text.indexOf(System.lineSeparator(), fromIndex);
            if (indexOf == -1) {
                builder.append(text, fromIndex, text.length());
                break;
            }
            builder.append(text, fromIndex, indexOf - System.lineSeparator().length() + 1);
            fromIndex = indexOf + System.lineSeparator().length();
        }

        return builder.toString();
    }

    /**
     * 判断是否在指定的元素内
     *
     * @param obj
     * @param rangeObjs
     * @return
     */
    public static boolean in(Object obj, Object... rangeObjs) {
        for (Object o : rangeObjs) {
            if (o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 截断并移除空格
     *
     * @param str
     * @param maxLen
     * @return
     */
    public static String trimTrunc(String str, int maxLen) {
        return str == null ? null : str.length() <= maxLen
                ? str.trim() : str.substring(0, maxLen).trim() + "...";
    }

    /**
     * 获取枚举类型所有实例
     *
     * @param tClass
     * @param <T>
     * @param <K>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>, K> List<T> values(Class<T> tClass) {
        return (List<T>) ENUM_HASHMAP.computeIfAbsent(tClass, k -> {
            try {
                Field valuesField = tClass.getDeclaredField("$VALUES");
                valuesField.setAccessible(true);
                T[] values = (T[]) valuesField.get(tClass);
                return Arrays.stream(values).collect(Collectors.toList());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 转化枚举类型
     *
     * @param tClass
     * @param function
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T extends Enum<T>, K> List<EnumVO> valueToEnumVOs(Class<T> tClass, Function<T, EnumVO> function) {
        return values(tClass).stream().map(function).collect(Collectors.toList());
    }
}
