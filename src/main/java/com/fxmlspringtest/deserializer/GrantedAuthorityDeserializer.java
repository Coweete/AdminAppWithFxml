package com.fxmlspringtest.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sebastian on 2016-05-04.
 */
public class GrantedAuthorityDeserializer extends JsonDeserializer<List<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(GrantedAuthorityDeserializer.class);

    @Override
    public List<GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<GrantedAuthority> auths = new ArrayList<>();

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        node.forEach(jsonNode -> {
            SimpleGrantedAuthority tempAuth = (new SimpleGrantedAuthority(jsonNode.get("authority").asText()));
            auths.add(tempAuth);
        });


        return auths;
    }
}
