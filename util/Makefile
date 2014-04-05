JAVAC_FLAGS = -Xlint -Xlint:-serial -classpath ..:$(CLASSPATH) -source 1.6 -deprecation
JAVAC = javac $(JAVAC_FLAGS)

JAVA_SOURCES := $(wildcard *.java)
JAVA_CLASSES = $(JAVA_SOURCES:.java=.class)

all: $(JAVA_CLASSES)
#	$(MAKE) -C bool
#	$(MAKE) -C combx
	$(MAKE) -C fn
#	$(MAKE) -C graph
	$(MAKE) -C str

%.class : %.java
	$(JAVAC) $<

clean: 
	rm -vf *.class *~
#	$(MAKE) -C bool clean
#	$(MAKE) -C combx clean
	$(MAKE) -C fn clean
#	$(MAKE) -C graph clean
	$(MAKE) -C str clean
