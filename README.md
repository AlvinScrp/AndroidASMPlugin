# AndroidASMPlugin
 Android项目的一些字节码插桩插件,基于AsmClassVisitorFactory(AGP7.0+)
 
 核心即通过ASM插桩，实现方法插入和替换。
 
 ## privacyLogPlugin
 支持在隐私API访问前，插入日志代码，编译时代码扫描+运行时访问日志

 ## privacyHookPlugin
 APP隐私API Hook,运行时访问自定义代码
 
 ## autoTrackPlugin
 支持在指定方法前，插入埋点代码
 支持Click事件Hook，运行时访问自定义代码
