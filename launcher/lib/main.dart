import 'package:flutter/material.dart';
import 'package:launcher/theme.dart';
import 'package:launcher/windows.dart';

void main() {
  runApp(const App());
}

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  int index = 0;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "OtterMC Launcher (ALPHA-0.0.1)",
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          backgroundColor: ColorTheme.bk2,
          centerTitle: false,
          title: const Row(
            children: [
              ImageIcon(
                AssetImage("assets/otter_icon.png"),
                color: ColorTheme.light,
                size: 40.0,
              ),
              SizedBox(width: 10.0),
              Text(
                "OtterMC",
                style: TextStyle(color: ColorTheme.light),
              )
            ],
          ),
          actions: [
            _createTabButton("INSTALL", 0),
            _createTabButton("ATTACH", 1),
            _createTabButton("UPDATES", 2),
            _createTabButton("GITHUB", 3),
          ],
        ),
        backgroundColor: ColorTheme.bk1,
        body: _createBodyWidget(),
      ),
    );
  }

  Widget? _createBodyWidget() {
    switch (index) {
      case 0:
        return const InstallWindow();
      case 1:
        break;
      case 2:
        break;
      case 3:
        break;
    }
    return null;
  }

  Widget _createTabButton(String display, int idx) {
    return TextButton(
      onPressed: () {
        setState(() => index = idx);
      },
      style: ButtonStyle(
        shape: WidgetStatePropertyAll(
          RoundedRectangleBorder(borderRadius: BorderRadius.circular(5.0)),
        ),
      ),
      child: Container(
        decoration: BoxDecoration(
          border: index != idx
              ? null
              : const Border(
                  bottom: BorderSide(width: 2.0, color: ColorTheme.accent),
                ),
        ),
        child: Text(
          display,
          style: const TextStyle(color: ColorTheme.accent),
        ),
      ),
    );
  }
}
