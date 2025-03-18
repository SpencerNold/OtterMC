// ignore_for_file: avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:path_provider/path_provider.dart';

abstract class Version {
  static final Version version189 = _Version189();

  static List<Version> versions = [version189, _Version1214()];

  String getName();
  Future<bool> install();
  Future<bool> attach();
}

class _Version189 extends Version {
  final String _clientJarName = "client-v1.8.9.jar";

  @override
  String getName() {
    return "1.8.9";
  }

  @override
  Future<bool> install() async {
    await Future.delayed(const Duration(milliseconds: 250));
    return true;
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
    process.stdout.transform(utf8.decoder).forEach(print);
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
  Future<bool> install() async {
    await Future.delayed(const Duration(milliseconds: 250));
    return true;
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
    process.stdout.transform(utf8.decoder).forEach(print);
    return true;
  }
}

Future<Directory> _findClientDirectory() async {
  String sep = Platform.pathSeparator;
  if (Platform.isMacOS) {
    return Directory(
        "${(await getApplicationSupportDirectory()).absolute.path}${sep}minecraft${sep}ottermc");
  } else if (Platform.isWindows) {
    return Directory(
        "${(await getApplicationSupportDirectory()).absolute.path}$sep.minecraft${sep}ottermc");
  } else {
    return Directory("~/.minecraft");
  }
}
