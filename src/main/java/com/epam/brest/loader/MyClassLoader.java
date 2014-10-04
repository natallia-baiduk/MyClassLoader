package com.epam.brest.loader;

import org.apache.log4j.*;

import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Custom class loader for loading custom implementation.
 */
public class MyClassLoader extends ClassLoader {
    static final Logger LOG = Logger.getLogger(MyClassLoader.class);

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public MyClassLoader() {
        this(MyClassLoader.class.getClassLoader());
    }

    /**
     * Loads path to jar from properties
     *
     * @return path string
     */
    protected String getPathToJar() {
        Properties prop = new Properties();
        InputStream input = null;
        String pathToJar = new String();

        try {
            prop.load(MyClassLoader.class.getResourceAsStream("loader-properties.properties"));
            pathToJar = prop.getProperty("PATH_TO_JAR");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return pathToJar;
    }

    /**
     * Finds the class with the specified name.
     * This method follows the delegation model for loading classes, and will be invoked after checking
     * the parent class loader for the requested class.
     *
     * @param name class name
     * @return  class
     * @throws ClassNotFoundException
     */
    @Override
    protected Class findClass(final String name) throws ClassNotFoundException {
        Class result = findLoadedClass(name);

        if (result != null) {
            LOG.info(">> Class " + name + "found in cash.");

            return result;
        }

        File file = findJar();

        LOG.info(">> Jar file " + file == null ? "" : " found in " + file);

        if (file == null) {
            return findSystemClass(name);
        }

        try {
            byte[] classBytes = getFile(file, name.replace('.', File.separatorChar) + ".class");
            result = defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException ex) {
            throw new ClassNotFoundException("Cannot load class " + name + ": " + ex);
        } catch (ClassFormatError er) {
            throw new ClassNotFoundException("Format of class file is incorrect for class " + name + ": " + er);
        }

        return result;
    }

    /**
     * Finds a jar file in a file system.
     *
     * @return file
     */
    private File findJar() {
        String pathToJar = getPathToJar();
        File file = new File(pathToJar);

        if (file.exists()) {
            return file;
        }

        return null;
    }

    /**
     * Loads a class file as bytes.
     *
     * @param jar jar file
     * @param requestedFile name of requested class
     * @return array of bytes
     * @throws IOException
     */
    private byte[] getFile(File jar, String requestedFile) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipInputStream in = null;
        try {
            in = new ZipInputStream(new FileInputStream(jar));
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                if (entry.getName().equals(requestedFile)) {

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            out.close();

        }

        return out.toByteArray().clone();
    }

}
