package dev.sleep.scorelib.common.text;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

public class TextUtil {

    private static DecimalFormat decimalFormatter;

    public static String formatDecimalNumber(float number){
        if(decimalFormatter != null){
            return decimalFormatter.format(number);
        }

        decimalFormatter = new DecimalFormat("###.###");
        return decimalFormatter.format(number);
    }

    public static String formatWithCapLowAndDelim(String textToFormat) {
        return splitTextAndFormat(textToFormat);
    }

    private static String splitTextAndFormat(String textToFormat) {
        textToFormat = formatSeparator(textToFormat);
        textToFormat = formatToLower(textToFormat);

        String[] textParts = textToFormat.split("-");
        for (int i = 0; i < textParts.length; i++) {
            textParts[i] = StringUtils.capitalize(textParts[i]);
        }

        textToFormat = String.join("-", textParts);
        return textToFormat;
    }

    private static String formatSeparator(String textToFormat) {
        textToFormat = textToFormat.replace("_", "-");
        return textToFormat;
    }

    private static String formatToLower(String textToFormat) {
        textToFormat = textToFormat.toLowerCase();
        return textToFormat;
    }

}
