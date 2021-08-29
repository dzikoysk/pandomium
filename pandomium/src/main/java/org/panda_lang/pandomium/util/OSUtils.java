package org.panda_lang.pandomium.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author Osiris-Team
 */
public class OSUtils {
    public static OperatingSystemType OS_TYPE;
    public static OperatingSystemArchitectureType OS_ARCH;

    static {
        String actualOsArchitecture = System.getProperty("os.arch").toLowerCase();
        for (OSUtils.OperatingSystemArchitectureType type :
                OSUtils.OperatingSystemArchitectureType.values()) {
            if (actualOsArchitecture.equals(type.toString().toLowerCase())) // Not comparing the actual names because the enum has more stuff matching one name
                OS_ARCH = type;
        }
        if (OS_ARCH == null) {
            // Do another check.
            // On windows it can be harder to detect the right architecture that's why we do the stuff below:
            // Source: https://stackoverflow.com/questions/4748673/how-can-i-check-the-bitness-of-my-os-using-java-j2se-not-os-arch/5940770#5940770
            String arch = System.getenv("PROCESSOR_ARCHITECTURE");
            String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
            boolean is64 = arch != null && arch.endsWith("64")
                    || wow64Arch != null && wow64Arch.endsWith("64"); // Otherwise its 32bit
            if (is64)
                OS_ARCH = OSUtils.OperatingSystemArchitectureType.X64;
            else
                OS_ARCH = OSUtils.OperatingSystemArchitectureType.X32;
        }

        // Set the operating systems type
        String actualOsType = System.getProperty("os.name").toLowerCase();
        if (actualOsType.contains("alpine"))
            OS_TYPE = OSUtils.OperatingSystemType.ALPINE_LINUX;
        if (actualOsType.contains("win"))
            OS_TYPE = OSUtils.OperatingSystemType.WINDOWS;
        else if (actualOsType.contains("mac"))
            OS_TYPE = OSUtils.OperatingSystemType.MAC;
        else if (actualOsType.contains("aix"))
            OS_TYPE = OSUtils.OperatingSystemType.AIX;
        else if (actualOsType.contains("nix")
                || actualOsType.contains("nux"))
            OS_TYPE = OSUtils.OperatingSystemType.LINUX;
        else if (actualOsType.contains("sunos"))
            OS_TYPE = OSUtils.OperatingSystemType.SOLARIS;
        else
            OS_TYPE = OSUtils.OperatingSystemType.LINUX;
    }

    public static boolean isLinux() {
        return OS_TYPE.equals(OperatingSystemType.LINUX);
    }

    public static boolean isWindows() {
        return OS_TYPE.equals(OperatingSystemType.WINDOWS);
    }

    public static boolean isMac() {
        return OS_TYPE.equals(OperatingSystemType.MAC);
    }

    // ENUMS:


    public enum OperatingSystemArchitectureType {
        X64("x64", "64"),
        X86("x86", "86"),
        X32("x32", "32"),
        PPC64("ppc64"),
        PPC64LE("ppc64le"),
        S390X("s390x"),
        AARCH64("aarch64"),
        ARM("arm"),
        SPARCV9("sparcv9"),
        RISCV64("riscv64"),
        // x64 with alternative names:
        AMD64("x64", "64"),
        X86_64("x64", "64"),
        // x32 with alternative names:
        I386("x32", "32");

        private final List<String> aliases;

        OperatingSystemArchitectureType(String... aliases) {
            this.aliases = Arrays.asList(aliases);
        }

        public List<String> getAliases() {
            return aliases;
        }
    }

    public enum OperatingSystemType {
        LINUX("linux"),
        WINDOWS("windows", "win"),
        MAC("mac"),
        SOLARIS("solaris"),
        AIX("aix"),
        ALPINE_LINUX("alpine-linux");

        private final List<String> aliases;

        OperatingSystemType(String... aliases) {
            this.aliases = Arrays.asList(aliases);
        }

        public List<String> getAliases() {
            return aliases;
        }
    }

}
