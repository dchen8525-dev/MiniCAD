package com.minicad.app;

import com.minicad.helper.metadata.StepMetadataExtractor;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StepMetadataExtractorTest {

    @Test
    void shouldExtractRgbAndLayerMetadataForStyledFace() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#6=EDGE_LOOP('L0',());\n"
        + "#7=FACE_OUTER_BOUND('B0',#6,.T.);\n"
        + "#8=ADVANCED_FACE('FACE0',(#7),#5,.T.);\n"
        + "#20=COLOUR_RGB('Terracotta',0.8,0.4,0.2);\n"
        + "#21=FILL_AREA_STYLE_COLOUR('',#20);\n"
        + "#22=FILL_AREA_STYLE('',(#21));\n"
        + "#23=SURFACE_STYLE_FILL_AREA(#22);\n"
        + "#24=SURFACE_SIDE_STYLE('',(#23));\n"
        + "#25=SURFACE_STYLE_USAGE(.BOTH.,#24);\n"
        + "#26=PRESENTATION_STYLE_ASSIGNMENT((#25));\n"
        + "#27=STYLED_ITEM('FACE_STYLE',(#26),#8);\n"
        + "#28=PRESENTATION_LAYER_ASSIGNMENT('Inspection','Layer for QA',(#8));\n"
        + "ENDSEC;\n"
        ));

        StepMetadataExtractor extractor = StepMetadataExtractor.fromResolved(resolved);

        assertArrayEquals(new int[]{204, 102, 51}, extractor.forItem(8).rgb());
        assertEquals(1, extractor.forItem(8).layers().size());
        assertEquals("Inspection", extractor.forItem(8).layers().get(0));
    }
}
