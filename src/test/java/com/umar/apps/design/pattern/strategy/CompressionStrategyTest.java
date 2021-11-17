package com.umar.apps.design.pattern.strategy;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class CompressionStrategyTest {
    
    @Test
    void when_ZipCompressionStrategy_then_files_compressed_using_ZipCompression() {
        var compressionStrategy = new ZipCompressionStrategy();
        var context = new CompressionContext(compressionStrategy);
        context.createArchive(List.of());
        var strategy = context.archivingStrategyUsed();
        assertThat(strategy).isEqualTo("ZipCompression");
    }
    
    @Test
    void when_RarCompressionStrategy_then_files_compressed_using_RarCompression() {
        var compressionStrategy = new RarCompressionStrategy();
        var context = new CompressionContext(compressionStrategy);
        context.createArchive(List.of());
        var strategy = context.archivingStrategyUsed();
        assertThat(strategy).isEqualTo("RarCompression");
    }
}
