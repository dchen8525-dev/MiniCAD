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
import com.minicad.step.model.annotation.StepPlanarBox;
import com.minicad.step.model.annotation.StepPlanarExtent;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.geometry.StepManifoldSurfaceModel;
import com.minicad.step.model.geometry.StepPointSet;
import com.minicad.step.model.geometry.StepSurfaceModel;
import com.minicad.step.model.geometry.StepSurfacePatch;
import com.minicad.step.model.geometry.StepSurfacedOpenShell;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.step.model.product.StepEdgeBasedWireframeModel;
import com.minicad.step.model.product.StepFaceBasedSurfaceModel;
import com.minicad.step.model.product.StepGeometricCurveSet;
import com.minicad.step.model.product.StepGeometricSet;
import com.minicad.step.model.product.StepGeometricSurfaceSet;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.product.StepShellBasedSurfaceModel;
import com.minicad.step.model.product.StepShellBasedWireframeModel;
import com.minicad.step.model.product.StepTessellatedFace;
import com.minicad.step.model.product.StepTessellatedFaceSet;
import com.minicad.step.model.topology.StepClosedShell;
import com.minicad.step.model.topology.StepComplexTriangulatedFace;
import com.minicad.step.model.topology.StepConnectedEdgeSet;
import com.minicad.step.model.topology.StepConnectedFaceSet;
import com.minicad.step.model.topology.StepConnectedFaceSubSet;
import com.minicad.step.model.topology.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.topology.StepEdgeWire;
import com.minicad.step.model.topology.StepOpenShell;
import com.minicad.step.model.topology.StepOrientedClosedShell;
import com.minicad.step.model.topology.StepOrientedOpenShell;
import com.minicad.step.model.topology.StepTriangulatedFace;
import com.minicad.step.model.topology.StepVertexShell;
import com.minicad.step.model.topology.StepWireShell;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;

import java.util.ArrayList;
import java.util.List;

final class StepShellBuilder {

    private final StepCadBuilder builder;

    StepShellBuilder(StepCadBuilder builder) {
        this.builder = builder;
    }

    Shell buildShell(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        if (entity instanceof StepOpenShell openShell) {
            return buildFaceShell(openShell.faces(), false);
        }
        if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
            return buildFaceShell(surfacedOpenShell.faces(), false);
        }
        if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
            return buildFaceShell(orientedOpenShell.faces(), false);
        }
        if (entity instanceof StepClosedShell closedShell) {
            return buildFaceShell(closedShell.faces(), true);
        }
        if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
            return buildFaceShell(orientedClosedShell.faces(), true);
        }
        if (entity instanceof StepTessellatedFace tessellated) {
            return builder.buildTessellatedFaceShell(tessellated);
        }
        if (entity instanceof StepTessellatedFaceSet tessellated) {
            return builder.buildTessellatedShell(tessellated);
        }
        if (entity instanceof StepTriangulatedFace triangulated) {
            return builder.buildTriangulatedFaceShell(triangulated);
        }
        if (entity instanceof StepComplexTriangulatedFace complex) {
            return builder.buildComplexTriangulatedFaceShell(complex);
        }
        if (entity instanceof StepCubicBezierTriangulatedFace bezier) {
            return builder.buildCubicBezierTriangulatedFaceShell(bezier);
        }
        if (entity instanceof StepVertexShell) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepWireShell) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepConnectedFaceSet connectedFaces) {
            return buildConnectedFaceSet(connectedFaces);
        }
        if (entity instanceof StepConnectedFaceSubSet connectedFaceSubSet) {
            return buildConnectedFaceSubSet(connectedFaceSubSet);
        }
        if (entity instanceof StepGeometricCurveSet) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepGeometricSet) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepGeometricSurfaceSet surfaceSet) {
            return buildGeometricSurfaceSetShell(surfaceSet);
        }
        if (entity instanceof StepFaceBasedSurfaceModel faceModel) {
            return buildFaceBasedSurfaceModel(faceModel);
        }
        if (entity instanceof StepManifoldSurfaceModel manifoldModel) {
            return buildManifoldSurfaceModel(manifoldModel);
        }
        if (entity instanceof StepShellBasedSurfaceModel shellModel) {
            return buildShellBasedSurfaceModel(shellModel);
        }
        if (entity instanceof StepShellBasedWireframeModel wireframeModel) {
            return buildShellBasedWireframeModel(wireframeModel);
        }
        if (entity instanceof StepSurfacePatch surfacePatch) {
            return buildSurfacePatchShell(surfacePatch);
        }
        if (entity instanceof StepEdgeBasedWireframeModel) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepConnectedEdgeSet) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepEdgeWire) {
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepPlanarBox planarBox) {
            return buildPlanarBoxShell(planarBox);
        }
        if (entity instanceof StepPlanarExtent planarExtent) {
            return buildPlanarExtentShell(planarExtent);
        }
        if (entity instanceof StepPointSet) {
            throw new UnsupportedGeometryException("POINT_SET cannot be converted to a B-Rep shell");
        }
        if (entity instanceof StepFiniteElementMesh femMesh) {
            return builder.buildFiniteElementMeshShell(femMesh);
        }
        if (entity instanceof StepFlatPattern flatPattern) {
            if (flatPattern.flatGeometry() instanceof StepFaceEntity faceEntity) {
                return new Shell(List.of(builder.buildFace(faceEntity.id())), false);
            }
            if (flatPattern.flatGeometry() instanceof StepOpenShell
                    || flatPattern.flatGeometry() instanceof StepClosedShell) {
                return builder.buildShell(flatPattern.flatGeometry().id());
            }
            return new Shell(List.of(), false);
        }
        if (entity instanceof StepMappedItem mappedItem) {
            return builder.buildShell(mappedItem.mappingTarget().id());
        }
        if (entity instanceof StepSurfaceModel surfaceModel) {
            StepEntity actual = builder.resolvedEntity(surfaceModel.id());
            if (actual != null && actual != surfaceModel) {
                return builder.buildShell(actual.id());
            }
            throw new StepResolutionException(
                    "entity #" + id + " is an abstract SURFACE_MODEL with no concrete subtype"
            );
        }
        throw new StepResolutionException(
                "entity #" + id + " is not an OPEN_SHELL, SURFACED_OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL"
        );
    }

    private Shell buildFaceShell(List<? extends StepFaceEntity> faces, boolean closed) {
        return new Shell(faces.stream().map(face -> builder.buildFace(face.id())).toList(), closed);
    }

    private Shell buildConnectedFaceSet(StepConnectedFaceSet connectedFaces) {
        List<Face> faces = connectedFaces.faces().stream()
                .map(face -> builder.buildFace(face.id()))
                .toList();
        return new Shell(faces, !faces.isEmpty());
    }

    private Shell buildConnectedFaceSubSet(StepConnectedFaceSubSet connectedFaceSubSet) {
        List<Face> faces = connectedFaceSubSet.faces().stream()
                .map(face -> builder.buildFace(face.id()))
                .toList();
        return new Shell(faces, !faces.isEmpty());
    }

    private Shell buildGeometricSurfaceSetShell(StepGeometricSurfaceSet surfaceSet) {
        List<Face> faces = new ArrayList<>();
        for (StepEntity element : surfaceSet.elements()) {
            SurfaceGeometry surface = builder.buildSupportedFaceGeometry(element, "SURFACE");
            if (surface == null) {
                continue;
            }
            try {
                faces.add(new Face(surface, List.of(), true));
            } catch (Exception ignored) {
            }
        }
        return new Shell(faces, !faces.isEmpty());
    }

    private Shell buildFaceBasedSurfaceModel(StepFaceBasedSurfaceModel faceModel) {
        List<Face> faces = new ArrayList<>();
        for (StepEntity faceSet : faceModel.faceSets()) {
            if (faceSet instanceof StepConnectedFaceSet connectedFaces) {
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
        if (surface instanceof Plane plane) {
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
        if (planarBox.placement() instanceof StepAxis2Placement3D placement) {
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
        Vector3 yVec = normal.cross(xDir).scale(height);
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
