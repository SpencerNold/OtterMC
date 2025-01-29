abstract class Version {
  static final Version version189 = _Version189();

  static List<Version> versions = [version189, _Version1214()];

  String getName();
  Future<bool> install();
  Future<bool> attach();
}

class _Version189 extends Version {
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
    return true;
  }
}

class _Version1214 extends Version {
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
    return true;
  }
}
