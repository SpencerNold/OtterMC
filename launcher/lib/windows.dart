import 'dart:async';

import 'package:flutter/material.dart';
import 'package:launcher/installer.dart';
import 'package:launcher/theme.dart';

class ClientWindow extends StatelessWidget {
  const ClientWindow({super.key});

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        Container(
          margin: const EdgeInsets.all(20.0),
          height: (height / 2) - 40,
          decoration: BoxDecoration(
            color: ColorTheme.light,
            borderRadius: BorderRadius.circular(10.0),
            image: DecorationImage(
              image: const AssetImage("assets/background.png"),
              fit: BoxFit.cover,
              colorFilter: ColorFilter.mode(
                ColorTheme.bk1.withOpacity(0.75),
                BlendMode.darken,
              ),
            ),
          ),
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton(
                  onPressed: () async {
                    Version? version = await showDialog(
                      context: context,
                      builder: (context) {
                        ContainerController<Version> controller =
                            ContainerController(Version.version189);
                        return AlertDialog(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(7.0),
                          ),
                          backgroundColor: ColorTheme.bk2,
                          title: const Center(
                            child: Text(
                              "Select Version",
                              style: TextStyle(color: ColorTheme.light),
                            ),
                          ),
                          content: VersionDropdown(controller),
                          actionsAlignment: MainAxisAlignment.center,
                          actions: [
                            ElevatedButton(
                              onPressed: () {
                                Navigator.of(context).pop(controller.value);
                              },
                              style: ButtonStyle(
                                backgroundColor: const WidgetStatePropertyAll(
                                  ColorTheme.accent,
                                ),
                                padding: const WidgetStatePropertyAll(
                                  EdgeInsets.only(
                                    top: 5.0,
                                    bottom: 5.0,
                                    left: 15.0,
                                    right: 15.0,
                                  ),
                                ),
                                shape: WidgetStatePropertyAll(
                                  RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(25.0),
                                  ),
                                ),
                              ),
                              child: const Text(
                                "Attach",
                                style: TextStyle(color: ColorTheme.light),
                              ),
                            ),
                          ],
                        );
                      },
                    );
                    if (version == null) return;
                    showDialog(
                      // ignore: use_build_context_synchronously
                      context: context,
                      barrierDismissible: false,
                      builder: (context) {
                        return AlertDialog(
                          contentPadding: EdgeInsets.zero,
                          backgroundColor: Colors.transparent,
                          content: Center(
                            child: FutureBuilder(
                              future: version.attach(),
                              builder: (context, snapshot) {
                                if (snapshot.hasData) {
                                  Navigator.of(context).pop();
                                  return Icon(
                                    snapshot.data!
                                        ? Icons.check_circle
                                        : Icons.close_rounded,
                                    color: ColorTheme.accent,
                                    size: 60.0,
                                  );
                                } else if (snapshot.hasError) {
                                  return Text(
                                    "Error ${snapshot.error}",
                                    style: const TextStyle(
                                      color: ColorTheme.light,
                                    ),
                                  );
                                } else {
                                  return const SizedBox(
                                    width: 60.0,
                                    height: 60.0,
                                    child: CircularProgressIndicator(
                                      color: ColorTheme.accent,
                                    ),
                                  );
                                }
                              },
                            ),
                          ),
                        );
                      },
                    );
                  },
                  style: ButtonStyle(
                    padding: const WidgetStatePropertyAll(EdgeInsets.all(25.0)),
                    shape: WidgetStatePropertyAll(
                      RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10.0),
                      ),
                    ),
                    backgroundColor: const WidgetStatePropertyAll(
                      ColorTheme.accent,
                    ),
                  ),
                  child: const Text(
                    "Attach",
                    style: TextStyle(color: ColorTheme.light),
                  ),
                ),
                const SizedBox(height: 15.0),
                ElevatedButton(
                  onPressed: () async {
                    showDialog(
                      context: context,
                      barrierDismissible: false,
                      builder: (context) {
                        return AlertDialog(
                          contentPadding: EdgeInsets.zero,
                          backgroundColor: Colors.transparent,
                          content: Center(child: InstallerLogWidget()),
                        );
                      },
                    );
                  },
                  style: ButtonStyle(
                    padding: const WidgetStatePropertyAll(EdgeInsets.all(25.0)),
                    shape: WidgetStatePropertyAll(
                      RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10.0),
                      ),
                    ),
                    backgroundColor: const WidgetStatePropertyAll(
                      ColorTheme.accent,
                    ),
                  ),
                  child: const Text(
                    "Install",
                    style: TextStyle(color: ColorTheme.light),
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}

class VersionDropdown extends StatefulWidget {
  final ContainerController<Version> _controller;

  const VersionDropdown(this._controller, {super.key});

  @override
  State<VersionDropdown> createState() => _VersionDropdownState();
}

class _VersionDropdownState extends State<VersionDropdown> {
  @override
  Widget build(BuildContext context) {
    return DropdownButton(
      value: widget._controller.value,
      onChanged: (Version? value) {
        setState(() {
          if (value != null) widget._controller.value = value;
        });
      },
      dropdownColor: ColorTheme.bk2,
      borderRadius: BorderRadius.circular(10.0),
      padding: const EdgeInsets.only(left: 10.0, right: 10.0),
      items:
          Version.versions.map((version) {
            return DropdownMenuItem(
              value: version,
              child: Center(
                child: Text(
                  version.getName(),
                  style: const TextStyle(color: ColorTheme.light),
                ),
              ),
            );
          }).toList(),
    );
  }
}

class InstallerLogWidget extends StatefulWidget {
  const InstallerLogWidget({super.key});

  @override
  State<InstallerLogWidget> createState() => _InstallerLogWidgetState();
}

class _InstallerLogWidgetState extends State<InstallerLogWidget> {
  final List<String> _log = [];
  late final StreamSubscription<String> _subscription;

  @override
  void initState() {
    super.initState();
    _subscription = Version.install().listen(
      (line) {
        setState(() => _log.add(line));
      },
      onDone: () {
        if (mounted) {
          Navigator.of(context).pop();
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 300,
      width: 600,
      child: ListView.builder(
        itemBuilder: (context, index) {
          if (index >= _log.length) {
            return const Text("");
          } else {
            return Text(
              _log[index],
              style: const TextStyle(color: ColorTheme.light),
            );
          }
        },
      ),
    );
  }

  @override
  void dispose() {
    _subscription.cancel();
    super.dispose();
  }
}

class ContainerController<T> {
  T value;
  ContainerController(this.value);
}
