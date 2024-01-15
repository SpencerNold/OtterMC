package remap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Compile {

    public static boolean execute(File file) {
    	try {
    		if (!file.exists())
    			return false;
    		File output = new File(file.getParentFile(), "OtterMC-remapped.jar");
    		if (output.exists())
    			output.delete();
    		JarOutputStream target = new JarOutputStream(new FileOutputStream(output));
    		
    		JarFile jar = new JarFile(file);
    		Enumeration<JarEntry> entries = jar.entries();
    		while (entries.hasMoreElements()) {
    			JarEntry entry = entries.nextElement();
    			if (entry.isDirectory()) {
    				target.putNextEntry(entry);
    				target.closeEntry();
    				continue;
    			}
    			InputStream input = jar.getInputStream(entry);
    			byte[] bytes = input.readAllBytes();
    			input.close();
    			if (entry.getName().endsWith(".class"))
    				bytes = remap(bytes);
    			target.putNextEntry(entry);
    			target.write(bytes, 0, bytes.length);
    			target.closeEntry();
    		}
    		jar.close();
    		target.close();
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    private static byte[] remap(byte[] bytes) {
		ClassReader reader = new ClassReader(bytes);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		reader.accept(new TClassVisitor(writer), 0);
		return writer.toByteArray();
    }
}