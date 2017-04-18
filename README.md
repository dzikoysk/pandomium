# Pandomium
Pandomium is the JCEF (Java Chromium Embedded Framework) implementation dedicated for the maven projects 

#### Repository structure
```
pandomium/
+--pandomium/                 Pandomium Library module
   +----/src                  Sources of Pandomium Library module
   +----pom.xml               The main maven build script for Pandomium Library module
+--pandomium-linux/           Linux implementation of Pandomium Library module
   +----/libs                 Platform specified libraries
   +----/native               Platform specified natives
   +----/src                  Platform implementation
   +----pom.xml               The main maven build script for Linux module
+--pandomium-macos/           MacOS implementation of Pandomium Library module
   +----/libs                 Platform specified libraries
   +----/native               Platform specified natives
   +----/src                  Platform implementation
   +----pom.xml               The main maven build script for macOS module
+--pandomium-repo/            Clone of the remote repository used by Pandomium
+--pandomium-resources/       Clone of the repositories used to build Pandomium
   +----/jcebb                Modifed sources of JCEF cloned from JavaChromiumEmbeddedByteBuffer repository
   +----/jcef                 Clone of the JCEF repository used to build natvies 
+--pandomium-win/             Windows implementation of Pandomium Library module
   +----/libs                 Platform specified libraries
   +----/native               Platform specified natives
   +----/src                  Platform implementation
   +----pom.xml               The main maven build script for Windows module
+--pom.xml                    The main maven build script
```

#### Maven
Soon...

```xml
<dependency>
    <groupId>org.panda_lang</groupId>
    <artifactId>pandomium</artifactId>
    <version>indev-0.1.0-SNAPSHOT</version>
</dependency>
```
