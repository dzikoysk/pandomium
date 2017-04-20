# Pandomium
Pandomium is the JCEF (Java Chromium Embedded Framework) implementation dedicated for the maven projects 

#### Example
```java
public class PandomiumTest {

    public static void main(String[] args) {
        PandomiumSettings settings = PandomiumSettings.getDefaultSettings();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumClient client = pandomium.createClient();
        PandomiumBrowser browser = client.createBrowser("https://panda-lang.org");

        JFrame frame = new JFrame();
        frame.getContentPane().add(browser.toAWTComponent(), BorderLayout.CENTER);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                pandomium.exit();
            }
        });

        frame.setTitle("Pandomium");
        frame.setSize(1380, 760);
        frame.setVisible(true);
    }

}
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

#### Maven
```xml
<dependency>
    <groupId>org.panda_lang</groupId>
    <artifactId>pandomium</artifactId>
    <version>indev-0.1.0-SNAPSHOT</version>
</dependency>
```

#### TODO
* [x] Build natives & jcef
* [ ] Win32, Linux32 & macOS support
* [x] Basic implementation
* [ ] Builds available in the maven repository
* [ ] Pandomium wrapper for JCEF objects
* [ ] Advanced Java <-> JS bridge
* [ ] ByteBuffer implementation
