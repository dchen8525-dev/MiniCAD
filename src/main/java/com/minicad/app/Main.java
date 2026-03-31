package com.minicad.app;

import com.alibaba.fastjson2.JSON;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

  public static void main(String... args) throws Exception {
    String content = Files.readString(Path.of("D:\\work\\MiniCAD\\examples\\bspline-patch.step"));
    StepFile stepFile = StepParser.parse(content);
    System.out.println(JSON.toJSONString(stepFile));
  }
}
