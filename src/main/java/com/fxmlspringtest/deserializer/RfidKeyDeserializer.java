package com.fxmlspringtest.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fxmlspringtest.model.RfidKey;

import java.io.IOException;

/**
 * Created by Anton Hellbe on 2016-05-04.
 */
public class RfidKeyDeserializer extends JsonDeserializer<RfidKey> {


    /**
     * When recieving an account object an RFID-key object attached to the user will not be recognized, this RFID-key deseralizer
     * tells how an RFID-key object should be parsed.
     *
     * @param jsonParser             - parsing retrived data
     * @param deserializationContext - Not used
     * @return - New RFID key object
     * @throws IOException             - Throws exception if needed
     * @throws JsonProcessingException - Throws exception if needed
     */
    @Override
    public RfidKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        RfidKey key = new RfidKey();
        key.setId(node.get("id").asText());
        key.setEnabled(node.get("enabled").asBoolean());
        return key;
    }
}
