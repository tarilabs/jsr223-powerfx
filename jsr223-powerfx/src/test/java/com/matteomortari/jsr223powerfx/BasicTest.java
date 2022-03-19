package com.matteomortari.jsr223powerfx;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WireMockTest()
public class BasicTest {

    private static final Logger LOG = LoggerFactory.getLogger(BasicTest.class);

    @Test
    public void test(WireMockRuntimeInfo wmRuntimeInfo) throws ScriptException {
        String mockServerlURL = wmRuntimeInfo.getHttpBaseUrl() + "/eval";
        System.setProperty(PowerFxScriptEngine.JSR223_URL, mockServerlURL);
        stubFor(post("/eval")
                .willReturn(ok()
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"result\": 3}")));
        ScriptEngineManager MANAGER = new ScriptEngineManager();
        for (ScriptEngineFactory factory : MANAGER.getEngineFactories()) {
            printScriptEngineFactoryInfo(factory);
        }
        ScriptEngine engine = MANAGER.getEngineByName("powerfx");
        Bindings bindings = engine.createBindings();
        bindings.put("a", 1);
        bindings.put("b", 2);
        engine.eval("a+b", bindings);
        Object result = engine.eval("a+b", bindings);
        Assertions.assertThat(result).isEqualTo(3);
    }

    private void printScriptEngineFactoryInfo(ScriptEngineFactory factory) {
        String engName = factory.getEngineName();
        String engVersion = factory.getEngineVersion();
        String langName = factory.getLanguageName();
        String langVersion = factory.getLanguageVersion();
        LOG.debug("Script Engine: {} {} {} {}", engName, engVersion, langName, langVersion);
        List<String> engNames = factory.getNames();
        for (String name : engNames) {
            LOG.debug("\tEngine Alias: {}", name);
        }
    }
}
