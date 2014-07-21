package org.baeldung.jackson.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.baeldung.jackson.field.MyDtoAccessLevel;
import org.baeldung.jackson.field.MyDtoGetter;
import org.baeldung.jackson.field.MyDtoGetterImplicitDeserialization;
import org.baeldung.jackson.field.MyDtoSetter;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonFieldUnitTest {

    @Test
    public final void givenDifferentAccessLevels_whenPrivateOrPackage_thenNotSerializable_whenPublic_thenSerializable() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();

        final MyDtoAccessLevel dtoObject = new MyDtoAccessLevel();

        final String dtoAsString = mapper.writeValueAsString(dtoObject);
        assertThat(dtoAsString, not(containsString("stringValue")));
        assertThat(dtoAsString, not(containsString("intValue")));
        assertThat(dtoAsString, containsString("booleanValue"));
        System.out.println(dtoAsString);
    }

    @Test
    public final void givenDifferentAccessLevels_whenGetterAdded_thenSerializable() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();

        final MyDtoGetter dtoObject = new MyDtoGetter();

        final String dtoAsString = mapper.writeValueAsString(dtoObject);
        assertThat(dtoAsString, containsString("stringValue"));
        assertThat(dtoAsString, not(containsString("intValue")));
        System.out.println(dtoAsString);
    }

    @Test
    public final void givenDifferentAccessLevels_whenGetterAdded_thenDeserializable() throws JsonProcessingException, JsonMappingException, IOException {
        final String jsonAsString = "{\"stringValue\":\"dtoString\",\"booleanValue\":\"true\"}";
        final ObjectMapper mapper = new ObjectMapper();

        final MyDtoGetterImplicitDeserialization dtoObject = mapper.readValue(jsonAsString, MyDtoGetterImplicitDeserialization.class);

        assertNotNull(dtoObject);
        assertThat(dtoObject.getStringValue(), equalTo("dtoString"));
        assertThat(dtoObject.booleanValue, equalTo(true));
    }

    @Test
    public final void givenDifferentAccessLevels_whenSetterAdded_thenDeserializable() throws JsonProcessingException, JsonMappingException, IOException {
        final String jsonAsString = "{\"stringValue\":\"dtoString\",\"intValue\":1}";
        final ObjectMapper mapper = new ObjectMapper();

        final MyDtoSetter dtoObject = mapper.readValue(jsonAsString, MyDtoSetter.class);

        assertNotNull(dtoObject);
        assertThat(dtoObject.getStringValue(), equalTo("dtoString"));
        assertThat(dtoObject.anotherGetIntValue(), equalTo(1));
    }

    @Test
    public final void givenDifferentAccessLevels_whenSetterAdded_thenStillNotSerializable() throws JsonProcessingException, JsonMappingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final MyDtoSetter dtoObject = new MyDtoSetter();

        final String dtoAsString = mapper.writeValueAsString(dtoObject);
        assertThat(dtoAsString, containsString("stringValue"));
        assertThat(dtoAsString, not(containsString("intValue")));
        System.out.println(dtoAsString);
    }

    @Test
    public final void givenDifferentAccessLevels_whenSetVisibility_thenSerializable() throws JsonProcessingException, JsonMappingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        final MyDtoAccessLevel dtoObject = new MyDtoAccessLevel();

        final String dtoAsString = mapper.writeValueAsString(dtoObject);
        assertThat(dtoAsString, containsString("stringValue"));
        assertThat(dtoAsString, containsString("intValue"));
        assertThat(dtoAsString, containsString("booleanValue"));
        System.out.println(dtoAsString);
    }

}