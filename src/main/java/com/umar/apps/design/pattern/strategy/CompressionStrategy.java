package com.umar.apps.design.pattern.strategy;

import java.io.File;
import java.util.List;

public interface CompressionStrategy {
    void compress(List<File> files);
    String strategy();
}
