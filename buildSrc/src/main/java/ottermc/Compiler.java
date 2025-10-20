package ottermc;

import org.gradle.api.GradleScriptException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import ottermc.asm.Hierarchy;
import ottermc.asm.TClassVisitor;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Compiler {

    public static File compile(File file, File[] dependencies, int version, File minecraft) {
    	try {
			if (!minecraft.exists())
				return null;
			Hierarchy.create(minecraft, false);
			Hierarchy.create(file, true);
			for (File f : dependencies)
				Hierarchy.create(f, true);
			Hierarchy.finish();
    		if (!file.exists())
    			return null;
    		File output = new File(file.getParentFile(), file.getName().replace(".jar", "-remapped.jar"));
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
			return output;
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

	public static void copy(File src, File dst) throws IOException {
		if (!dst.exists() && !dst.createNewFile())
			throw new IOException("failed to create: " + dst.getAbsolutePath());
		FileInputStream input = new FileInputStream(src);
		FileOutputStream output = new FileOutputStream(dst);
		byte[] buffer = new byte[1024];
		int read;
		while ((read = input.read(buffer)) != -1)
			output.write(buffer, 0, read);
		input.close();
		output.close();
	}
}