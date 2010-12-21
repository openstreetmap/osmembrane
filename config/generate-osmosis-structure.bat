@ECHO off
CLS

ECHO ---- xjc-compiler for osmosis-structure-definitions.xsd -----
xjc osmosis-structure-definitions.xsd -d ../src -extension
ECHO --------------------------- done ----------------------------
PAUSE