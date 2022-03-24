package com.example.mongospringboottest.util;

import java.util.function.Function;

public enum MongoDataType {
    STRING(value -> value),
    INTEGER(ConversionUtil::convertToInteger),
    FLOAT(ConversionUtil::convertToFloat),
    DOUBLE(ConversionUtil::covertToDouble),
    BOOLEAN(ConversionUtil::convertToBoolean),
    DATE(ConversionUtil::convertToDate);

    Function<String, Object> convertFunction;

    MongoDataType(Function<String, Object> convertFunction) {
        this.convertFunction = value -> {
            try {
                return convertFunction.apply(value);
            } catch (Exception ex) {
                ConversionUtil.logger.error("Unknown error occurred while data type conversion", ex);
            }
            return value;
        };
    }
}
