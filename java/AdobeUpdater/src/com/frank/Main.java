package com.frank;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static int currentTime = (int) ( System.currentTimeMillis() / 1000 );

    static class PathWithTime {
        Path filePath;
        int fileCreationDate;
    };

    static Set<String> seenFiles = new HashSet<>();

    static String newFolder = "C:/Apache24/htdocs/staging";

    public static void doLoop() throws Exception {
        var tmpdir = Files.list(Paths.get(System.getProperty("java.io.tmpdir")));
        List<PathWithTime> dirs_in_tmp;

        try (Stream<Path> stream = tmpdir) {
            dirs_in_tmp = stream
                    .filter(Files::isDirectory)
                    .map(path -> {
                        PathWithTime out = new PathWithTime();
                        BasicFileAttributes attributes = null;
                        try {
                            attributes =
                                    Files.readAttributes(path, BasicFileAttributes.class);
                        } catch (IOException e) {
                            return out;
                        }
                        out.fileCreationDate = (int)attributes.creationTime().to(TimeUnit.SECONDS);
                        out.filePath = path;
                        return out;
                    })
                    .filter(pathWithTime -> {
                        return pathWithTime.fileCreationDate > currentTime;
                    })
                    .collect(Collectors.toList());
        }

        List<Path> zips = new ArrayList<>();
        for (PathWithTime folderWithTime: dirs_in_tmp) {
            // Does not work, need to skip over no-priviliges files
            try (Stream<Path> stream = Files.list(folderWithTime.filePath)) {
                zips.addAll(stream.filter(file -> !Files.isDirectory(file))
                        .filter(path -> path.getFileName().toString().endsWith(".zip.aamdownload"))
                        .collect(Collectors.toList()));
            } catch (Exception e) {

            }
        }

        for (Path zipfile: zips) {
            if (seenFiles.contains(zipfile.getFileName().toString())) {
                continue;
            }
            seenFiles.add(
                    zipfile.getFileName().toString()
            );
            System.out.println("Added " + zipfile.getFileName().toString() + " to seen & cache.");
            Path newLink = Path.of(newFolder, zipfile.getFileName().toString());
            try {
                Files.createLink(newLink, zipfile);
            } catch (FileAlreadyExistsException e) {
                System.err.println( zipfile.getFileName().toString() + " already exists, skipping.");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        while (true) {
            doLoop();
            Thread.sleep(1200);
        }
    }
}
