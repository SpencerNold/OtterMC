package ottermc;

import org.gradle.api.GradleScriptException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapping {
	
	private static final Pattern PATTERN = Pattern.compile("\\s(\\S+?)(?=\\s|$)");

	private static final Map<Integer, Map<String, Class>> mappings = new HashMap<>();

	static {
		try {
			mappings.put(Constants.VERSION_1_8_9, read(Constants.VERSION_1_8_9));
			mappings.put(Constants.VERSION_1_21_4, read(Constants.VERSION_1_21_4));
		} catch (Exception e) {
			e.printStackTrace();
			throw new GradleScriptException("failed to read mapping", e);
		}
	}

	public static Class get(String className, int version) {
		return mappings.get(version).get(className);
	}
	
	public static boolean contains(String className, int version) {
		return mappings.get(version).containsKey(className);
	}
	
	private static Map<String, Class> read(int version) throws Exception {
		Map<String, Class> map = new HashMap<>();
		InputStream input = Mapping.class.getResourceAsStream(String.format("/mapping-%s.txt", VersionRegistry.NAMES.get(version)));
		if (input == null)
			return map;
		Scanner scanner = new Scanner(input);
		Class current = null;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			Matcher matcher = PATTERN.matcher(line);
			if (line.startsWith("CL:")) {
				if (current != null)
					map.put(current.name0, current);
				List<String> matches = new ArrayList<>();
				while (matcher.find())
					matches.add(matcher.group(1));
				current = new Class(matches.get(0), matches.get(1));
			} else if (line.startsWith("FD:")) {
				if (current == null) {
					scanner.close();
					throw new IOException("Field without class: " + line);
				}
				List<String> matches = new ArrayList<>();
				while (matcher.find())
					matches.add(matcher.group(1));
				Field field = new Field(matches.get(0), matches.get(1));
				current.fields.put(field.name0, field);
			} else if (line.startsWith("MD:")) {
				if (current == null) {
					scanner.close();
					throw new IOException("Method without class: " + line);
				}
				List<String> matches = new ArrayList<>();
				while (matcher.find())
					matches.add(matcher.group(1));
				Method method = new Method(matches.get(0), matches.get(1), matches.get(2), matches.get(3));
				current.methods.put(method.name0 + method.desc0, method);
			}
		}
		scanner.close();
		return map;
	}

	public static class Class {
		
		private final String name0, name1;
		private final Map<String, Field> fields;
		private final Map<String, Method> methods;
		
		public Class(String name0, String name1) {
			this.name0 = name0;
			this.name1 = name1;
			this.fields = new HashMap<>();
			this.methods = new HashMap<>();
		}
		
		public String getName0() {
			return name0;
		}
		
		public String getName1() {
			return name1;
		}
		
		public Field getField(String name) {
			return fields.get(name);
		}
		
		public boolean containsField(String name) {
			return fields.containsKey(name);
		}
		
		public Method getMethod(String name, String desc) {
			return methods.get(name + desc);
		}
		
		public boolean containsMethod(String name, String desc) {
			return methods.containsKey(name + desc);
		}
	}

	public static class Field {

		private final String name0, name1;

		public Field(String name0, String name1) {
			this.name0 = name0;
			this.name1 = name1;
		}

		public String getName0() {
			return name0;
		}

		public String getName1() {
			return name1;
		}
	}

	public static class Method {

		private final String name0, desc0;
		private final String name1, desc1;

		public Method(String name0, String desc0, String name1, String desc1) {
			this.name0 = name0;
			this.desc0 = desc0;
			this.name1 = name1;
			this.desc1 = desc1;
		}

		public String getName0() {
			return name0;
		}

		public String getDesc0() {
			return desc0;
		}

		public String getName1() {
			return name1;
		}

		public String getDesc1() {
			return desc1;
		}
	}
}
