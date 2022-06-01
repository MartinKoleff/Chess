package com.koleff.chess.SerializationManager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.koleff.chess.Pieces.Piece;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;

public class MapSerializationManager extends JsonSerializer<LinkedHashMap> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(LinkedHashMap value,
                          JsonGenerator gen,
                          SerializerProvider serializers)
            throws IOException, JsonProcessingException {

        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, value);
        gen.writeFieldName(writer.toString());
    }


    /**
     * Deserializes the map
     */
    private LinkedHashMap<String, Piece> loadBoard() {
        LinkedHashMap<String, Piece> loadedBoard;

        String jsonInput = "{\"key\": \"value\"}"; //To change...
        TypeReference<LinkedHashMap<String, Piece>> typeRef
                = new TypeReference<>() {
        };

        try {
            loadedBoard = mapper.readValue(jsonInput, typeRef);
            return loadedBoard;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}