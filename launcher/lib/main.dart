import 'package:flutter/material.dart';
import 'package:launcher/theme.dart';

// TODO
// pretty up the ui
// support different versions
// allow dynamic loaded

void main() {
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          backgroundColor: ColorTheme.bk2,
          title: const Text(
            "OtterMC (ALPHA-0.0.1)",
            style: TextStyle(color: ColorTheme.light),
          ),
        ),
        backgroundColor: ColorTheme.bk1,
        body: Center(
          child: ElevatedButton(
            onPressed: () {
              // add profile to launcher_profiles.json
              // copy old installation with profile name and jar file
              // insert ottermc client json
              // download client, wrapper, and plugins
            },
            style: const ButtonStyle(
              backgroundColor: WidgetStatePropertyAll(ColorTheme.accent),
              fixedSize: WidgetStatePropertyAll(Size(175, 50)),
              shape: WidgetStatePropertyAll(
                RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(10.0)),
                ),
              ),
            ),
            child: const Text(
              "Install",
              style: TextStyle(color: ColorTheme.light),
            ),
          ),
        ),
      ),
    );
  }
}
/*

{
    "assetIndex": {
      "id": "1.8"
    },
    "assets": "1.8",
    "complianceLevel": 0,
    "id": "tester",
    "javaVersion": {
      "component": "jre-legacy",
      "majorVersion": 8
    },
    "libraries": [
      {
        "name": "io.github.ottermc:wrapper:1.0.0"
      }
    ],
    "logging": {
      "client": {
        "argument": "-Dlog4j.configurationFile=${path}",
        "file": {
          "id": "client-1.7.xml"
        },
        "type": "log4j2-xml"
      }
    },
    "mainClass": "io.github.ottermc.Wrapper",
    "minecraftArguments": "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userProperties ${user_properties} --userType ${user_type}",
    "minimumLauncherVersion": 14,
    "type": "release"
  }

*/
