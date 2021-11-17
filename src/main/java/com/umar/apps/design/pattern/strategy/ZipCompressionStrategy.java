package com.umar.apps.design.pattern.strategy;

import java.io.File;
import java.util.List;

public class ZipCompressionStrategy implements CompressionStrategy{

    @Override
    public void compress(List<File> files) {
        files.stream().forEach(f -> System.out.println("Compressing..."));
    }

    @Override
    public String strategy() {
        return "ZipCompression";
    }
}
