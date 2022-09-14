package com.wercent.hero.server.config;


import com.wercent.hero.common.protocol.Serializer;

public abstract class Config {

    public static Serializer.Algorithm getSerializerAlgorithm() {
        return getSerializerAlgorithm("Json");
    }
    public static Serializer.Algorithm getSerializerAlgorithm(String algorithm) {
        return Serializer.Algorithm.Json;
    }
}