package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepPlanarBox;
import com.minicad.step.model.StepPlanarExtent;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepManifoldSurfaceModel;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepFlatPattern;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricSurfaceSet;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepComplexTriangulatedFace;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.StepEdgeWire;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepTriangulatedFace;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepWireShell;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final class StepShellBuilder {

    private final StepCadBuilder builder;

    StepShellBuilder(StepCadBuilder builder) {
        this.builder = builder;
    }

    // buildShell dispatch table (first-match-return, mirrors the original sequential ifs).
    private record ShellRule(Class<? extends StepEntity> type, ShellHandler handler) {}

    private interface ShellHandler {
        Shell build(StepShellBuilder self, StepEntity entity, int id);
    }

    private static ShellRule shellRule(Class<? extends StepEntity> type, ShellHandler handler) {
        return new ShellRule(type, handler);
    }

    private static final List<ShellRule> SHELL_RULES = List.of(
        shellRule(StepOpenShell.class, (self, entity, id) -> {
            StepOpenShell openShell = (StepOpenShell) entity;
            return self.buildFaceShell(openShell.faces(), false);
        }),
        shellRule(StepSurfacedOpenShell.class, (self, entity, id) -> {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
            return self.buildFaceShell(surfacedOpenShell.faces(), false);
        }),
        shellRule(StepOrientedOpenShell.class, (self, entity, id) -> {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
            return self.buildFaceShell(orientedOpenShell.faces(), false);
        }),
        shellRule(StepClosedShell.class, (self, entity, id) -> {
            StepClosedShell closedShell = (StepClosedShell) entity;
            return self.buildFaceShell(closedShell.faces(), true);
        }),
        shellRule(StepOrientedClosedShell.class, (self, entity, id) -> {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
            return self.buildFaceShell(orientedClosedShell.faces(), true);
        }),
        shellRule(StepTessellatedFace.class, (self, entity, id) -> {
            StepTessellatedFace tessellated = (StepTessellatedFace) entity;
            return self.builder.buildTessellatedFaceShell(tessellated);
        }),
        shellRule(StepTessellatedFaceSet.class, (self, entity, id) -> {
            StepTessellatedFaceSet tessellated = (StepTessellatedFaceSet) entity;
            return self.builder.buildTessellatedShell(tessellated);
        }),
        shellRule(StepTriangulatedFace.class, (self, entity, id) -> {
            StepTriangulatedFace triangulated = (StepTriangulatedFace) entity;
            return self.builder.buildTriangulatedFaceShell(triangulated);
        }),
        shellRule(StepComplexTriangulatedFace.class, (self, entity, id) -> {
            StepComplexTriangulatedFace complex = (StepComplexTriangulatedFace) entity;
            return self.builder.buildComplexTriangulatedFaceShell(complex);
        }),
        shellRule(StepCubicBezierTriangulatedFace.class, (self, entity, id) -> {
            StepCubicBezierTriangulatedFace bezier = (StepCubicBezierTriangulatedFace) entity;
            return self.builder.buildCubicBezierTriangulatedFaceShell(bezier);
        }),
        shellRule(StepVertexShell.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepWireShell.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepConnectedFaceSet.class, (self, entity, id) -> {
            StepConnectedFaceSet connectedFaces = (StepConnectedFaceSet) entity;
            return self.buildConnectedFaceSet(connectedFaces);
        }),
        shellRule(StepConnectedFaceSubSet.class, (self, entity, id) -> {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return self.buildConnectedFaceSubSet(connectedFaceSubSet);
        }),
        shellRule(StepGeometricCurveSet.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepGeometricSet.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepGeometricSurfaceSet.class, (self, entity, id) -> {
            StepGeometricSurfaceSet surfaceSet = (StepGeometricSurfaceSet) entity;
            return self.buildGeometricSurfaceSetShell(surfaceSet);
        }),
        shellRule(StepFaceBasedSurfaceModel.class, (self, entity, id) -> {
            StepFaceBasedSurfaceModel faceModel = (StepFaceBasedSurfaceModel) entity;
            return self.buildFaceBasedSurfaceModel(faceModel);
        }),
        shellRule(StepManifoldSurfaceModel.class, (self, entity, id) -> {
            StepManifoldSurfaceModel manifoldModel = (StepManifoldSurfaceModel) entity;
            return self.buildManifoldSurfaceModel(manifoldModel);
        }),
        shellRule(StepShellBasedSurfaceModel.class, (self, entity, id) -> {
            StepShellBasedSurfaceModel shellModel = (StepShellBasedSurfaceModel) entity;
            return self.buildShellBasedSurfaceModel(shellModel);
        }),
        shellRule(StepShellBasedWireframeModel.class, (self, entity, id) -> {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) entity;
            return self.buildShellBasedWireframeModel(wireframeModel);
        }),
        shellRule(StepSurfacePatch.class, (self, entity, id) -> {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) entity;
            return self.buildSurfacePatchShell(surfacePatch);
        }),
        shellRule(StepEdgeBasedWireframeModel.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepConnectedEdgeSet.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepEdgeWire.class, (self, entity, id) -> {
            return new Shell(List.of(), false);
        }),
        shellRule(StepPlanarBox.class, (self, entity, id) -> {
            StepPlanarBox planarBox = (StepPlanarBox) entity;
            return self.buildPlanarBoxShell(planarBox);
        }),
        shellRule(StepPlanarExtent.class, (self, entity, id) -> {
            StepPlanarExtent planarExtent = (StepPlanarExtent) entity;
            return self.buildPlanarExtentShell(planarExtent);
        }),
        shellRule(StepPointSet.class, (self, entity, id) -> {
            throw new UnsupportedGeometryException("POINT_SET cannot be converted to a B-Rep shell");
        }),
        shellRule(StepFiniteElementMesh.class, (self, entity, id) -> {
            StepFiniteElementMesh femMesh = (StepFiniteElementMesh) entity;
            return self.builder.buildFiniteElementMeshShell(femMesh);
        }),
        shellRule(StepFlatPattern.class, (self, entity, id) -> {
            StepFlatPattern flatPattern = (StepFlatPattern) entity;
            if (flatPattern.flatGeometry() instanceof StepFaceEntity) {
            StepFaceEntity faceEntity = (StepFaceEntity) flatPattern.flatGeometry();
            return new Shell(List.of(self.builder.buildFace(faceEntity.id())), false);
            }
            if (flatPattern.flatGeometry() instanceof StepOpenShell
            || flatPattern.flatGeometry() instanceof StepClosedShell) {
            return self.builder.buildShell(flatPattern.flatGeometry().id());
            }
            return new Shell(List.of(), false);
        }),
        shellRule(StepMappedItem.class, (self, entity, id) -> {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            return self.builder.buildShell(mappedItem.mappingTarget().id());
        }),
        shellRule(StepSurfaceModel.class, (self, entity, id) -> {
            StepSurfaceModel surfaceModel = (StepSurfaceModel) entity;
            StepEntity actual = self.builder.resolvedEntity(surfaceModel.id());
            if (actual != null && actual != surfaceModel) {
            return self.builder.buildShell(actual.id());
            }
            throw new StepResolutionException(
            "entity #" + id + " is an abstract SURFACE_MODEL with no concrete subtype"
            );
        })
    );

    Shell buildShell(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        for (ShellRule rule : SHELL_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().build(this, entity, id);
            }
        }

        throw new StepResolutionException(
                "entity #" + id + " is not an OPEN_SHELL, SURFACED_OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL"
        );
    }

    private Shell buildFaceShell(List<? extends StepFaceEntity> faces, boolean closed) {
        return new Shell(faces.stream().map(face -> builder.buildFace(face.id())).collect(Collectors.toList()), closed);
    }

    private Shell buildConnectedFaceSet(StepConnectedFaceSet connectedFaces) {
        List<Face> faces = connectedFaces.faces().stream()
                .map(face -> builder.buildFace(face.id()))
                .collect(Collectors.toList());
        return new Shell(faces, !faces.isEmpty());
    }

    private Shell buildConnectedFaceSubSet(StepConnectedFaceSubSet connectedFaceSubSet) {
        List<Face> faces = connectedFaceSubSet.faces().stream()
                .map(face -> builder.buildFace(face.id()))
                .collect(Collectors.toList());
        return new Shell(faces, !faces.isEmpty());
    }

    private Shell buildGeometricSurfaceSetShell(StepGeometricSurfaceSet surfaceSet) {
        for (StepEntity element : surfaceSet.elements()) {
            SurfaceGeometry surface = builder.buildSupportedFaceGeometry(element, "SURFACE");
            if (surface != null) {
                throw new UnsupportedGeometryException(
                        "GEOMETRIC_SURFACE_SET shell construction requires bounded face geometry"
                );
            }
        }
        throw new UnsupportedGeometryException(
                "GEOMETRIC_SURFACE_SET shell construction requires supported surface elements"
        );
    }

    private Shell buildFaceBasedSurfaceModel(StepFaceBasedSurfaceModel faceModel) {
        List<Face> faces = new ArrayList<>();
        for (StepEntity faceSet : faceModel.faceSets()) {
            if (faceSet instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet connectedFaces = (StepConnectedFaceSet) faceSet;
                faces.addAll(buildConnectedFaceSet(connectedFaces).faces());
            }
        }
        return new Shell(faces, !faces.isEmpty());
    }

    private Shell buildManifoldSurfaceModel(StepManifoldSurfaceModel manifoldModel) {
        List<Face> allFaces = new ArrayList<>();
        for (StepEntity shellEntity : manifoldModel.shells()) {
            if (shellEntity instanceof StepOpenShell
                    || shellEntity instanceof StepClosedShell
                    || shellEntity instanceof StepOrientedOpenShell
                    || shellEntity instanceof StepOrientedClosedShell) {
                allFaces.addAll(builder.buildShell(shellEntity.id()).faces());
            }
        }
        return new Shell(allFaces, !allFaces.isEmpty());
    }

    private Shell buildShellBasedSurfaceModel(StepShellBasedSurfaceModel shellModel) {
        List<Face> allFaces = new ArrayList<>();
        for (StepEntity shellEntity : shellModel.shells()) {
            allFaces.addAll(builder.buildShell(shellEntity.id()).faces());
        }
        return new Shell(allFaces, !allFaces.isEmpty());
    }

    private Shell buildSurfacePatchShell(StepSurfacePatch surfacePatch) {
        SurfaceGeometry surface = builder.buildSupportedFaceGeometry(surfacePatch.basisSurface(), "SURFACE_PATCH");
        if (surface == null) {
            return new Shell(List.of(), false);
        }
        CartesianPoint p1;
        CartesianPoint p2;
        CartesianPoint p3;
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            p1 = plane.origin();
            Vector3 xDir = plane.normal().asVector().cross(new Vector3(1, 0, 0));
            if (xDir.norm() < Epsilon.EPS) {
                xDir = plane.normal().asVector().cross(new Vector3(0, 1, 0));
            }
            xDir = xDir.normalize().asVector().scale(1.0);
            Vector3 yDir = plane.normal().asVector().cross(xDir).normalize().asVector().scale(1.0);
            p2 = new CartesianPoint(
                    p1.x() + xDir.x() + yDir.x(),
                    p1.y() + xDir.y() + yDir.y(),
                    p1.z() + xDir.z() + yDir.z()
            );
            p3 = new CartesianPoint(
                    p1.x() + xDir.x() - yDir.x(),
                    p1.y() + xDir.y() - yDir.y(),
                    p1.z() + xDir.z() - yDir.z()
            );
        } else {
            BoundingBox3 box = surface.boundingBox();
            if (box == null || box.isEmpty()) {
                return new Shell(List.of(), false);
            }
            p1 = new CartesianPoint(box.minX(), box.minY(), box.minZ());
            p2 = new CartesianPoint(box.maxX(), box.minY(), box.minZ());
            p3 = new CartesianPoint(box.minX(), box.maxY(), box.minZ());
        }
        if (p1.distanceTo(p2) < Epsilon.EPS || p1.distanceTo(p3) < Epsilon.EPS || p2.distanceTo(p3) < Epsilon.EPS) {
            return new Shell(List.of(), false);
        }
        PolyLoop loop = new PolyLoop(List.of(p1, p2, p3));
        Face face = new Face(surface, List.of(FaceBound.outer(loop, true)), surfacePatch.sameSense());
        return new Shell(List.of(face), false);
    }

    private Shell buildShellBasedWireframeModel(StepShellBasedWireframeModel wireframeModel) {
        List<Face> allFaces = new ArrayList<>();
        for (StepEntity boundary : wireframeModel.boundaries()) {
            if (boundary instanceof StepOpenShell
                    || boundary instanceof StepClosedShell
                    || boundary instanceof StepOrientedOpenShell
                    || boundary instanceof StepOrientedClosedShell
                    || boundary instanceof StepTessellatedFaceSet
                    || boundary instanceof StepConnectedFaceSet
                    || boundary instanceof StepConnectedFaceSubSet
                    || boundary instanceof StepPlanarBox
                    || boundary instanceof StepPlanarExtent) {
                allFaces.addAll(builder.buildShell(boundary.id()).faces());
            }
        }
        return new Shell(allFaces, false);
    }

    private Shell buildPlanarBoxShell(StepPlanarBox planarBox) {
        CartesianPoint corner = new CartesianPoint(0.0, 0.0, 0.0);
        Direction3 normal = new Direction3(0.0, 0.0, 1.0);
        Direction3 xDir = new Direction3(1.0, 0.0, 0.0);
        if (planarBox.placement() instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) planarBox.placement();
            corner = builder.buildPoint(placement.location().id());
            if (placement.axis() != null) {
                normal = builder.buildDirection(placement.axis().id());
            }
            if (placement.refDirection() != null) {
                xDir = builder.buildDirection(placement.refDirection().id());
            }
        }
        double width = planarBox.width();
        double height = planarBox.height();
        Vector3 xVec = xDir.asVector().scale(width);
        Vector3 yVec = normal.cross(xDir).asVector().scale(height);
        CartesianPoint p1 = corner;
        CartesianPoint p2 = new CartesianPoint(p1.x() + xVec.x(), p1.y() + xVec.y(), p1.z() + xVec.z());
        CartesianPoint p3 = new CartesianPoint(p2.x() + yVec.x(), p2.y() + yVec.y(), p2.z() + yVec.z());
        CartesianPoint p4 = new CartesianPoint(p1.x() + yVec.x(), p1.y() + yVec.y(), p1.z() + yVec.z());
        Plane plane = new Plane(p1, normal);
        PolyLoop polyLoop = new PolyLoop(List.of(p1, p2, p3, p4));
        Face face = new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true);
        return new Shell(List.of(face), false);
    }

    private Shell buildPlanarExtentShell(StepPlanarExtent planarExtent) {
        double halfW = planarExtent.width() * 0.5;
        double halfH = planarExtent.height() * 0.5;
        CartesianPoint p1 = new CartesianPoint(-halfW, -halfH, 0.0);
        CartesianPoint p2 = new CartesianPoint(halfW, -halfH, 0.0);
        CartesianPoint p3 = new CartesianPoint(halfW, halfH, 0.0);
        CartesianPoint p4 = new CartesianPoint(-halfW, halfH, 0.0);
        Plane plane = new Plane(p1, new Direction3(0.0, 0.0, 1.0));
        PolyLoop polyLoop = new PolyLoop(List.of(p1, p2, p3, p4));
        Face face = new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true);
        return new Shell(List.of(face), false);
    }
}
