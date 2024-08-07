package top.imyzt.javaagent.bytecode.asm9;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2024/08/06
 * @description 描述信息
 */
public class Transformer {

    static class MyClassVisitor extends ClassVisitor {

        public MyClassVisitor(int api, ClassWriter cw) {
            super(api, cw);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            // 查找方法，名称为 incr， (I)I 表示参数类型为一个int,返回类型也为int
            if (name.equals("incr") && descriptor.equals("(I)I")) {
                return new MyMethodVisitor(Opcodes.ASM9, mv);
            } else {
                return mv;
            }
        }
    }

    static class MyMethodVisitor extends MethodVisitor {
        public MyMethodVisitor(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }
        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ICONST_1) {
                // 如果指定是ICONST_1，则修改为 ICONST_2
                super.visitInsn(Opcodes.ICONST_2);
            } else {
                super.visitInsn(opcode);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String file = "/Users/imyzt/dev/ideaWorkspace/learning-technology-code/framework-in-java/java-agent-demo/bytecode/src/main/java/top/imyzt/javaagent/bytecode/Demo.class";
        ClassReader cr = new ClassReader(new FileInputStream(file));
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM9, cw);
        cr.accept(myClassVisitor, 0);

        byte[] byteArray = cw.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(byteArray);
        fos.close();
    }
}
