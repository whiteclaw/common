package com.whiteclaw.common;


import com.fasterxml.jackson.core.type.TypeReference;
import com.whiteclaw.common.exampledomain.Sample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whiteclaw
 * created on 2018-12-07
 */
class JacksonUtilTest {

    private Sample sample = new Sample(1, "jackson");
    private String json = "{\"id\":1,\"name\":\"jackson\"}";

    @Test
    void obj2Json() {
        assertEquals(json, JacksonUtil.obj2Json(sample));
    }

    @Test
    void json2ObjUseClass() {
        assertEquals(sample, JacksonUtil.json2Obj(json, Sample.class));
    }

    @Test
    void json2ObjUseTypeReference() {
        assertEquals(sample, JacksonUtil.json2Obj(json, new TypeReference<Sample>() {}));
    }

}