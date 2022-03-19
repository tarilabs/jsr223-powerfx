package com.matteomortari.jsr223powerfx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class PowerFxScriptEngineFactory implements ScriptEngineFactory {

    @Override
    public String getEngineName() {
        return "jsr223-powerfx";
    }

    @Override
    public String getEngineVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public List<String> getExtensions() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getMimeTypes() {
        return Arrays.asList("text/x.powerfx");
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList(getLanguageName(), "power fx", "powerfx");
    }

    @Override
    public String getLanguageName() {
        return "Power Fx";
    }

    @Override
    public String getLanguageVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public Object getParameter(String key) {
        if (key.equals(ScriptEngine.NAME)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.ENGINE)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
            return getEngineVersion();
        } else if (key.equals(ScriptEngine.LANGUAGE)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
            return getLanguageVersion();
        } else {
            return null;
        }
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return null;
    }

    @Override
    public String getProgram(String... statements) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new PowerFxScriptEngine(this);
    }
    
}
