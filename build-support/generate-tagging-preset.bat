@ECHO off
CLS

ECHO ---- xjc-compiler for tagging-presets1.0.xsd -----
xjc tagging-preset-1.0.xsd -d ../../../../ -extension
ECHO --------------------------- done ----------------------------
PAUSE