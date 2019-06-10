#!/usr/bin/env bash

#
# Windows x64
#
# Requirements:
#   Git for Windows
#   CMake for WIndows
#   Visual Stdio 2017 (Windows 10 SDK, C++ Support)
#   Python 2.7 added to PATH
#   JDK 8 added to PATH
#

mkdir JCEF && cd JCEF
git clone https://bitbucket.org/chromiumembedded/java-cef.git src && cd src

# Modify sources
find ./java/org -type f -exec sed -i 's/ protected / public /g' {} +
find ./java/org -type f -exec sed -i 's/ private / public /g' {} +
find ./java/org -type f -exec sed -i 's/ final /  /g' {} +

# Modification fixes
find ./java/org -type f -exec sed -i 's/public TransitionFlags(/TransitionFlags(/g' {} +
find ./java/org -type f -exec sed -i 's/public TransitionType(/TransitionType(/g' {} +
find ./java/org -type f -exec sed -i 's/static  int MENU_ID/static final int MENU_ID/g' {} +

# Build natives
mkdir jcef_build && cd jcef_build
cmake -G "Visual Studio 15 Win64" ..
sync

echo ""
echo "Open jcef.sln in Visual Studio 2017"
echo "- Select Build > Configuration Manager and change the 'Active solution configuration' to 'Release'"
echo "- Select Build > Build Solution. Then:"

start 'jcef.sln'
read -p "Press enter to continue"
cd ../tools/

# Modification fixes
./compile.bat win64

# Test
./run.bat win64 Release detailed

# Create binary distrib
./make_distrib.bat win64

cd ../..
mkdir win64
cd ./src/binary_distrib/win64/bin

# Create fat content jar
mkdir jcef-win64
(cd jcef-win64; unzip -uo ../gluegen-rt.jar)
(cd jcef-win64; unzip -uo ../gluegen-rt-natives-windows-amd64.jar)
(cd jcef-win64; unzip -uo ../jcef.jar)
(cd jcef-win64; unzip -uo ../jogl-all.jar)
(cd jcef-win64; unzip -uo ../jogl-all-natives-windows-amd64.jar)

# Move output
cd ../../../../
cp -r ./src/binary_distrib/win64/bin/jcef-win64 ./win64/jcef-win64
cp -r ./src/binary_distrib/win64/bin/lib/win64 ./win64/natives

# Pack natives
cd ./win64/natives/ && tar -cf - . | xz -9e -c - > ../../win64/jcef-win64/win64-natives.tar.xz && cd -

# Create fat jar
cd win64 && jar -cvf pandomium-natives-win64-73.0.jar -C ./jcef-win64 .

# Deploy
"../../pandomium-tools/maven/bin/mvn" -s $HOME/.m2/settings.xml deploy:deploy-file \
 -DrepositoryId="panda-repository" \
 -Durl="https://repo.panda-lang.org/releases" \
 -Dpackaging="jar" \
 -Dfile="./win64/pandomium-natives-win64-73.0.jar" \
 -DgroupId="org.panda-lang.pandomium-natives" \
 -DartifactId="pandomium-natives-win64" \
 -Dversion="73.0"