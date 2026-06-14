# AP242 Edition 2 + CC2 + Semantic PMI 实体清单

> 用途：STEP AP242 文件逐项检查（工程实用子集）  
> 覆盖：Geometry / Assembly / PMI / GD&T / E2 扩展  
> 规模：约 400+ 核心实体

# 1. Geometry（几何与拓扑）

## 1.1 基础几何
cartesian_point
direction
vector
axis1_placement
axis2_placement_2d
axis2_placement_3d

## 1.2 曲线
line
circle
ellipse
parabola
hyperbola
b_spline_curve
b_spline_curve_with_knots
bezier_curve
trimmed_curve
offset_curve_3d

## 1.3 曲面
plane
cylindrical_surface
conical_surface
spherical_surface
toroidal_surface
b_spline_surface
b_spline_surface_with_knots
bezier_surface
offset_surface

## 1.4 拓扑
vertex_point
edge_curve
oriented_edge
edge_loop
face_outer_bound
face_bound
advanced_face
closed_shell
open_shell

## 1.5 实体
manifold_solid_brep
brep_with_voids
faceted_brep
advanced_brep_shape_representation

# 2. Representation（表示关系）
representation
shape_representation
advanced_brep_shape_representation
representation_item
representation_relationship
shape_representation_relationship
context_dependent_shape_representation

# 3. Product Structure（产品结构）
product
product_definition
product_definition_formation
product_definition_formation_with_specified_source
product_definition_context
product_definition_relationship
product_definition_shape
shape_definition_representation
next_assembly_usage_occurrence
assembly_component_usage

# 4. Assembly / Instance（装配实例）
context_dependent_shape_representation
item_defined_transformation
mapped_item
representation_map

# 5. External Reference（外部引用）
external_source
external_identification_item
externally_defined_item
document
document_reference

# 6. PMI – 尺寸（Semantic）
dimensional_size
dimensional_location
size_dimension
location_dimension
dimensional_characteristic_representation

# 7. PMI – 公差
plus_minus_tolerance
limits_and_fits
tolerance_value
tolerance_zone
tolerance_zone_definition

# 8. PMI – GD&T（核心）
geometric_tolerance
geometric_tolerance_with_datums
position_tolerance
profile_tolerance
surface_profile_tolerance
flatness_tolerance
straightness_tolerance
roundness_tolerance
cylindricity_tolerance
parallelism_tolerance
perpendicularity_tolerance
angularity_tolerance
runout_tolerance
total_runout_tolerance
concentricity_tolerance
coaxiality_tolerance
symmetry_tolerance

# 9. PMI – Datum（基准）
datum
datum_feature
datum_feature_callout
datum_reference
datum_reference_compartment
datum_system
datum_target

# 10. PMI – Annotation（显示）
annotation_occurrence
annotation_curve_occurrence
annotation_fill_area_occurrence
draughting_callout
leader_curve
leader_directed_callout
text_literal
text_style
text_style_for_defined_font
annotation_plane
annotation_text_occurrence

# 11. Presentation（显示样式）
styled_item
presentation_style_assignment
surface_style_usage
surface_style_rendering
colour_rgb
curve_style

# 12. Validation Properties（验证属性）
measure_representation_item
length_measure
area_measure
volume_measure
mass_measure
centroid
moment_of_inertia

# 13. Shape Quality（几何质量）
shape_data_quality
shape_tolerance

# 14. Kinematics（运动学）
kinematic_pair
revolute_pair
prismatic_pair
spherical_pair
planar_pair
kinematic_link
kinematic_topology_structure

# 15. Hole Features（Edition 2）
round_hole
explicit_round_hole
counterbore_hole
countersunk_hole
tapped_hole
patterned_hole

# 16. Tessellation（轻量化）
tessellated_shape_representation
tessellated_item
tessellated_face_set
triangulated_face
complex_triangulated_face

# 17. Point Cloud（Edition 2）
point_cloud
point_cloud_dataset
point_set

# 18. EWIS（电气线束）
wire
cable
wire_harness
electrical_node
connector
routing_path

# 19. Composite（复合材料）
ply
laminate
stackup
fiber_orientation
composite_shape_representation

# 20. Additive Manufacturing（增材制造）
build_orientation
build_plate
support_structure
part_placement
am_setup

# 21. Change / Configuration（变更管理）
change
change_request
change_action
approval
configuration_item
