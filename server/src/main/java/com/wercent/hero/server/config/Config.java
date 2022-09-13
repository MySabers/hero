package com.wercent.hero.server.config;


import com.wercent.hero.server.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Config {

    public static Serializer.Algorithm getSerializerAlgorithm() {
        return getSerializerAlgorithm("Json");
    }
    public static Serializer.Algorithm getSerializerAlgorithm(String algorithm) {
        return Serializer.Algorithm.Json;
    }
}