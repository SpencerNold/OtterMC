#!/bin/zsh

THIS="distributors/macos"
TARGET_DIR="$THIS/staging/Library/Application Support/structure"
mkdir -p "$TARGET_DIR"

# client
OMC_DIR="$TARGET_DIR/ottermc"
mkdir -p "$OMC_DIR"
cp "client-latest/build/libs/client-latest-remapped-joined.jar" "$OMC_DIR/client-latest.jar"
cp "client-v1.8.9/build/libs/client-v1.8.9-remapped-joined.jar" "$OMC_DIR/client-v1.8.9.jar"

# json files
LATEST_DIR="$TARGET_DIR/versions/ottermc-latest"
mkdir -p $LATEST_DIR
cp "client-latest/latest.json" "$LATEST_DIR/ottermc-latest.json"

V189_DIR="$TARGET_DIR/versions/ottermc-v1.8.9"
mkdir -p $V189_DIR
cp "client-v1.8.9/v1.8.9.json" "$V189_DIR/ottermc-v1.8.9.json"

# wrapper
WRAPPER_DIR="$TARGET_DIR/libraries/io/github/ottermc/wrapper/1.0.0"
mkdir -p "$WRAPPER_DIR"
cp "wrapper/build/libs/wrapper.jar" "$WRAPPER_DIR/wrapper-1.0.0.jar"

# profiler
cp "distributors/profiler/build/libs/profiler-all.jar" "$TARGET_DIR/profiler.jar"

pkgbuild --identifier io.github.ottermc --version 1.0.0 --scripts "$THIS/scripts" --root "$THIS/staging" OtterMC.pkg