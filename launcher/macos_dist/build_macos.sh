#! /bin/bash
rm -rf temp && mkdir temp
cd ../
rm -dr build
flutter build macos
cd build/macos/Build/Products/Release
mv launcher.app ../../../../../macos_dist/temp/OtterMC.app
cd ../../../../../macos_dist
mkdir temp/installer
cp -R temp/OtterMC.app temp/installer
ln -s /Applications temp/installer/Applications
rm ../OtterMC.dmg
./create-dmg.sh
