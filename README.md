# Pandomium [![Build Status](https://travis-ci.org/Panda-Programming-Language/Pandomium.svg?branch=master)](https://travis-ci.org/Panda-Programming-Language/Pandomium)
Pandomium is the JCEF (Java Chromium Embedded Framework) implementation dedicated for the maven projects 

#### Example
![PandomiumTest.java](https://panda-lang.org/screenshot/5d8KeBJg.png)
```java
public class PandomiumTest {

    public static void main(String[] args) {
        PandomiumSettings settings = PandomiumSettings.getDefaultSettings();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumClient client = pandomium.createClient();
        PandomiumBrowser browser = client.loadURL("https://panda-lang.org");

        JFrame frame = new JFrame();
        frame.getContentPane().add(browser.toAWTComponent(), BorderLayout.CENTER);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pandomium.dispose();
                frame.dispose();
            }
        });

        pandomium.getRaw().getPandomiumThread().getApp().addShutdownHookAction(() -> {
            if (!frame.isDisplayable()) {
                return;
            }

            pandomium.dispose();
            frame.dispose();
        });

        frame.setTitle("Pandomium");
        frame.setSize(1380, 760);
        frame.setVisible(true);
    }

}
```

#### Requirements
* Windows x64 (only)
* Java 8

#### Maven
```xml
<dependency>
    <groupId>org.panda_lang</groupId>
    <artifactId>pandomium</artifactId>
    <version>1.0.3</version>
</dependency>

<repository>
    <id>panda-repo</id>
    <url>https://repo.panda-lang.org/</url>
</repository>
```

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

#### TODO
* [x] Build natives & jcef
* [ ] Win32, Linux32 & macOS support
* [x] Basic implementation
* [x] Builds available in the maven repository
* [ ] Pandomium wrapper for JCEF objects
* [ ] Advanced Java <-> JS bridge
* [ ] ByteBuffer implementation
