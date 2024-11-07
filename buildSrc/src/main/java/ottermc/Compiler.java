package ottermc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import org.gradle.api.GradleScriptException;

public class Compiler {

    public static void compile(File file, int version) {
    	try {
    		if (!file.exists())
    			return;
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
    				bytes = remap(bytes, version);
    			target.putNextEntry(entry);
    			target.write(bytes, 0, bytes.length);
    			target.closeEntry();
    		}
    		jar.close();
    		target.close();
    	} catch (Exception e) {
			throw new GradleScriptException("client build failed during remap", e);
    	}
    }
    
    private static byte[] remap(byte[] bytes, int version) {
		ClassReader reader = new ClassReader(bytes);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		reader.accept(new TClassVisitor(writer, version), 0);
		return writer.toByteArray();
    }
}