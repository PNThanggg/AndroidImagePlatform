import 'dart:ui' as ui show Codec;

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

/// A [ImageProvider] get data from android drawable/bitmap.
class AndroidPlatformImage extends ImageProvider<AndroidPlatformImage> {
  const AndroidPlatformImage(
    this.id, {
    this.scale = 1.0,
    this.quality = 100,
    this.type = AndroidPlatformImageType.drawable,
  });

  static const MethodChannel _channel =
      MethodChannel('android_platform_images');

  /// name or alias for drawable&mipmap, full path for assets.
  final String id;

  /// compress quality for drawable&mipmap
  final int quality;

  /// scale of image
  final double scale;

  /// type of android platform image, [AndroidPlatformImageType.drawable]
  /// for drawable & mipmap, [AndroidPlatformImageType.assets] for assets.
  final AndroidPlatformImageType type;

  @override
  ImageStreamCompleter loadImage(
    AndroidPlatformImage key,
    ImageDecoderCallback decode,
  ) {
    return MultiFrameImageStreamCompleter(
      codec: _loadAsync(key, decode),
      scale: key.scale,
      debugLabel: key.id,
      informationCollector: () sync* {
        yield ErrorDescription('Resource: $id');
      },
    );
  }

  @override
  Future<AndroidPlatformImage> obtainKey(ImageConfiguration configuration) {
    return Future<AndroidPlatformImage>.value(this);
  }

  Future<ui.Codec> _loadAsync(
      AndroidPlatformImage key, ImageDecoderCallback decode) async {
    assert(key == this);
    final Uint8List? bytes = await _channel
        .invokeMethod<Uint8List>(describeEnum(type), <String, dynamic>{
      'id': id,
      'quality': quality,
    });

    if (bytes == null) {
      throw StateError('$id does not exist and cannot be loaded as an image.');
    }

    final buffer = await ImmutableBuffer.fromUint8List(bytes);
    return decode(buffer);
  }
}

/// Android Platform Image Type.
enum AndroidPlatformImageType {
  /// res/drawable or mipmap
  drawable,

  /// assets
  assets,
}
