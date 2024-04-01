all: Project3.class DataItem.class Table.class Instr.class
Instr.class: Instr.java
	javac Instr.java
Table.class: Table.java
	javac Table.java
DataItem.class: DataItem.java
	java DataItem.java
Project3.class: Project3.java
	javac Project3.java







