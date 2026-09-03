package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.StepBlockVolume;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepFacetedBrepAndBrepWithVoids;
import com.minicad.step.model.StepComplexClippingResult;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepCsgVolume;
import com.minicad.step.model.StepCylinderVolume;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepExtrudedFaceSolid;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepPrismVolume;
import com.minicad.step.model.StepRightCircularConeVolume;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepRevolvedFaceSolid;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSphereVolume;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepSweptFaceSolid;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepTorusVolume;
import com.minicad.step.model.StepAdvancedBrep;
import com.minicad.step.model.StepComplexTriangulatedFace;
import com.minicad.step.model.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepTriangulatedFace;
import com.minicad.step.model.StepFlatPattern;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;

import java.util.List;
import java.util.stream.Collectors;

final class StepCadSolidBuilder {

    private final StepCadBuilder builder;

    StepCadSolidBuilder(StepCadBuilder builder) {
        this.builder = builder;
    }

    // buildSolid dispatch table (first-match; null-fallthrough for conditional-return branches).
    private record SolidRule(Class<? extends StepEntity> type, SolidHandler handler) {}

    private interface SolidHandler {
        Solid build(StepCadSolidBuilder self, StepEntity entity, int id);
    }

    private static SolidRule solidRule(Class<? extends StepEntity> type, SolidHandler handler) {
        return new SolidRule(type, handler);
    }

    private static final List<SolidRule> SOLID_RULES = List.of(
        solidRule(StepManifoldSolidBrep.class, (self, entity, id) -> {
            StepManifoldSolidBrep solidBrep = (StepManifoldSolidBrep) entity;
            return new Solid(self.builder.buildShell(solidBrep.outer().id()));
        }),
        solidRule(StepFacettedBrep.class, (self, entity, id) -> {
            StepFacettedBrep facettedBrep = (StepFacettedBrep) entity;
            return new Solid(self.builder.buildShell(facettedBrep.outer().id()));
        }),
        solidRule(StepBrepWithVoids.class, (self, entity, id) -> {
            StepBrepWithVoids brepWithVoids = (StepBrepWithVoids) entity;
            Shell outerShell = self.builder.buildShell(brepWithVoids.outer().id());
            List<Shell> voidShells = brepWithVoids.voids().stream()
            .map(voidShell -> self.builder.buildShell(voidShell.id()))
            .collect(Collectors.toList());
            return new Solid(outerShell, voidShells);
        }),
        solidRule(StepFacetedBrepAndBrepWithVoids.class, (self, entity, id) -> {
            StepFacetedBrepAndBrepWithVoids facetedBrepWithVoids = (StepFacetedBrepAndBrepWithVoids) entity;
            Shell outerShell = self.builder.buildShell(facetedBrepWithVoids.outer().id());
            List<Shell> voidShells = facetedBrepWithVoids.voids().stream()
            .map(voidShell -> self.builder.buildShell(voidShell.id()))
            .collect(Collectors.toList());
            return new Solid(outerShell, voidShells);
        }),
        solidRule(StepCsgPrimitive.class, (self, entity, id) -> {
            StepCsgPrimitive csgPrimitive = (StepCsgPrimitive) entity;
            return self.builder.buildCsgPrimitive(csgPrimitive);
        }),
        solidRule(StepCsgSolid.class, (self, entity, id) -> {
            StepCsgSolid csgSolid = (StepCsgSolid) entity;
            return self.builder.buildBooleanOperandSolid(csgSolid.treeRootExpression());
        }),
        solidRule(StepSolidReplica.class, (self, entity, id) -> {
            StepSolidReplica solidReplica = (StepSolidReplica) entity;
            return self.builder.transformSolid(
            self.builder.buildSolid(solidReplica.parentSolid().id()),
            solidReplica.transformation()
            );
        }),
        solidRule(StepSweptAreaSolid.class, (self, entity, id) -> {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) entity;
            return self.builder.buildSweptAreaSolid(sweptAreaSolid);
        }),
        solidRule(StepSweptDiskSolid.class, (self, entity, id) -> {
            StepSweptDiskSolid sweptDiskSolid = (StepSweptDiskSolid) entity;
            return self.builder.buildSweptDiskSolid(sweptDiskSolid);
        }),
        solidRule(StepExtrudedAreaSolidTapered.class, (self, entity, id) -> {
            StepExtrudedAreaSolidTapered taperedExtrusion = (StepExtrudedAreaSolidTapered) entity;
            return self.builder.buildExtrudedAreaSolidTapered(taperedExtrusion);
        }),
        solidRule(StepRevolvedAreaSolidTapered.class, (self, entity, id) -> {
            StepRevolvedAreaSolidTapered taperedRevolution = (StepRevolvedAreaSolidTapered) entity;
            return self.builder.buildRevolvedAreaSolidTapered(taperedRevolution);
        }),
        solidRule(StepSurfaceCurveSweptAreaSolid.class, (self, entity, id) -> {
            StepSurfaceCurveSweptAreaSolid surfaceCurveSweep = (StepSurfaceCurveSweptAreaSolid) entity;
            return self.builder.buildSurfaceCurveSweptAreaSolid(surfaceCurveSweep);
        }),
        solidRule(StepBooleanClippingResult.class, (self, entity, id) -> {
            StepBooleanClippingResult clippingResult = (StepBooleanClippingResult) entity;
            return self.builder.buildBooleanResult(
            clippingResult.operator(),
            clippingResult.firstOperand(),
            clippingResult.secondOperand()
            );
        }),
        solidRule(StepBooleanResult.class, (self, entity, id) -> {
            StepBooleanResult booleanResult = (StepBooleanResult) entity;
            return self.builder.buildBooleanResult(
            booleanResult.operator(),
            booleanResult.firstOperand(),
            booleanResult.secondOperand()
            );
        }),
        solidRule(StepNonManifoldSolidBrep.class, (self, entity, id) -> {
            StepNonManifoldSolidBrep nonManifoldBrep = (StepNonManifoldSolidBrep) entity;
            return new Solid(self.builder.buildShell(nonManifoldBrep.outer().id()));
        }),
        solidRule(StepAdvancedBrep.class, (self, entity, id) -> {
            StepAdvancedBrep advancedBrep = (StepAdvancedBrep) entity;
            Shell outerShell = self.builder.buildShell(advancedBrep.outer().id());
            List<Shell> voidShells = advancedBrep.voids().stream()
            .map(v -> self.builder.buildShell(v.id()))
            .collect(Collectors.toList());
            return new Solid(outerShell, voidShells);
        }),
        solidRule(StepComplexClippingResult.class, (self, entity, id) -> {
            StepComplexClippingResult clippingResult = (StepComplexClippingResult) entity;
            return self.builder.buildBooleanResult(
            clippingResult.operator(),
            clippingResult.firstOperand(),
            clippingResult.secondOperand()
            );
        }),
        solidRule(StepCsgVolume.class, (self, entity, id) -> {
            StepCsgVolume csgVolume = (StepCsgVolume) entity;
            return self.builder.buildCsgVolumeSolid(csgVolume);
        }),
        solidRule(StepBlockVolume.class, (self, entity, id) -> {
            StepBlockVolume blockVolume = (StepBlockVolume) entity;
            return self.builder.buildBlockVolume(blockVolume);
        }),
        solidRule(StepHalfSpaceSolid.class, (self, entity, id) -> {
            StepHalfSpaceSolid halfSpace = (StepHalfSpaceSolid) entity;
            return self.builder.buildHalfSpaceSolid(halfSpace);
        }),
        solidRule(StepPolygonalBoundedHalfSpace.class, (self, entity, id) -> {
            StepPolygonalBoundedHalfSpace polyHalfSpace = (StepPolygonalBoundedHalfSpace) entity;
            return self.builder.buildPolygonalBoundedHalfSpace(polyHalfSpace);
        }),
        solidRule(StepTessellatedFaceSet.class, (self, entity, id) -> {
            StepTessellatedFaceSet tessellatedFaceSet = (StepTessellatedFaceSet) entity;
            return new Solid(self.builder.buildTessellatedShell(tessellatedFaceSet));
        }),
        solidRule(StepTessellatedFace.class, (self, entity, id) -> {
            StepTessellatedFace tessellatedFace = (StepTessellatedFace) entity;
            return new Solid(self.builder.buildTessellatedFaceShell(tessellatedFace));
        }),
        solidRule(StepTriangulatedFace.class, (self, entity, id) -> {
            StepTriangulatedFace triangulatedFace = (StepTriangulatedFace) entity;
            return new Solid(self.builder.buildTriangulatedFaceShell(triangulatedFace));
        }),
        solidRule(StepComplexTriangulatedFace.class, (self, entity, id) -> {
            StepComplexTriangulatedFace complexFace = (StepComplexTriangulatedFace) entity;
            return new Solid(self.builder.buildComplexTriangulatedFaceShell(complexFace));
        }),
        solidRule(StepCubicBezierTriangulatedFace.class, (self, entity, id) -> {
            StepCubicBezierTriangulatedFace bezierFace = (StepCubicBezierTriangulatedFace) entity;
            return new Solid(self.builder.buildCubicBezierTriangulatedFaceShell(bezierFace));
        }),
        solidRule(StepExtrudedFaceSolid.class, (self, entity, id) -> {
            StepExtrudedFaceSolid extrudedFace = (StepExtrudedFaceSolid) entity;
            return self.builder.buildExtrudedFaceSolid(extrudedFace);
        }),
        solidRule(StepRevolvedFaceSolid.class, (self, entity, id) -> {
            StepRevolvedFaceSolid revolvedFace = (StepRevolvedFaceSolid) entity;
            return self.builder.buildRevolvedFaceSolid(revolvedFace);
        }),
        solidRule(StepSweptFaceSolid.class, (self, entity, id) -> {
            StepSweptFaceSolid sweptFace = (StepSweptFaceSolid) entity;
            return self.builder.buildSweptFaceSolid(sweptFace);
        }),
        solidRule(StepCylinderVolume.class, (self, entity, id) -> {
            StepCylinderVolume cylVolume = (StepCylinderVolume) entity;
            return self.builder.buildCylinderVolume(cylVolume);
        }),
        solidRule(StepSphereVolume.class, (self, entity, id) -> {
            StepSphereVolume sphereVolume = (StepSphereVolume) entity;
            return self.builder.buildSphereVolume(sphereVolume);
        }),
        solidRule(StepTorusVolume.class, (self, entity, id) -> {
            StepTorusVolume torusVolume = (StepTorusVolume) entity;
            return self.builder.buildTorusVolume(torusVolume);
        }),
        solidRule(StepPrismVolume.class, (self, entity, id) -> {
            StepPrismVolume prismVolume = (StepPrismVolume) entity;
            return self.builder.buildPrismVolume(prismVolume);
        }),
        solidRule(StepRightCircularConeVolume.class, (self, entity, id) -> {
            StepRightCircularConeVolume coneVolume = (StepRightCircularConeVolume) entity;
            return self.builder.buildRightCircularConeVolume(coneVolume);
        }),
        solidRule(StepFiniteElementMesh.class, (self, entity, id) -> {
            StepFiniteElementMesh femMesh = (StepFiniteElementMesh) entity;
            return new Solid(self.builder.buildFiniteElementMeshShell(femMesh));
        }),
        solidRule(StepFlatPattern.class, (self, entity, id) -> {
            StepFlatPattern flatPattern = (StepFlatPattern) entity;
            if (flatPattern.flatGeometry() instanceof StepFaceEntity) {
            StepFaceEntity faceEntity = (StepFaceEntity) flatPattern.flatGeometry();
            return new Solid(new Shell(List.of(self.builder.buildFace(faceEntity.id())), false));
            }
            if (flatPattern.flatGeometry() instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) flatPattern.flatGeometry();
            return new Solid(self.builder.buildShell(openShell.id()));
            }
            throw new UnsupportedGeometryException("FLAT_PATTERN flat geometry must be FACE or OPEN_SHELL");
        }),
        solidRule(StepSolidModel.class, (self, entity, id) -> {
            StepSolidModel solidModel = (StepSolidModel) entity;
            StepEntity actual = self.builder.resolvedEntity(solidModel.id());
            if (actual != null && actual != solidModel && self.canBuildAsSolid(actual)) {
            return self.builder.buildSolid(solidModel.id());
            }
            throw new StepResolutionException(
            "entity #" + id + " is an abstract SOLID_MODEL with no concrete subtype"
            );
        }),
        solidRule(StepMappedItem.class, (self, entity, id) -> {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            return self.builder.buildSolid(mappedItem.mappingTarget().id());
        }),
        solidRule(StepContextDependentShapeRepresentation.class, (self, entity, id) -> {
            StepContextDependentShapeRepresentation cdsr = (StepContextDependentShapeRepresentation) entity;
            StepEntity actual = self.builder.resolvedEntity(cdsr.id());
            if (actual != null && actual != cdsr && self.canBuildAsSolid(actual)) {
            return self.builder.buildSolid(actual.id());
            }
            return null;
        }),
        solidRule(StepItemDefinedTransformation.class, (self, entity, id) -> {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            StepEntity actual = self.builder.resolvedEntity(transformation.id());
            if (actual != null && actual != transformation && self.canBuildAsSolid(actual)) {
            return self.builder.buildSolid(actual.id());
            }
            return null;
        })
    );

    Solid buildSolid(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        for (SolidRule rule : SOLID_RULES) {
            if (!rule.type().isInstance(entity)) {
                continue;
            }
            Solid solid = rule.handler().build(this, entity, id);
            if (solid != null) {
                return solid;
            }
        }

        throw new StepResolutionException("entity #" + id + " is not a supported SOLID");
    }

    boolean canBuildAsSolid(StepEntity entity) {
        return entity instanceof StepManifoldSolidBrep
                || entity instanceof StepFacettedBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepCsgSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSweptDiskSolid
                || entity instanceof StepExtrudedAreaSolidTapered
                || entity instanceof StepRevolvedAreaSolidTapered
                || entity instanceof StepSurfaceCurveSweptAreaSolid
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepBooleanResult
                || entity instanceof StepNonManifoldSolidBrep
                || entity instanceof StepAdvancedBrep
                || entity instanceof StepComplexClippingResult
                || entity instanceof StepCsgVolume
                || entity instanceof StepBlockVolume
                || entity instanceof StepHalfSpaceSolid
                || entity instanceof StepPolygonalBoundedHalfSpace
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
                || entity instanceof StepTriangulatedFace
                || entity instanceof StepComplexTriangulatedFace
                || entity instanceof StepCubicBezierTriangulatedFace
                || entity instanceof StepExtrudedFaceSolid
                || entity instanceof StepRevolvedFaceSolid
                || entity instanceof StepSweptFaceSolid
                || entity instanceof StepCylinderVolume
                || entity instanceof StepSphereVolume
                || entity instanceof StepTorusVolume
                || entity instanceof StepPrismVolume
                || entity instanceof StepRightCircularConeVolume
                || entity instanceof StepSolidModel
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepMappedItem;
    }
}
