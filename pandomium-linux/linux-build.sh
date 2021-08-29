#
# Linux x64
#
# Requirements:
#   Git
#   CMake
#   Python 2.7
#   Oracle JDK 8 with JAVA_HOME
#   libnss3, libx11-xcb-dev, libxss1
#   libasound2, libxtst6, unzip
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
cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..

# Required dependencies
sudo apt-get install libnss3
sudo apt-get install libx11-xcb-dev
sudo apt-get install libxss1
sudo apt-get install libasound2
sudo apt-get install libxtst6
sudo apt-get install unzip
sudo apt-get install libgbm1
sudo apt-get install maven
sudo apt-get install build-essential

# Build sources
mkdir ./native/Release
make -j4
cd ../tools/

# Modification fixes
./compile.sh linux64

# Test
export LD_LIBRARY_PATH="${JAVA_HOME}/jre/lib/amd64/"
./run.sh linux64 Release detailed

# Create binary distrib
./make_distrib.sh linux64

cd ../..
mkdir linux64
cd src/binary_distrib/linux64/bin

# Create fat jar
mkdir jcef-linux64
(cd jcef-linux64; unzip -uo ../gluegen-rt.jar)
(cd jcef-linux64; unzip -uo ../gluegen-rt-natives-linux-amd64.jar)
(cd jcef-linux64; unzip -uo ../jcef.jar)
(cd jcef-linux64; unzip -uo ../jogl-all.jar)
(cd jcef-linux64; unzip -uo ../jogl-all-natives-linux-amd64.jar)

# Move output
cd ../../../../
# cp src/binary_distrib/linux64/bin/jcef-linux64.jar linux64/jcef-linux64.jar
cp -r src/binary_distrib/linux64/bin/jcef-linux64/ linux64/jcef-linux64
cp -r src/binary_distrib/linux64/bin/lib/linux64 linux64/natives

mkdir -p linux64/src/org && cp -r src/java/org linux64/src/org

# Remove symbols libcef.so (500MB -> 100MB)
strip linux64/natives/libcef.so

#
# Download 'liblinuxenv.so' from github.com/dzikoysk/LinuxEnv and put in the natives directory
#

# Pack natives
cd linux64/natives/ && tar -cf - . | xz -9e -c - > ../../linux64/linux64-natives.tar.xz && cd -

# Create fat jar
jar -cvf pandomium-natives-linux64-81.2.jar -C jcef-linux64 .

mvn install:install-file\
    -Dpackaging=jar\
    -Dfile=pandomium-linux/jcef-linux64.jar\
    -DgroupId=org.panda-lang.pandomium-natives\
    -DartifactId=pandomium-natives-linux64\
    -Dversion=81.2

