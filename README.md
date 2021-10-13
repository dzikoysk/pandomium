# Pandomium [![Build Status](https://travis-ci.org/dzikoysk/Pandomium.svg?branch=master)](https://travis-ci.org/Panda-Programming-Language/Pandomium) [![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/dzikoysk/Pandomium.svg)](http://isitmaintained.com/project/dzikoysk/Pandomium "Average time to resolve an issue")
Pandomium is the JCEF (Java Chromium Embedded Framework) implementation dedicated for the maven projects

#### Example
<!-- ![PandomiumTest.java - Windows x64](https://panda-lang.org/screenshot/5d8KeBJg.png) -->
<!-- ![PandomiumTest.java - Linux x64](https://panda-lang.org/screenshot/5E5ZgE9A.png) -->
<!-- ![PandomiumTest.java - Linux x64](https://panda-lang.org/screenshot/bZwbEGmm.png) -->
![PandomiumTest.java - Both x64](https://pandomium.panda-lang.org/github/preview-both.png)

```java
PandomiumClient client = Pandomium.buildDefault().createClient();
CefBrowser browser = client.loadURL("https://panda-lang.org");
```
Full example: [PandomiumTest.java](https://github.com/dzikoysk/Pandomium/blob/master/pandomium/src/test/java/org/panda_lang/pandomium/PandomiumTest.java)

#### Installation
 - Java 8 or higher required
 - [Maven instructions for the latest release](https://github.com/dzikoysk/pandomium/releases/)

#### Features
 - Almost always running the latest version of JCEF, thanks to an advanced CI system
 - Supported platforms are the same as for JCEF (may differ for each release)
 - Provides methods and classes to easily access core JCEF components

#### Building
* [Windows x64](https://github.com/dzikoysk/Pandomium/wiki/Windows-x64-Build)
* [Linux x64](https://github.com/dzikoysk/Pandomium/wiki/Linux-x64-Build)
* [macOS](https://github.com/dzikoysk/Pandomium/wiki/macOS-Build)

#### TODO
* [x] Build natives & jcef
* [x] Basic implementation
* [x] Builds available in the maven repository
* [x] Pandomium wrapper for JCEF objects
* [ ] macOS support
* [ ] Advanced Java <-> JS bridge
* [ ] ByteBuffer implementation
