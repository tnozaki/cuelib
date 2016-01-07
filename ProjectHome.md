Cuelib is a java library for manipulating [cue sheets](http://en.wikipedia.org/wiki/Cue_sheet_%28computing%29).

You can use cuelib to...
  * Create an internal representation of a cue sheet, either from scratch or by parsing an existing one. The parser is designed to be very tolerant for maximum compatibility, but it will raise a warning for any deviation of the spec that it detects, helping you to fix problems.
  * Manipulate the internal representation to read specific data, or to make changes.
  * Turn the representation back into a cue sheet.
  * Convert the representation into XML so you can use your XML tools to process it. You can easily convert it back to a cue sheet with the XSLT we provide.
  * [Cut up files into track-sized bits and (optionally) post-process these](Using_TrackCutter.md). You can use this functionality to easily create mp3 files (with ID3 tags based on the cue sheet) from wav files. Support for [tracks hidden in pregaps](http://en.wikipedia.org/wiki/Stoosh) is included.
  * More features are [planned](http://code.google.com/p/cuelib/issues/list?can=2&q=milestone%3Arelease1.3.0&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

Besides the fields [originally specified for cue sheets](http://digitalx.org/cuesheetsyntax.php), cuelib also supports the extra fields used by [Exact Audio Copy](http://www.exactaudiocopy.de/).

Cuelib requires [java SE 1.5 or higher](http://java.sun.com), and is released under [version 2.1 of the GNU Lesser Public License](http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html). Feel free to check out the [source code](http://code.google.com/p/cuelib/source/browse). You may change the code and you may redistribute it with or without your changes, as long as you comply with the license. You don't need to ask for permission.

If you discover a problem with cuelib, or if you have ideas on how to improve cuelib, please [tell us about it](http://code.google.com/p/cuelib/issues/list).