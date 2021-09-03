package me.djtheredstoner.devauth.forge.legacy;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!"net.minecraft.client.main.Main".equals(name)) return basicClass;

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("main")) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    "me/djtheredstoner/devauth/forge/legacy/ForgeLegacyBootstrap",
                    "processArguments",
                    "([Ljava/lang/String;)[Ljava/lang/String;",
                    false
                ));
                list.add(new VarInsnNode(Opcodes.ASTORE, 0));
                method.instructions.insert(list);
                break;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);

        return writer.toByteArray();
    }
}
