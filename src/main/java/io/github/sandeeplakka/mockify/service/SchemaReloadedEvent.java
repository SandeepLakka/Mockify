package io.github.sandeeplakka.mockify.service;

import io.github.sandeeplakka.mockify.schema.ModelCfg;

import java.util.Map;

/**
 * Application event indicating that schema configuration has been reloaded.
 * Contains the latest set of model configurations.
 */
public record SchemaReloadedEvent(Map<String, ModelCfg> configs) {
}
