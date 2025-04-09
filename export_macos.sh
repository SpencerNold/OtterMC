#! /bin/bash
echo [OtterMC] Building entire gradle project...
./gradlew build
echo [OtterMC] Packaging installercli.jar...
./gradlew installercli:buildCompleteJar
mv installercli/build/libs/installercli-complete.jar launcher/assets/installercli.jar
cd launcher/macos_dist
echo [OtterMC] Building OtterMC.dmg file...
./build_macos.sh

