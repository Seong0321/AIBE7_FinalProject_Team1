package org.example.springtestci.common.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LocalFileStorageTest {

  @TempDir Path tempDirectory;

  @Test
  void storesLoadsAndDeletesAFile() throws Exception {
    LocalFileStorage storage = new LocalFileStorage(tempDirectory.toString());

    String key =
        storage.store(
            "sample file.txt",
            new ByteArrayInputStream("content".getBytes(StandardCharsets.UTF_8)));

    assertTrue(key.endsWith("sample_file.txt"));
    assertEquals("content", Files.readString(storage.load(key).getFile().toPath()));

    storage.delete(key);

    assertFalse(storage.load(key).exists());
  }

  @Test
  void rejectsAPathOutsideTheStorageRoot() {
    LocalFileStorage storage = new LocalFileStorage(tempDirectory.toString());

    assertThrows(IllegalArgumentException.class, () -> storage.load("../outside.txt"));
  }
}
