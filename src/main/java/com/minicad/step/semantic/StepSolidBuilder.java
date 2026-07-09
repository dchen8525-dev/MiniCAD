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

final class StepSolidBuilder {

    private final StepCadBuilder builder;

    StepSolidBuilder(StepCadBuilder builder) {
        this.builder = builder;
    }

    Solid buildSolid(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        if (entity instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solidBrep = (StepManifoldSolidBrep) entity;
            return new Solid(builder.buildShell(solidBrep.outer().id()));
        }
        if (entity instanceof StepFacettedBrep) {
            StepFacettedBrep facettedBrep = (StepFacettedBrep) entity;
            return new Solid(builder.buildShell(facettedBrep.outer().id()));
        }
        if (entity instanceof StepBrepWithVoids) {
            StepBrepWithVoids brepWithVoids = (StepBrepWithVoids) entity;
            Shell outerShell = builder.buildShell(brepWithVoids.outer().id());
            List<Shell> voidShells = brepWithVoids.voids().stream()
                    .map(voidShell -> builder.buildShell(voidShell.id()))
                    .collect(Collectors.toList());
            return new Solid(outerShell, voidShells);
        }
        if (entity instanceof StepCsgPrimitive) {
            StepCsgPrimitive csgPrimitive = (StepCsgPrimitive) entity;
            return builder.buildCsgPrimitive(csgPrimitive);
        }
        if (entity instanceof StepCsgSolid) {
            StepCsgSolid csgSolid = (StepCsgSolid) entity;
            return builder.buildBooleanOperandSolid(csgSolid.treeRootExpression());
        }
        if (entity instanceof StepSolidReplica) {
            StepSolidReplica solidReplica = (StepSolidReplica) entity;
            return builder.transformSolid(
                    builder.buildSolid(solidReplica.parentSolid().id()),
                    solidReplica.transformation()
            );
        }
        if (entity instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) entity;
            return builder.buildSweptAreaSolid(sweptAreaSolid);
        }
        if (entity instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid sweptDiskSolid = (StepSweptDiskSolid) entity;
            return builder.buildSweptDiskSolid(sweptDiskSolid);
        }
        if (entity instanceof StepExtrudedAreaSolidTapered) {
            StepExtrudedAreaSolidTapered taperedExtrusion = (StepExtrudedAreaSolidTapered) entity;
            return builder.buildExtrudedAreaSolidTapered(taperedExtrusion);
        }
        if (entity instanceof StepRevolvedAreaSolidTapered) {
            StepRevolvedAreaSolidTapered taperedRevolution = (StepRevolvedAreaSolidTapered) entity;
            return builder.buildRevolvedAreaSolidTapered(taperedRevolution);
        }
        if (entity instanceof StepSurfaceCurveSweptAreaSolid) {
            StepSurfaceCurveSweptAreaSolid surfaceCurveSweep = (StepSurfaceCurveSweptAreaSolid) entity;
            return builder.buildSurfaceCurveSweptAreaSolid(surfaceCurveSweep);
        }
        if (entity instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult clippingResult = (StepBooleanClippingResult) entity;
            return builder.buildBooleanResult(
                    clippingResult.operator(),
                    clippingResult.firstOperand(),
                    clippingResult.secondOperand()
            );
        }
        if (entity instanceof StepBooleanResult) {
            StepBooleanResult booleanResult = (StepBooleanResult) entity;
            return builder.buildBooleanResult(
                    booleanResult.operator(),
                    booleanResult.firstOperand(),
                    booleanResult.secondOperand()
            );
        }
        if (entity instanceof StepNonManifoldSolidBrep) {
            StepNonManifoldSolidBrep nonManifoldBrep = (StepNonManifoldSolidBrep) entity;
            return new Solid(builder.buildShell(nonManifoldBrep.outer().id()));
        }
        if (entity instanceof StepAdvancedBrep) {
            StepAdvancedBrep advancedBrep = (StepAdvancedBrep) entity;
            Shell outerShell = builder.buildShell(advancedBrep.outer().id());
            List<Shell> voidShells = advancedBrep.voids().stream()
                    .map(v -> builder.buildShell(v.id()))
                    .collect(Collectors.toList());
            return new Solid(outerShell, voidShells);
        }
        if (entity instanceof StepComplexClippingResult) {
            StepComplexClippingResult clippingResult = (StepComplexClippingResult) entity;
            return builder.buildBooleanResult(
                    clippingResult.operator(),
                    clippingResult.firstOperand(),
                    clippingResult.secondOperand()
            );
        }
        if (entity instanceof StepCsgVolume) {
            StepCsgVolume csgVolume = (StepCsgVolume) entity;
            return builder.buildCsgVolumeSolid(csgVolume);
        }
        if (entity instanceof StepBlockVolume) {
            StepBlockVolume blockVolume = (StepBlockVolume) entity;
            return builder.buildBlockVolume(blockVolume);
        }
        if (entity instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid halfSpace = (StepHalfSpaceSolid) entity;
            return builder.buildHalfSpaceSolid(halfSpace);
        }
        if (entity instanceof StepPolygonalBoundedHalfSpace) {
            StepPolygonalBoundedHalfSpace polyHalfSpace = (StepPolygonalBoundedHalfSpace) entity;
            return builder.buildPolygonalBoundedHalfSpace(polyHalfSpace);
        }
        if (entity instanceof StepTessellatedFaceSet) {
            StepTessellatedFaceSet tessellatedFaceSet = (StepTessellatedFaceSet) entity;
            return new Solid(builder.buildTessellatedShell(tessellatedFaceSet));
        }
        if (entity instanceof StepTessellatedFace) {
            StepTessellatedFace tessellatedFace = (StepTessellatedFace) entity;
            return new Solid(builder.buildTessellatedFaceShell(tessellatedFace));
        }
        if (entity instanceof StepTriangulatedFace) {
            StepTriangulatedFace triangulatedFace = (StepTriangulatedFace) entity;
            return new Solid(builder.buildTriangulatedFaceShell(triangulatedFace));
        }
        if (entity instanceof StepComplexTriangulatedFace) {
            StepComplexTriangulatedFace complexFace = (StepComplexTriangulatedFace) entity;
            return new Solid(builder.buildComplexTriangulatedFaceShell(complexFace));
        }
        if (entity instanceof StepCubicBezierTriangulatedFace) {
            StepCubicBezierTriangulatedFace bezierFace = (StepCubicBezierTriangulatedFace) entity;
            return new Solid(builder.buildCubicBezierTriangulatedFaceShell(bezierFace));
        }
        if (entity instanceof StepExtrudedFaceSolid) {
            StepExtrudedFaceSolid extrudedFace = (StepExtrudedFaceSolid) entity;
            return builder.buildExtrudedFaceSolid(extrudedFace);
        }
        if (entity instanceof StepRevolvedFaceSolid) {
            StepRevolvedFaceSolid revolvedFace = (StepRevolvedFaceSolid) entity;
            return builder.buildRevolvedFaceSolid(revolvedFace);
        }
        if (entity instanceof StepSweptFaceSolid) {
            StepSweptFaceSolid sweptFace = (StepSweptFaceSolid) entity;
            return builder.buildSweptFaceSolid(sweptFace);
        }
        if (entity instanceof StepCylinderVolume) {
            StepCylinderVolume cylVolume = (StepCylinderVolume) entity;
            return builder.buildCylinderVolume(cylVolume);
        }
        if (entity instanceof StepSphereVolume) {
            StepSphereVolume sphereVolume = (StepSphereVolume) entity;
            return builder.buildSphereVolume(sphereVolume);
        }
        if (entity instanceof StepTorusVolume) {
            StepTorusVolume torusVolume = (StepTorusVolume) entity;
            return builder.buildTorusVolume(torusVolume);
        }
        if (entity instanceof StepPrismVolume) {
            StepPrismVolume prismVolume = (StepPrismVolume) entity;
            return builder.buildPrismVolume(prismVolume);
        }
        if (entity instanceof StepRightCircularConeVolume) {
            StepRightCircularConeVolume coneVolume = (StepRightCircularConeVolume) entity;
            return builder.buildRightCircularConeVolume(coneVolume);
        }
        if (entity instanceof StepFiniteElementMesh) {
            StepFiniteElementMesh femMesh = (StepFiniteElementMesh) entity;
            return new Solid(builder.buildFiniteElementMeshShell(femMesh));
        }
        if (entity instanceof StepFlatPattern) {
            StepFlatPattern flatPattern = (StepFlatPattern) entity;
            if (flatPattern.flatGeometry() instanceof StepFaceEntity) {
                StepFaceEntity faceEntity = (StepFaceEntity) flatPattern.flatGeometry();
                return new Solid(new Shell(List.of(builder.buildFace(faceEntity.id())), false));
            }
            if (flatPattern.flatGeometry() instanceof StepOpenShell) {
                StepOpenShell openShell = (StepOpenShell) flatPattern.flatGeometry();
                return new Solid(builder.buildShell(openShell.id()));
            }
            throw new UnsupportedGeometryException("FLAT_PATTERN flat geometry must be FACE or OPEN_SHELL");
        }
        if (entity instanceof StepSolidModel) {
            StepSolidModel solidModel = (StepSolidModel) entity;
            StepEntity actual = builder.resolvedEntity(solidModel.id());
            if (actual != null && actual != solidModel && canBuildAsSolid(actual)) {
                return builder.buildSolid(solidModel.id());
            }
            throw new StepResolutionException(
                    "entity #" + id + " is an abstract SOLID_MODEL with no concrete subtype"
            );
        }
        if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            return builder.buildSolid(mappedItem.mappingTarget().id());
        }
        if (entity instanceof StepContextDependentShapeRepresentation) {
            StepContextDependentShapeRepresentation cdsr = (StepContextDependentShapeRepresentation) entity;
            StepEntity actual = builder.resolvedEntity(cdsr.id());
            if (actual != null && actual != cdsr && canBuildAsSolid(actual)) {
                return builder.buildSolid(actual.id());
            }
        }
        if (entity instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            StepEntity actual = builder.resolvedEntity(transformation.id());
            if (actual != null && actual != transformation && canBuildAsSolid(actual)) {
                return builder.buildSolid(actual.id());
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
