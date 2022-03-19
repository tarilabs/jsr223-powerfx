package com.matteomortari.jsr223powerfx;

import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matteomortari.jsr223powerfx.model.EvalRequest;
import com.matteomortari.jsr223powerfx.model.EvalResponse;

import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerFxScriptEngine extends AbstractScriptEngine {

    private static final Logger LOG = LoggerFactory.getLogger(PowerFxScriptEngine.class);

    public static final String JSR223_URL = "JSR223_URL";
    public static final String JSR223_URL_DEFAULT_VALUE = "http://localhost:5000/eval";

    private final static ObjectMapper MAPPER = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .build()
        .configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true)
        ;

    private final PowerFxScriptEngineFactory factory;
    private final String serverUrl;

    PowerFxScriptEngine(PowerFxScriptEngineFactory factory) {
        this.factory = factory;
        this.serverUrl = System.getProperty(JSR223_URL, JSR223_URL_DEFAULT_VALUE);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        try {
            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
            String contextJSON = MAPPER.writeValueAsString(bindings);
            LOG.debug(contextJSON);
            EvalRequest payload = new EvalRequest();
            payload.setContext(contextJSON);
            payload.setExpression(script);
            String payloadJSON = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            Content responseContent = Request.post(this.serverUrl)
                .bodyString(payloadJSON, ContentType.APPLICATION_JSON)
                .execute().returnContent();
            String responseJSON = responseContent.asString();
            EvalResponse responseValue = MAPPER.readValue(responseJSON, EvalResponse.class);
            Object evaluation = MAPPER.readValue(responseValue.getResult(), Object.class);
            LOG.debug("{}", evaluation);
            return evaluation;
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        try (Scanner scanner = new Scanner(reader)) {
            String script = scanner.useDelimiter("\\Z").next();
            return eval(script, context);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }
}
