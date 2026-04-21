package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.product.StepBlockVolume;
import com.minicad.step.model.product.StepBooleanClippingResult;
import com.minicad.step.model.product.StepBooleanResult;
import com.minicad.step.model.product.StepBrepWithVoids;
import com.minicad.step.model.product.StepComplexClippingResult;
import com.minicad.step.model.product.StepContextDependentShapeRepresentation;
import com.minicad.step.model.product.StepCsgPrimitive;
import com.minicad.step.model.product.StepCsgSolid;
import com.minicad.step.model.product.StepCsgVolume;
import com.minicad.step.model.product.StepCylinderVolume;
import com.minicad.step.model.product.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.product.StepExtrudedFaceSolid;
import com.minicad.step.model.product.StepFacettedBrep;
import com.minicad.step.model.product.StepHalfSpaceSolid;
import com.minicad.step.model.product.StepItemDefinedTransformation;
import com.minicad.step.model.product.StepManifoldSolidBrep;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.product.StepNonManifoldSolidBrep;
import com.minicad.step.model.product.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.product.StepPrismVolume;
import com.minicad.step.model.product.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.product.StepRevolvedFaceSolid;
import com.minicad.step.model.product.StepSolidModel;
import com.minicad.step.model.product.StepSolidReplica;
import com.minicad.step.model.product.StepSphereVolume;
import com.minicad.step.model.product.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.product.StepSweptAreaSolid;
import com.minicad.step.model.product.StepSweptDiskSolid;
import com.minicad.step.model.product.StepSweptFaceSolid;
import com.minicad.step.model.product.StepTessellatedFace;
import com.minicad.step.model.product.StepTessellatedFaceSet;
import com.minicad.step.model.product.StepTorusVolume;
import com.minicad.step.model.topology.StepAdvancedBrep;
import com.minicad.step.model.topology.StepComplexTriangulatedFace;
import com.minicad.step.model.topology.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.topology.StepOpenShell;
import com.minicad.step.model.topology.StepTriangulatedFace;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;

import java.util.List;

final class StepSolidBuilder {

    private final StepCadBuilder builder;

    StepSolidBuilder(StepCadBuilder builder) {
        this.builder = builder;
    }

    Solid buildSolid(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        if (entity instanceof StepManifoldSolidBrep solidBrep) {
            return new Solid(builder.buildShell(solidBrep.outer().id()));
        }
        if (entity instanceof StepFacettedBrep facettedBrep) {
            return new Solid(builder.buildShell(facettedBrep.outer().id()));
        }
        if (entity instanceof StepBrepWithVoids brepWithVoids) {
            Shell outerShell = builder.buildShell(brepWithVoids.outer().id());
            List<Shell> voidShells = brepWithVoids.voids().stream()
                    .map(voidShell -> builder.buildShell(voidShell.id()))
                    .toList();
            return new Solid(outerShell, voidShells);
        }
        if (entity instanceof StepCsgPrimitive csgPrimitive) {
            return builder.buildCsgPrimitive(csgPrimitive);
        }
        if (entity instanceof StepCsgSolid csgSolid) {
            return builder.buildBooleanOperandSolid(csgSolid.treeRootExpression());
        }
        if (entity instanceof StepSolidReplica solidReplica) {
            return builder.transformSolid(
                    builder.buildSolid(solidReplica.parentSolid().id()),
                    solidReplica.transformation()
            );
        }
        if (entity instanceof StepSweptAreaSolid sweptAreaSolid) {
            return builder.buildSweptAreaSolid(sweptAreaSolid);
        }
        if (entity instanceof StepSweptDiskSolid sweptDiskSolid) {
            return builder.buildSweptDiskSolid(sweptDiskSolid);
        }
        if (entity instanceof StepExtrudedAreaSolidTapered taperedExtrusion) {
            return builder.buildExtrudedAreaSolidTapered(taperedExtrusion);
        }
        if (entity instanceof StepRevolvedAreaSolidTapered taperedRevolution) {
            return builder.buildRevolvedAreaSolidTapered(taperedRevolution);
        }
        if (entity instanceof StepSurfaceCurveSweptAreaSolid surfaceCurveSweep) {
            return builder.buildSurfaceCurveSweptAreaSolid(surfaceCurveSweep);
        }
        if (entity instanceof StepBooleanClippingResult clippingResult) {
            return builder.buildBooleanResult(
                    clippingResult.operator(),
                    clippingResult.firstOperand(),
                    clippingResult.secondOperand()
            );
        }
        if (entity instanceof StepBooleanResult booleanResult) {
            return builder.buildBooleanResult(
                    booleanResult.operator(),
                    booleanResult.firstOperand(),
                    booleanResult.secondOperand()
            );
        }
        if (entity instanceof StepNonManifoldSolidBrep nonManifoldBrep) {
            return new Solid(builder.buildShell(nonManifoldBrep.outer().id()));
        }
        if (entity instanceof StepAdvancedBrep advancedBrep) {
            Shell outerShell = builder.buildShell(advancedBrep.outer().id());
            List<Shell> voidShells = advancedBrep.voids().stream()
                    .map(v -> builder.buildShell(v.id()))
                    .toList();
            return new Solid(outerShell, voidShells);
        }
        if (entity instanceof StepComplexClippingResult clippingResult) {
            return builder.buildBooleanResult(
                    clippingResult.operator(),
                    clippingResult.firstOperand(),
                    clippingResult.secondOperand()
            );
        }
        if (entity instanceof StepCsgVolume csgVolume) {
            return builder.buildCsgVolumeSolid(csgVolume);
        }
        if (entity instanceof StepBlockVolume blockVolume) {
            return builder.buildBlockVolume(blockVolume);
        }
        if (entity instanceof StepHalfSpaceSolid halfSpace) {
            return builder.buildHalfSpaceSolid(halfSpace);
        }
        if (entity instanceof StepPolygonalBoundedHalfSpace polyHalfSpace) {
            return builder.buildPolygonalBoundedHalfSpace(polyHalfSpace);
        }
        if (entity instanceof StepTessellatedFaceSet tessellatedFaceSet) {
            return new Solid(builder.buildTessellatedShell(tessellatedFaceSet));
        }
        if (entity instanceof StepTessellatedFace tessellatedFace) {
            return new Solid(builder.buildTessellatedFaceShell(tessellatedFace));
        }
        if (entity instanceof StepTriangulatedFace triangulatedFace) {
            return new Solid(builder.buildTriangulatedFaceShell(triangulatedFace));
        }
        if (entity instanceof StepComplexTriangulatedFace complexFace) {
            return new Solid(builder.buildComplexTriangulatedFaceShell(complexFace));
        }
        if (entity instanceof StepCubicBezierTriangulatedFace bezierFace) {
            return new Solid(builder.buildCubicBezierTriangulatedFaceShell(bezierFace));
        }
        if (entity instanceof StepExtrudedFaceSolid extrudedFace) {
            return builder.buildExtrudedFaceSolid(extrudedFace);
        }
        if (entity instanceof StepRevolvedFaceSolid revolvedFace) {
            return builder.buildRevolvedFaceSolid(revolvedFace);
        }
        if (entity instanceof StepSweptFaceSolid sweptFace) {
            return builder.buildSweptFaceSolid(sweptFace);
        }
        if (entity instanceof StepCylinderVolume cylVolume) {
            return builder.buildCylinderVolume(cylVolume);
        }
        if (entity instanceof StepSphereVolume sphereVolume) {
            return builder.buildSphereVolume(sphereVolume);
        }
        if (entity instanceof StepTorusVolume torusVolume) {
            return builder.buildTorusVolume(torusVolume);
        }
        if (entity instanceof StepPrismVolume prismVolume) {
            return builder.buildPrismVolume(prismVolume);
        }
        if (entity instanceof StepFiniteElementMesh femMesh) {
            return new Solid(builder.buildFiniteElementMeshShell(femMesh));
        }
        if (entity instanceof StepFlatPattern flatPattern) {
            if (flatPattern.flatGeometry() instanceof StepFaceEntity faceEntity) {
                return new Solid(new Shell(List.of(builder.buildFace(faceEntity.id())), false));
            }
            if (flatPattern.flatGeometry() instanceof StepOpenShell openShell) {
                return new Solid(builder.buildShell(openShell.id()));
            }
            throw new UnsupportedGeometryException("FLAT_PATTERN flat geometry must be FACE or OPEN_SHELL");
        }
        if (entity instanceof StepSolidModel solidModel) {
            StepEntity actual = builder.resolvedEntity(solidModel.id());
            if (actual != null && actual != solidModel && canBuildAsSolid(actual)) {
                return builder.buildSolid(solidModel.id());
            }
            throw new StepResolutionException(
                    "entity #" + id + " is an abstract SOLID_MODEL with no concrete subtype"
            );
        }
        if (entity instanceof StepMappedItem mappedItem) {
            return builder.buildSolid(mappedItem.mappingTarget().id());
        }
        if (entity instanceof StepContextDependentShapeRepresentation cdsr) {
            StepEntity actual = builder.resolvedEntity(cdsr.id());
            if (actual != null && actual != cdsr && canBuildAsSolid(actual)) {
                return builder.buildSolid(actual.id());
            }
        }
        if (entity instanceof StepItemDefinedTransformation transformation) {
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
                || entity instanceof StepSolidModel
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepMappedItem;
    }
}
