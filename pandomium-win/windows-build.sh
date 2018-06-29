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

mkdir JCEF67 && cd JCEF67
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

# Open jcef.sln in Visual Studio 2017
# - Select Build > Configuration Manager and change the "Active solution configuration" to "Release"
# - Select Build > Build Solution. Then:
cd ../tools/

# Modification fixes
./compile.bat win64

# Test
./run.bat win64 Release detailed

# Create binary distrib
./make_distrib.bat win64

cd ../..
mkdir win64
cd src/binary_distrib/win64/bin

# Create fat jar
mkdir jcef-win64
(cd jcef-win64; unzip -uo ../gluegen-rt.jar)
(cd jcef-win64; unzip -uo ../gluegen-rt-natives-windows-amd64.jar)
(cd jcef-win64; unzip -uo ../jcef.jar)
(cd jcef-win64; unzip -uo ../jogl-all.jar)
(cd jcef-win64; unzip -uo ../jogl-all-natives-windows-amd64.jar)
jar -cvf jcef-win64.jar -C jcef-win64 .

# Move output
cd ../../../../
cp src/binary_distrib/win64/bin/jcef-win64.jar win64/jcef-win64.jar
cp -r src/binary_distrib/win64/bin/lib/win64 win64/natives
mkdir -p win64/src/org && cp -r src/java/org win64/src/org

# Pack natives
cd win64/natives/ && tar -cf - . | xz -9e -c - > ../../win64/win64-natives.tar.xz && cd -