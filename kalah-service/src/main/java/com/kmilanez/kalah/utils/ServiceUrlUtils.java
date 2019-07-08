package com.kmilanez.kalah.utils;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public final class ServiceUrlUtils {

    public static String createUrl(Class<?> controller) {
        return WebMvcLinkBuilder
                .linkTo(controller)
                .toUriComponentsBuilder()
                .toUriString();
    }

}
