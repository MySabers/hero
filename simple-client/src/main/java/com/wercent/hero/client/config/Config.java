package com.wercent.hero.client.config;


import com.wercent.hero.client.protocol.Serializer;

public abstract class Config {

    public static Serializer.Algorithm getSerializerAlgorithm() {
        return getSerializerAlgorithm("Json");
    }
    public static Serializer.Algorithm getSerializerAlgorithm(String algorithm) {
        return Serializer.Algorithm.Json;
    }
}