package com.minicad.helper.metadata;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.product.StepProduct;
import com.minicad.step.model.product.StepProductDefinition;
import com.minicad.step.model.product.StepProductDefinitionShape;
import com.minicad.step.model.product.StepProductDefinitionFormation;
import com.minicad.step.model.product.StepProductRelationship;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepFileName;
import com.minicad.step.syntax.StepHeaderEntry;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Extracts product metadata from STEP header and resolved entities.
 */
final class ProductMetadataExtractor {

    private ProductMetadataExtractor() {
    }

    static final class ProductMetadata {
        private final String fileName;
        private final String fileDescription;
        private final String productName;
        private final String productDescription;
        private final String productIdentifier;
        private final List<String> schemaNames;
        private final List<ComponentInfo> components;

        ProductMetadata(
                String fileName,
                String fileDescription,
                String productName,
                String productDescription,
                String productIdentifier,
                List<String> schemaNames,
                List<ComponentInfo> components
        ) {
            this.fileName = fileName;
            this.fileDescription = fileDescription;
            this.productName = productName;
            this.productDescription = productDescription;
            this.productIdentifier = productIdentifier;
            this.schemaNames = schemaNames;
            this.components = components;
        }

        String fileName() { return fileName; }
        String fileDescription() { return fileDescription; }
        String productName() { return productName; }
        String productDescription() { return productDescription; }
        String productIdentifier() { return productIdentifier; }
        List<String> schemaNames() { return schemaNames; }
        List<ComponentInfo> components() { return components; }

        boolean isEmpty() {
            return fileName == null && fileDescription == null && productName == null
                    && productDescription == null && productIdentifier == null
                    && schemaNames.isEmpty() && components.isEmpty();
        }

        static final class ComponentInfo {
            private final String name;
            private final String identifier;
            private final String description;

            ComponentInfo(String name, String identifier, String description) {
                this.name = name;
                this.identifier = identifier;
                this.description = description;
            }

            String name() { return name; }
            String identifier() { return identifier; }
            String description() { return description; }
        }
    }

    static ProductMetadata extract(StepFile stepFile, Map<Integer, StepEntity> resolved) {
        String fileName = null;
        String fileDescription = null;
        List<String> schemaNames = stepFile.schemaNames();

        StepFileName headerFileName = stepFile.fileName().orElse(null);
        if (headerFileName != null) {
            fileName = headerFileName.name();
        }

        for (StepHeaderEntry entry : stepFile.headerEntries()) {
            if ("FILE_DESCRIPTION".equals(entry.name())) {
                StepValue desc = entry.parameters().isEmpty() ? null : entry.parameters().get(0);
                if (desc instanceof StepValue.ListValue) {
                    StepValue.ListValue list = (StepValue.ListValue) desc;
                    if (!list.elements().isEmpty()) {
                        fileDescription = stringValue(list.elements().get(0));
                    }
                } else if (desc != null) {
                    fileDescription = stringValue(desc);
                }
            }
        }

        String productName = null;
        String productDescription = null;
        String productIdentifier = null;
        List<ProductMetadata.ComponentInfo> components = new ArrayList<>();

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepProduct) {
                StepProduct product = (StepProduct) entity;
                if (productName == null) {
                    productName = product.name();
                    productDescription = product.description();
                    productIdentifier = product.identifier();
                } else {
                    components.add(new ProductMetadata.ComponentInfo(product.name(), product.identifier(), product.description()));
                }
            } else if (entity instanceof StepProductDefinitionShape) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) entity;
                if (productName == null) {
                    productName = shape.name();
                    productDescription = shape.description();
                }
            } else if (entity instanceof StepProductDefinition) {
                StepProductDefinition pd = (StepProductDefinition) entity;
                if (productName == null && pd.description() != null && !pd.description().isEmpty()) {
                    productDescription = pd.description();
                }
                if (pd.formation() instanceof StepProductDefinitionFormation) {
                    StepProductDefinitionFormation formation = (StepProductDefinitionFormation) pd.formation();
                    if (productName == null && formation.name() != null) {
                        productName = formation.name();
                    }
                }
            } else if (entity instanceof StepProductRelationship) {
                StepProductRelationship rel = (StepProductRelationship) entity;
                if (rel.name() != null && productName == null) {
                    productName = rel.name();
                }
            }
        }

        return new ProductMetadata(
                fileName, fileDescription, productName, productDescription, productIdentifier,
                schemaNames, List.copyOf(components)
        );
    }

    private static String stringValue(StepValue v) {
        if (v instanceof StepValue.StringValue) {
            StepValue.StringValue str = (StepValue.StringValue) v;
            return str.value();
        }
        if (v instanceof StepValue.ListValue) {
            StepValue.ListValue list = (StepValue.ListValue) v;
            if (!list.elements().isEmpty()) {
                return stringValue(list.elements().get(0));
            }
        }
        return null;
    }
}