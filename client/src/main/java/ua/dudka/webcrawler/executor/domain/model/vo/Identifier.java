package ua.dudka.webcrawler.executor.domain.model.vo;

import lombok.Value;

import java.util.UUID;

@Value
public class Identifier {
    private String value;

    private Identifier(String value) {
        this.value = value;
    }

    public static Identifier randomUUID() {
        return new Identifier(UUID.randomUUID().toString());
    }
}