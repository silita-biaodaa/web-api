package com.silita.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类 - 公用
 */

public class CommonUtil {

    public static final String WEB_APP_ROOT_KEY = "anyvape.webAppRoot";// WebRoot路径KEY
    public static final String PATH_PREPARED_STATEMENT_UUID = "\\{uuid\\}";// UUID路径占位符
    public static final String PATH_PREPARED_STATEMENT_DATE = "\\{date(\\(\\w+\\))?\\}";// 日期路径占位符


    /**
     * 把中文字符进行分割
     *
     * @param s
     * @return
     */
    public static List<Character> splitChinese(String s) {
        List<Character> list = new ArrayList<Character>();
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = p.matcher(s);
        while (m.find()) {
            String subStr = m.group();
            for (int i = 0; i < subStr.length(); i++) {
                char temp = subStr.charAt(i);
                list.add(temp);
//				System.out.println(temp);
            }
        }
        return list;
    }

    /**
     * 获取WebRoot路径
     *
     * @return WebRoot路径
     */
    public static String getWebRootPath() {
        return System.getProperty(WEB_APP_ROOT_KEY);
    }

    /**
     * 随机获取UUID字符串(无中划线)
     *
     * @return UUID字符串
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }

    /**
     * 获取实际路径
     *
     * @param path 路径
     */
    public static String getPreparedStatementPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        StringBuffer uuidStringBuffer = new StringBuffer();
        Matcher uuidMatcher = Pattern.compile(PATH_PREPARED_STATEMENT_UUID).matcher(path);
        while (uuidMatcher.find()) {
            uuidMatcher.appendReplacement(uuidStringBuffer, CommonUtil.getUUID());
        }
        uuidMatcher.appendTail(uuidStringBuffer);

        StringBuffer dateStringBuffer = new StringBuffer();
        Matcher dateMatcher = Pattern.compile(PATH_PREPARED_STATEMENT_DATE).matcher(uuidStringBuffer.toString());
        while (dateMatcher.find()) {
            String dateFormate = "yyyyMM";
            Matcher dateFormatMatcher = Pattern.compile("\\(\\w+\\)").matcher(dateMatcher.group());
            if (dateFormatMatcher.find()) {
                String dateFormatMatcherGroup = dateFormatMatcher.group();
                dateFormate = dateFormatMatcherGroup.substring(1, dateFormatMatcherGroup.length() - 1);
            }
            dateMatcher.appendReplacement(dateStringBuffer, new SimpleDateFormat(dateFormate).format(new Date()));
        }
        dateMatcher.appendTail(dateStringBuffer);

        return dateStringBuffer.toString();
    }

    /**
     * 判断是否是正整数，并且不包括零
     *
     * @param str
     * @return
     */
    public static boolean isPositiveInteger(String str) {
        return Pattern.compile("^[1-9]+[0-9]*$").matcher(str).find();
    }

    /**
     * 判断是否是正整数，并且不包括零
     *
     * @param str
     * @return
     */
    public static boolean isPositiveInteger(int str) {
        return Pattern.compile("^[1-9]+[0-9]*$").matcher(String.valueOf(str)).find();
    }

    /**
     * 判断是否是正整数，并且不包括零
     *
     * @param str
     * @return
     */
    public static boolean isPositiveInteger(long str) {
        return Pattern.compile("^[1-9]+[0-9]*$").matcher(String.valueOf(str)).find();
    }

    private abstract static class WordTokenizer {
        protected static final char UNDERSCORE = '_';

        /**
         * Parse sentence。
         */
        public String parse(String str) {
            if (StringUtils.isEmpty(str)) {
                return str;
            }

            int length = str.length();
            StringBuffer buffer = new StringBuffer(length);

            for (int index = 0; index < length; index++) {
                char ch = str.charAt(index);

                // 忽略空白。
                if (Character.isWhitespace(ch)) {
                    continue;
                }

                // 大写字母开始：UpperCaseWord或是TitleCaseWord。
                if (Character.isUpperCase(ch)) {
                    int wordIndex = index + 1;

                    while (wordIndex < length) {
                        char wordChar = str.charAt(wordIndex);

                        if (Character.isUpperCase(wordChar)) {
                            wordIndex++;
                        } else if (Character.isLowerCase(wordChar)) {
                            wordIndex--;
                            break;
                        } else {
                            break;
                        }
                    }

                    // 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
                    // 2. wordIndex == index，说明index处为一个titleCaseWord。
                    // 3. wordIndex > index，说明index到wordIndex -
                    // 1处全部是大写，以upperCaseWord处理。
                    if ((wordIndex == length) || (wordIndex > index)) {
                        index = parseUpperCaseWord(buffer, str, index,
                                wordIndex);
                    } else {
                        index = parseTitleCaseWord(buffer, str, index);
                    }

                    continue;
                }

                // 小写字母开始：LowerCaseWord。
                if (Character.isLowerCase(ch)) {
                    index = parseLowerCaseWord(buffer, str, index);
                    continue;
                }

                // 数字开始：DigitWord。
                if (Character.isDigit(ch)) {
                    index = parseDigitWord(buffer, str, index);
                    continue;
                }

                // 非字母数字开始：Delimiter。
                inDelimiter(buffer, ch);
            }

            return buffer.toString();
        }

        private int parseUpperCaseWord(StringBuffer buffer, String str,
                                       int index, int length) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            for (; index < length; index++) {
                ch = str.charAt(index);
                inWord(buffer, ch);
            }

            return index - 1;
        }

        private int parseLowerCaseWord(StringBuffer buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为小写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseTitleCaseWord(StringBuffer buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseDigitWord(StringBuffer buffer, String str, int index) {
            char ch = str.charAt(index++);

            // 首字符，必然存在且为数字。
            if (buffer.length() == 0) {
                startDigitSentence(buffer, ch);
            } else {
                startDigitWord(buffer, ch);
            }

            // 后续字符，必为数字。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isDigit(ch)) {
                    inDigitWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        protected boolean isDelimiter(char ch) {
            return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
                    && !Character.isDigit(ch);
        }

        protected abstract void startSentence(StringBuffer buffer, char ch);

        protected abstract void startWord(StringBuffer buffer, char ch);

        protected abstract void inWord(StringBuffer buffer, char ch);

        protected abstract void startDigitSentence(StringBuffer buffer, char ch);

        protected abstract void startDigitWord(StringBuffer buffer, char ch);

        protected abstract void inDigitWord(StringBuffer buffer, char ch);

        protected abstract void inDelimiter(StringBuffer buffer, char ch);
    }

    private static final WordTokenizer CAMEL_CASE_TOKENIZER = new WordTokenizer() {

        @Override
        protected void startSentence(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        @Override
        protected void startWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(Character.toUpperCase(ch));
            } else {
                buffer.append(Character.toLowerCase(ch));
            }
        }

        @Override
        protected void inWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        @Override
        protected void startDigitSentence(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        @Override
        protected void startDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        @Override
        protected void inDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        @Override
        protected void inDelimiter(StringBuffer buffer, char ch) {
            if (ch != UNDERSCORE) {
                buffer.append(ch);
            }
        }
    };

    private static final WordTokenizer UPPER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {

        @Override
        protected void startSentence(StringBuffer buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        @Override
        protected void startWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(Character.toUpperCase(ch));
        }

        @Override
        protected void inWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        @Override
        protected void startDigitSentence(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        @Override
        protected void startDigitWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(ch);
        }

        @Override
        protected void inDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        @Override
        protected void inDelimiter(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }
    };

    public static String toCamelCase(String str) {
        if (str == null)
            return null;
        return CAMEL_CASE_TOKENIZER.parse(str);
    }

    public static String toUpperCaseWithUnderscores(String str) {
        if (str == null)
            return null;
        return UPPER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
    }

    public static String toLowerCaseWithUnderscores(String str) {
        if (str == null)
            return null;
        return toUpperCaseWithUnderscores(str).toLowerCase();
    }

    public static boolean matchesWildcard(String text, String pattern) {
        text += '\0';
        pattern += '\0';

        int N = pattern.length();

        boolean[] states = new boolean[N + 1];
        boolean[] old = new boolean[N + 1];
        old[0] = true;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            states = new boolean[N + 1];
            for (int j = 0; j < N; j++) {
                char p = pattern.charAt(j);

                if (old[j] && (p == '*'))
                    old[j + 1] = true;

                if (old[j] && (p == c))
                    states[j + 1] = true;
                if (old[j] && (p == '?'))
                    states[j + 1] = true;
                if (old[j] && (p == '*'))
                    states[j] = true;
                if (old[j] && (p == '*'))
                    states[j + 1] = true;
            }
            old = states;
        }
        return states[N];
    }

    public static String trimTail(String input, String tail) {
        if (input == null || tail == null || !input.endsWith(tail))
            return input;
        return input.substring(0, input.length() - tail.length());
    }

    public static String trimTailSlash(String input) {
        return trimTail(input, "/");
    }

    public static String compressRepeat(String input, String repeat) {
        if (input == null || repeat == null)
            return input;
        String s = repeat + repeat;
        while (input.contains(s))
            input = input.replace(s, repeat);
        return input;
    }

    public static String compressRepeatSlash(String input) {
        return compressRepeat(input, "/");
    }

    public static boolean isNumericOnly(String cs) {
        if (cs == null || cs.trim().length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String decodeUrl(String url) {
        try {
            if (isUtf8Url(url)) {
                return URLDecoder.decode(url, "UTF-8");
            } else {
                return URLDecoder.decode(url, "GBK");
            }
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    private static boolean utf8codeCheck(String urlCode) {
        StringBuilder sign = new StringBuilder();
        if (urlCode.startsWith("%e"))
            for (int p = 0; p != -1; ) {
                p = urlCode.indexOf("%", p);
                if (p != -1)
                    p++;
                sign.append(p);
            }
        return sign.toString().equals("147-1");
    }

    private static boolean isUtf8Url(String urlCode) {
        urlCode = urlCode.toLowerCase();
        int p = urlCode.indexOf("%");
        if (p != -1 && urlCode.length() - p > 9) {
            urlCode = urlCode.substring(p, p + 9);
        }
        return utf8codeCheck(urlCode);
    }

    public static String htmlspecialchars(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "@";
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public static int getPages(int total, int pageSize) {
        if (total % pageSize == 0) {
            return total / pageSize;
        } else {
            return (total / pageSize) + 1;
        }
    }
}