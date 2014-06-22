JAVAC_FLAGS = -Xlint -Xlint:-serial -classpath ../..:$(CLASSPATH) -source 1.5 -deprecation
JAVAC = javac $(JAVAC_FLAGS)

JAVA_SOURCES := $(wildcard *.java)
JAVA_CLASSES = $(JAVA_SOURCES:.java=.class)

all: $(JAVA_CLASSES)

# Pattern rule to compile Java files.
%.class : %.java
	$(JAVAC) $<

# Remove class files and emacs backup files.
clean: 
	rm -vf *.class *~
