package agent.objects;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import agent.transformation.Target;
import structures.Pair;

public class TMethodObj {

	private final String name, desc;
	private final Method method;
	private final Target target;
	
	public TMethodObj(String name, String desc, Method method, Target target) {
		this.name = name;
		this.desc = desc;
		this.method = method;
		this.target = target;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescriptor() {
		return desc;
	}
	
	public String getJoined() {
		return name + desc;
	}
	
	public String getTransformerName() {
		return method.getName();
	}
	
	public String getTransformerDescriptor() {
		return Type.getMethodDescriptor(method);
	}
	
	public Target getTarget() {
		return target;
	}
	
	public int getLocalSize() {
		Pattern pattern = Pattern.compile("(Z|B|S|I|J|F|D|L[^(L|;)]+?;)"); // (B|S|I|J|F|D|L[^(L|;)]+?;)
		Matcher matcher = pattern.matcher(desc);
		int size = 1;
		while (matcher.find()) {
			String s = matcher.group(1);
			if (s.equals("D") || s.equals("J"))
				size += 2;
			else
				size++;
		}
		return size;
	}
	
	public int getReturnType() {
		if (desc.endsWith("V"))
			return Opcodes.RETURN;
		else if (desc.endsWith(";"))
			return Opcodes.ARETURN;
		else if (desc.endsWith("B") || desc.endsWith("S") || desc.endsWith("I") || desc.endsWith("Z"))
			return Opcodes.IRETURN;
		else if (desc.endsWith("J"))
			return Opcodes.LRETURN;
		else if (desc.endsWith("F"))
			return Opcodes.FRETURN;
		else if (desc.endsWith("D"))
			return Opcodes.DRETURN;
		return -1;
	}
	
	public List<Pair<Integer, Integer>> getLoadParameterInstructions() {
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		int index = 1;
		int count = method.getParameterCount() - 1;
		for (int i = 1; i < count; i++) {
			Parameter param = method.getParameters()[i];
			Class<?> type = param.getType();
			int insn = Opcodes.ALOAD;
			if (type == byte.class || type == short.class || type == int.class || type == boolean.class)
				insn = Opcodes.ILOAD;
			if (type == long.class)
				insn = Opcodes.LLOAD;
			if (type == float.class)
				insn = Opcodes.FLOAD;
			if (type == double.class)
				insn = Opcodes.DLOAD;
			list.add(new Pair<>(insn, index));
			index += (type == double.class || type == long.class) ? 2 : 1;
		}
		return list;
	}
	
	public static TMethodObj getFromJoined(String name, Method method, Target target) {
		Pattern pattern = Pattern.compile("(.*)(\\(.*\\).*$)");
		Matcher matcher = pattern.matcher(name);
		if (matcher.matches()) {
			name = matcher.group(1);
			String desc = matcher.group(2);
			return new TMethodObj(name, desc, method, target);
		}
		return null;
	}
}
