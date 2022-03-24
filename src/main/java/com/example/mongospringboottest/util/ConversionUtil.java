package com.example.mongospringboottest.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConversionUtil {
    static final Logger logger = LoggerFactory.getLogger(ConversionUtil.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[[ ]['T']HH:mm:ss]");

    public static Function<String, Object> getConversionFunction(String dataType) {
        try {
            MongoDataType mongodataType = MongoDataType.valueOf(dataType.toUpperCase());
            return mongodataType.convertFunction;
        } catch(IllegalArgumentException ex) {
            logger.info("Mapping does not exist for dataType: " + dataType, ex);
        }
        return value -> value;
    }

    public static Boolean convertToBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    public static Integer convertToInteger(String value) {
        return Integer.parseInt(value);
    }

    public static Long convertToLong(String value) {
        return Long.parseLong(value);
    }

    public static Float convertToFloat(String value) {
        return Float.parseFloat(value);
    }

    public static Double covertToDouble(String value) {
        return Double.parseDouble(value);
    }
    
    public static Date convertToDate(String value) {     
        return Date.from(
            Instant.from(
                DATE_TIME_FORMATTER.parse(value)
            )
        );
    }

}
