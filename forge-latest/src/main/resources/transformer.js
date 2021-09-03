function initializeCoreMod() {
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");
    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");

    return {
        "Main.main transformer": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.main.Main",
                "methodName": "main",
                "methodDesc": "([Ljava/lang/String;)V"
            },
            "transformer": transformMain
        }
    }
}

function transformMain(method) {
    list = ASMAPI.listOf(
        new VarInsnNode(Opcodes.ALOAD, 0),
        ASMAPI.buildMethodCall(
            "me/djtheredstoner/devauth/forge/latest/ForgeLatestBootstrap",
            "processArguments",
            "([Ljava/lang/String;)[Ljava/lang/String;",
            ASMAPI.MethodType.STATIC
        ),
        new VarInsnNode(Opcodes.ASTORE, 0)
    )

    method.instructions.insert(list);

    return method;
}