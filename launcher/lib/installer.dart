// ignore_for_file: avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:flutter/services.dart';

abstract class Version {
  static final Version version189 = _Version189();

  static List<Version> versions = [version189, _Version1214()];

  String getName();
  Future<bool> attach();

  static Stream<String> install() async* {
    await Future.delayed(const Duration(milliseconds: 250));
    final file = File(
      "${(await _findClientDirectory()).absolute.path}${Platform.pathSeparator}installercli.jar",
    );
    print(file.absolute.path);
    final data = await rootBundle.load("assets/installercli.jar");
    final buffer = data.buffer;
    await file.writeAsBytes(
      buffer.asUint8List(data.offsetInBytes, data.lengthInBytes),
      flush: true,
    );
    final process = await Process.start("java", ["-jar", file.absolute.path]);
    final stdout = process.stdout
        .transform(utf8.decoder)
        .transform(const LineSplitter());
    final stderr = process.stderr
        .transform(utf8.decoder)
        .transform(const LineSplitter());
    await for (final line in stdout) {
      print(line);
      if (!line.startsWith("[OtterMC]")) {
        yield "[ERROR] $line";
      } else {
        yield line;
      }
    }
    await for (final line in stderr) {
      print(line);
      yield "[ERROR] $line";
    }
    await file.delete();
  }
}

class _Version189 extends Version {
  final String _clientJarName = "client-v1.8.9.jar";

  @override
  String getName() {
    return "1.8.9";
  }

  @override
  Future<bool> attach() async {
    await Future.delayed(const Duration(milliseconds: 250));
    Directory clientDir = await _findClientDirectory();
    if (!await clientDir.exists()) {
      return false;
    }
    String client = clientDir.absolute.path + _clientJarName;
    Process process = await Process.start("java", ["-jar", client, "1.8.9"]);
    await process.stdout.transform(utf8.decoder).forEach(print);
    return true;
  }
}

class _Version1214 extends Version {
  final String _clientJarName = "client-v1.21.4.jar";

  @override
  String getName() {
    return "1.21.4";
  }

  @override
  Future<bool> attach() async {
    await Future.delayed(const Duration(milliseconds: 250));
    Directory clientDir = await _findClientDirectory();
    if (!await clientDir.exists()) {
      return false;
    }
    String client = clientDir.absolute.path + _clientJarName;
    Process process = await Process.start("java", ["-jar", client, "1.8.9"]);
    await process.stdout.transform(utf8.decoder).forEach(print);
    return true;
  }
}

Future<Directory> _findClientDirectory() async {
  if (Platform.isMacOS) {
    return Directory(
      "${Platform.environment['HOME']}/Library/Application Support/minecraft/ottermc",
    );
  } else if (Platform.isWindows) {
    return Directory("${Platform.environment['APPDATA']}\\.minecraft\\ottermc");
  } else {
    return Directory("${Platform.environment['HOME']}/.minecraft/ottermc");
  }
}
