package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepToroidalSurfaceWithSpecifiedBends;

import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Builds analytic primitive surface geometry (plane, cylinder, cone, torus)
 * from resolved STEP entities. Free-form, swept and composite surface families
 * live in {@link StepCadBuilder} itself.
 */
final class StepCadSurfaceBuilder {

    // Entity lookup
    private final Map<Integer, StepEntity> entitiesById;

    // Surface caches
    private final Map<Integer, Plane> planes;
    private final Map<Integer, CylindricalSurface> cylindricalSurfaces;
    private final Map<Integer, ConicalSurface> conicalSurfaces;
    private final Map<Integer, ToroidalSurface> toroidalSurfaces;

    // Callback for placements
    private final IntFunction<Axis2Placement3D> buildPlacementCallback;

    StepCadSurfaceBuilder(
            Map<Integer, StepEntity> entitiesById,
            Map<Integer, Plane> planes,
            Map<Integer, CylindricalSurface> cylindricalSurfaces,
            Map<Integer, ConicalSurface> conicalSurfaces,
            Map<Integer, ToroidalSurface> toroidalSurfaces,
            IntFunction<Axis2Placement3D> buildPlacementCallback
    ) {
        this.entitiesById = entitiesById;
        this.planes = planes;
        this.cylindricalSurfaces = cylindricalSurfaces;
        this.conicalSurfaces = conicalSurfaces;
        this.toroidalSurfaces = toroidalSurfaces;
        this.buildPlacementCallback = buildPlacementCallback;
    }

    /**
     * Builds a Plane from a STEP PLANE entity.
     */
    Plane buildPlane(int id) {
        Plane existing = planes.get(id);
        if (existing != null) {
            return existing;
        }
        StepPlane plane = requireEntity(id, StepPlane.class, "PLANE");
        Axis2Placement3D placement = buildPlacementCallback.apply(plane.getPosition().id());
        Plane built = new Plane(placement.getLocation(), placement.getAxis());
        planes.put(id, built);
        return built;
    }

    /**
     * Builds a CylindricalSurface from a STEP CYLINDRICAL_SURFACE entity.
     */
    CylindricalSurface buildCylindricalSurface(int id) {
        CylindricalSurface existing = cylindricalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepCylindricalSurface surface = requireEntity(id, StepCylindricalSurface.class, "CYLINDRICAL_SURFACE");
        CylindricalSurface built = new CylindricalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getRadius());
        cylindricalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ConicalSurface from a STEP CONICAL_SURFACE entity.
     */
    ConicalSurface buildConicalSurface(int id) {
        ConicalSurface existing = conicalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicalSurface surface = requireEntity(id, StepConicalSurface.class, "CONICAL_SURFACE");
        ConicalSurface built = new ConicalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getRadius(), surface.getSemiAngle());
        conicalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ToroidalSurface from a STEP TOROIDAL_SURFACE entity.
     */
    ToroidalSurface buildToroidalSurface(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurface surface = requireEntity(id, StepToroidalSurface.class, "TOROIDAL_SURFACE");
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a degenerate toroidal surface geometry object.
     */
    ToroidalSurface buildDegenerateToroidalSurface(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepDegenerateToroidalSurface surface = requireEntity(id, StepDegenerateToroidalSurface.class, "DEGENERATE_TOROIDAL_SURFACE");
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a toroidal surface from TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
     */
    ToroidalSurface buildToroidalSurfaceFromSpecifiedBends(StepToroidalSurfaceWithSpecifiedBends surface) {
        ToroidalSurface existing = toroidalSurfaces.get(surface.id());
        if (existing != null) {
            return existing;
        }
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(surface.id(), built);
        return built;
    }

    private StepEntity requireExistingEntity(int id) {
        StepEntity entity = entitiesById.get(id);
        if (entity == null) {
            throw new StepResolutionException("missing resolved entity #" + id);
        }
        return entity;
    }

    private <T extends StepEntity> T requireEntity(int id, Class<T> type, String expectedName) {
        StepEntity entity = requireExistingEntity(id);
        if (!type.isInstance(entity)) {
            throw new StepResolutionException("entity #" + id + " is not a " + expectedName);
        }
        return type.cast(entity);
    }
}
