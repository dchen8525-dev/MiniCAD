package com.minicad.app;

import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StepMetadataExtractorTest {

    @Test
    void shouldExtractRgbAndLayerMetadataForStyledFace() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #20=COLOUR_RGB('Terracotta',0.8,0.4,0.2);
                #21=FILL_AREA_STYLE_COLOUR('',#20);
                #22=FILL_AREA_STYLE('',(#21));
                #23=SURFACE_STYLE_FILL_AREA(#22);
                #24=SURFACE_SIDE_STYLE('',(#23));
                #25=SURFACE_STYLE_USAGE(.BOTH.,#24);
                #26=PRESENTATION_STYLE_ASSIGNMENT((#25));
                #27=STYLED_ITEM('FACE_STYLE',(#26),#8);
                #28=PRESENTATION_LAYER_ASSIGNMENT('Inspection','Layer for QA',(#8));
                ENDSEC;
                """));

        StepMetadataExtractor extractor = StepMetadataExtractor.fromResolved(resolved);

        assertArrayEquals(new int[]{204, 102, 51}, extractor.forItem(8).rgb());
        assertEquals(1, extractor.forItem(8).layers().size());
        assertEquals("Inspection", extractor.forItem(8).layers().getFirst());
    }
}
