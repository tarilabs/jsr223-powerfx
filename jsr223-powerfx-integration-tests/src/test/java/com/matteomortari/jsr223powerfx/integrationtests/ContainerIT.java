package com.matteomortari.jsr223powerfx.integrationtests;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.matteomortari.jsr223powerfx.PowerFxScriptEngine;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class ContainerIT {
    @Container
    public GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("jsr223-powerfx-server:latest"))
            .withExposedPorts(80);

    @Test
    public void test() throws ScriptException {
        String address = redis.getHost();
        Integer port = redis.getFirstMappedPort();
        System.setProperty(PowerFxScriptEngine.JSR223_URL, "http://" + address + ":" + port + "/eval");
        System.out.println(System.getProperty(PowerFxScriptEngine.JSR223_URL));
        ScriptEngineManager MANAGER = new ScriptEngineManager();
        ScriptEngine engine = MANAGER.getEngineByName("powerfx");
        Bindings bindings = engine.createBindings();
        bindings.put("a", 1);
        bindings.put("b", 2);
        engine.eval("a+b", bindings);
        Object result = engine.eval("a+b", bindings);
        Assertions.assertThat(result).isEqualTo(3);
    }
}
