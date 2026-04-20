package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.product.StepProduct;
import com.minicad.step.model.product.StepProductDefinition;
import com.minicad.step.model.product.StepProductDefinitionShape;
import com.minicad.step.model.product.StepProductDefinitionFormation;
import com.minicad.step.model.product.StepProductRelationship;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepHeaderEntry;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Extracts product metadata from STEP header and resolved entities.
 */
final class ProductMetadataExtractor {

    private ProductMetadataExtractor() {
    }

    record ProductMetadata(
            String fileName,
            String fileDescription,
            String productName,
            String productDescription,
            String productIdentifier,
            List<String> schemaNames,
            List<ComponentInfo> components
    ) {
        boolean isEmpty() {
            return fileName == null && fileDescription == null && productName == null
                    && productDescription == null && productIdentifier == null
                    && schemaNames.isEmpty() && components.isEmpty();
        }

        record ComponentInfo(String name, String identifier, String description) {}
    }

    static ProductMetadata extract(StepFile stepFile, Map<Integer, StepEntity> resolved) {
        String fileName = null;
        String fileDescription = null;
        List<String> schemaNames = new ArrayList<>();

        for (StepHeaderEntry entry : stepFile.headerEntries()) {
            switch (entry.name()) {
                case "FILE_NAME" -> {
                    if (!entry.parameters().isEmpty()) {
                        fileName = stringValue(entry.parameters().get(0));
                    }
                }
                case "FILE_DESCRIPTION" -> {
                    StepValue desc = entry.parameters().isEmpty() ? null : entry.parameters().get(0);
                    if (desc instanceof StepValue.ListValue list && !list.elements().isEmpty()) {
                        fileDescription = stringValue(list.elements().get(0));
                    } else if (desc != null) {
                        fileDescription = stringValue(desc);
                    }
                }
                case "FILE_SCHEMA" -> {
                    StepValue schema = entry.parameters().isEmpty() ? null : entry.parameters().get(0);
                    if (schema instanceof StepValue.ListValue list) {
                        for (StepValue v : list.elements()) {
                            String s = stringValue(v);
                            if (s != null) schemaNames.add(s);
                        }
                    }
                }
            }
        }

        String productName = null;
        String productDescription = null;
        String productIdentifier = null;
        List<ProductMetadata.ComponentInfo> components = new ArrayList<>();

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepProduct product) {
                if (productName == null) {
                    productName = product.name();
                    productDescription = product.description();
                    productIdentifier = product.identifier();
                } else {
                    components.add(new ProductMetadata.ComponentInfo(product.name(), product.identifier(), product.description()));
                }
            } else if (entity instanceof StepProductDefinitionShape shape) {
                if (productName == null) {
                    productName = shape.name();
                    productDescription = shape.description();
                }
            } else if (entity instanceof StepProductDefinition pd) {
                if (productName == null && pd.description() != null && !pd.description().isEmpty()) {
                    productDescription = pd.description();
                }
                if (pd.formation() instanceof StepProductDefinitionFormation formation) {
                    if (productName == null && formation.name() != null) {
                        productName = formation.name();
                    }
                }
            } else if (entity instanceof StepProductRelationship rel) {
                if (rel.name() != null && productName == null) {
                    productName = rel.name();
                }
            }
        }

        return new ProductMetadata(
                fileName, fileDescription, productName, productDescription, productIdentifier,
                List.copyOf(schemaNames), List.copyOf(components)
        );
    }

    private static String stringValue(StepValue v) {
        if (v instanceof StepValue.StringValue str) return str.value();
        if (v instanceof StepValue.ListValue list && !list.elements().isEmpty()) return stringValue(list.elements().get(0));
        return null;
    }
}
