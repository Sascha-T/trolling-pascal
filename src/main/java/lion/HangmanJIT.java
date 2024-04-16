package lion;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;


public interface HangmanJIT {
    boolean process(char[] guessReference, char letter);

    // pascal i hope you hate me for this :D
    static HangmanJIT compile(char[] secret) {
        Map<Character, Integer> pos = new HashMap<>();
        for (int i = secret.length - 1; i >= 0; i--)
            pos.put(secret[i], i);

        ClassNode test = new ClassNode();
        test.name = "warrghgh";
        test.superName = "java/lang/Object";
        test.interfaces = List.of("lion/HangmanJIT");
        test.version = 60;
        test.access = Opcodes.ACC_PUBLIC;

        MethodNode cfun = new MethodNode();
        InsnList clist = new InsnList();
        cfun.name = "<init>";
        cfun.access = Opcodes.ACC_PUBLIC;
        cfun.desc = "()V";
        cfun.instructions = clist;
        clist.add(new VarInsnNode(Opcodes.ALOAD, 0));
        clist.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
        clist.add(new InsnNode(Opcodes.RETURN));

        MethodNode fun = new MethodNode();
        InsnList list = new InsnList();
        fun.instructions = list;
        test.methods = List.of(fun, cfun);

        fun.name = "process";
        fun.desc = "([CC)Z";
        fun.access = Opcodes.ACC_PUBLIC;

        LabelNode DIRECT_INVALID = new LabelNode();
        LabelNode END = new LabelNode();

        for (Character c : pos.keySet()) {
            LabelNode CONTINUE = new LabelNode();
            list.add(new VarInsnNode(Opcodes.ILOAD, 2));
            list.add(new LdcInsnNode(c));
            list.add(new JumpInsnNode(Opcodes.IF_ICMPNE, CONTINUE));
            list.add(new VarInsnNode(Opcodes.ALOAD, 1));
            list.add(new LdcInsnNode(pos.get(c)));
            list.add(new InsnNode(Opcodes.CALOAD));
            list.add(new LdcInsnNode(c));
            list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, DIRECT_INVALID));

            for (int i = 0; i < secret.length; i++) {
                if(secret[i] == c) {
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new LdcInsnNode(i));
                    list.add(new LdcInsnNode(c));
                    list.add(new InsnNode(Opcodes.CASTORE));
                }
            }

            list.add(new JumpInsnNode(Opcodes.GOTO, END));// */
            list.add(CONTINUE);
        }
        list.add(new JumpInsnNode(Opcodes.GOTO, DIRECT_INVALID));


        list.add(END);
        list.add(new LdcInsnNode(false));
        list.add(new InsnNode(Opcodes.IRETURN));


        list.add(DIRECT_INVALID);
        list.add(new LdcInsnNode(true));
        list.add(new InsnNode(Opcodes.IRETURN));

        TheLoader loader = new TheLoader();
        try {
            return (HangmanJIT) loader.load(test).newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static class TheLoader extends ClassLoader {
        public Class<?> load(ClassNode node) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
            node.accept(writer);
            byte[] a = writer.toByteArray();
            Class<?> wa = defineClass(node.name, a, 0, a.length);
            resolveClass(wa);
            return wa;
        }
    }
}
