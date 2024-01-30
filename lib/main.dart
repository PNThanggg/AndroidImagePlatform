import 'package:flutter/material.dart';

import 'android_platform_images.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: CustomScrollView(
          slivers: <Widget>[
            // // test custom alias
            // SliverGrid.count(
            //   crossAxisCount: 2,
            //   children: const <Widget>[
            //     Image(
            //       image: AndroidPlatformImage('launch_icon'),
            //     ),
            //     Image(
            //       image: AndroidPlatformImage('max_1_alias'),
            //     ),
            //   ],
            // ),
            SliverGrid.count(
              crossAxisCount: 3,
              children: <Widget>[
                ...List<Widget>.generate(
                  3,
                  (int index) => Image(
                    image: AndroidPlatformImage('max_${index + 1}'),
                  ),
                ),
              ],
            ),
            SliverGrid.count(
              crossAxisCount: 3,
              children: <Widget>[
                ...List<Widget>.generate(
                  3,
                  (int index) => Image(
                    image: AndroidPlatformImage(
                      'max_${index + 1}',
                      quality: 40,
                    ),
                  ),
                ),
              ],
            ),
            SliverGrid.count(
              crossAxisCount: 3,
              children: <Widget>[
                ...List<Widget>.generate(
                  9,
                  (int index) => Image(
                    image: AndroidPlatformImage('min_${index + 1}'),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
