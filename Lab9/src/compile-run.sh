#!/bin/bash
jflex minilang.jflex
sleep 10
javac SymTabs.java
java SymTabs Main.java
java SymTabs MainL1.java
java SymTabs MainL2.java