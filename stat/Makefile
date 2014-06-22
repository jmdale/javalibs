JAVAC_FLAGS = -Xlint -Xlint:-serial -classpath ..:$(CLASSPATH) -source 1.5 -deprecation
JAVAC = javac $(JAVAC_FLAGS)

JAVA_SOURCES := $(wildcard *.java)
JAVA_CLASSES = $(JAVA_SOURCES:.java=.class)

all: $(JAVA_CLASSES)
	$(MAKE) -C dist
#	$(MAKE) -C hmm
#	$(MAKE) -C test

# Pattern rule to compile Java files.
%.class : %.java
	$(JAVAC) $<

# Remove class files and emacs backup files.
clean: 
	rm -vf *.class *~
	$(MAKE) -C dist clean
#	$(MAKE) -C hmm clean
#	$(MAKE) -C test clean
