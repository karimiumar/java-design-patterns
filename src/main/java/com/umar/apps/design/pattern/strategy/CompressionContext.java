package com.umar.apps.design.pattern.strategy;

import java.io.File;
import java.util.List;

public class CompressionContext {
    private final CompressionStrategy strategy;

    public CompressionContext(CompressionStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void createArchive(List<File> files) {
        strategy.compress(files);
    }
    
    public String archivingStrategyUsed() {
        return strategy.strategy();
    }
}
