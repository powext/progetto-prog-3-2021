package it.tweetmail.lib.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public final class Serializer {
    public static boolean writeToFile(File file, Serializable serializable) {
        return writeToFile(file.toPath(), serializable);
    }

    public static boolean writeToFile(Path path, Serializable serializable) {
        try (FileChannel writeChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock ignored = writeChannel.lock(0L, Long.MAX_VALUE, false)) {

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                    objectOutputStream.writeObject(serializable);
                    objectOutputStream.flush();
                }

                byte[] objectBytes = byteArrayOutputStream.toByteArray();
                ByteBuffer buffer = ByteBuffer.wrap(objectBytes);
                writeChannel.write(buffer);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static <T extends Serializable> T readFromFile(File file) {
        return readFromFile(file.toPath());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T readFromFile(Path path) {
        Object objectClass = null;
        try (FileChannel readChannel = FileChannel.open(path, StandardOpenOption.READ);
            FileLock ignored = readChannel.lock(0L, Long.MAX_VALUE, true)) {

            byte[] bytes = new byte[5120]; // chunk of 5KB
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                int bytesRead;
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                while ((bytesRead = readChannel.read(buffer)) != -1) {
                    buffer.flip();
                    buffer.get(bytes, 0, bytesRead);
                    buffer.clear();
                    byteArrayOutputStream.write(bytes, 0, bytesRead);
                }

                byte[] objectBytes = byteArrayOutputStream.toByteArray();
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectBytes);
                     ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                    objectClass = objectInputStream.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return (T) objectClass;
    }

    public static String writeToB64(Serializable serializable) {
        String base64Class = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(serializable);
            base64Class = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Class;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T readFromB64(String base64) {
        Object objectClass = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            try (ObjectInputStream objStreamIn = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                objectClass = objStreamIn.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) objectClass;
    }
}
