package com.webpoker.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Created by matt on 04/06/2014.
 */
class PokerDateSerializer extends JsonSerializer<DateTime> {

    static DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        gen.writeString(formatter.print(value));
    }
}