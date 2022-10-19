package com.drlang.test;

import lsieun.utils.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

public class Test implements Opcodes {
    public static void main(String[] args) throws Throwable {

//        visit[visitSource][visitModule][visitNestHost][visitPermittedSubclass][visitOuterClass]
//        (visitAnnotation | visitTypeAnnotation | visitAttribute) *
//                (visitNestMember | visitInnerClass | visitRecordComponent | visitField | visitMethod)
//                                                                     * visitEnd.
//        test1();
//        test2();
//        test3();
        testFun("gen/HelloWorld.class",Test::dump2);
        print("gen/HelloWorld");
    }
    public static void print(String className) throws IOException {
        // (1) 设置参数
        int parsingOptions = ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG;
        boolean asmCode = true;


        // (2) 打印结果
        Printer printer = asmCode ? new ASMifier() : new Textifier();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
        new ClassReader(className).accept(traceClassVisitor, parsingOptions);
    }
    public static void test1() {
        String realPath = "example/Hello.class";
        String filePath = FileUtils.getFilePath(realPath);
        byte[] bytes = dump();
        FileUtils.writeBytes(filePath, bytes);
    }

    private static byte[] dump() {
        ClassWriter classWriter = new ClassWriter(COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | ACC_INTERFACE | ACC_ABSTRACT,
                "example/Hello", null, "java/lang/Object", null);
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    public static void test2() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("example.Hello");
        System.out.println(aClass);
    }

    public static void test3() {

        String realPath = "example/Hello.class";
        String filePath = FileUtils.getFilePath(realPath);
        byte[] bytes = dump1();
        FileUtils.writeBytes(filePath, bytes);
    }

    private static byte[] dump1() {
        ClassWriter classWriter = new ClassWriter(COMPUTE_FRAMES);
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_ABSTRACT | ACC_INTERFACE,
                "sample/HelloWorld", null, "java/lang/Object", new String[]{"java/lang/Cloneable"});
        classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "LESS", "I", null, -1).visitEnd();
        classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "EQUAL", "I", null, 0).visitEnd();
        classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "GREATER", "I", null, 1).visitEnd();
//        classWriter.visitMethod(ACC_PUBLIC|ACC_ABSTRACT,"compareTo",)
        classWriter.visitMethod(ACC_PUBLIC | ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();


        return classWriter.toByteArray();

    }
    public static byte[] dump2(){
        ClassWriter classWriter = new ClassWriter(COMPUTE_FRAMES);
        classWriter.visit(V1_8,ACC_PUBLIC +ACC_SUPER,"gan/HelloWorld",null,"java/lang/Object",null);
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
        return classWriter.toByteArray();
    }
    public static   <T> void testFun(String name, Supplier<byte[]> consumer){

        String filePath = FileUtils.getFilePath(name);
        byte[] t =  consumer.get();
        FileUtils.writeBytes(filePath, t);
    }

}
