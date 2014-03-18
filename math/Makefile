JAVAC_FLAGS = -Xlint -Xlint:-serial -classpath ..:$(CLASSPATH) -source 1.5 -deprecation
JAVAC = javac $(JAVAC_FLAGS)

JAVA_SOURCES := $(wildcard *.java)
JAVA_CLASSES = $(JAVA_SOURCES:.java=.class)

OS := $(shell uname -s)

ifeq ($(OS), Linux)
JNI_LIB = libjnimath.so
else
ifeq ($(OS), Darwin)
JNI_LIB = libjnimath.jnilib
endif
endif

all: $(JAVA_CLASSES) $(JNI_LIB)
#	$(MAKE) -C min
#	$(MAKE) -C root

# Pattern rule to compile Java files.
%.class : %.java
	$(JAVAC) $<

math_JNIMath.h: JNIMath.class
	javah -jni math.JNIMath

ifeq ($(OS), Linux)
CC_FLAGS = -fPIC -I/opt/java/include -I/opt/java/include/linux -I/usr/lib/jvm/java-6-openjdk/include
else
ifeq ($(OS), Darwin)
CC_FLAGS = -I/System/Library/Frameworks/JavaVM.framework/Versions/1.5/Headers
endif
endif

# The following optimization options have been tested on Linux, and
# either don't improve running time, or actually make it worse:
# -funroll-loops, -fprefetch-loop-arrays, -ffast-math
math_JNIMath.o: math_JNIMath.c math_JNIMath.h
	gcc -Wall -O3 $(CC_FLAGS) -c math_JNIMath.c -o math_JNIMath.o

libjnimath.so: math_JNIMath.o
	ld -shared math_JNIMath.o -o libjnimath.so
	mkdir -pv ../lib/
	cp -pv libjnimath.so ../lib/

libjnimath.jnilib: math_JNIMath.o
	gcc -dynamiclib -o libjnimath.jnilib math_JNIMath.o
	mkdir -pv ../lib/
	cp -pv libjnimath.jnilib ../lib/

# Remove class files and emacs backup files.
clean: 
	rm -vf *.class *~ math_JNIMath.h math_JNIMath.o libjnimath.so ../lib/libjnimath.so libjnimath.jnilib ../lib/libjnimath.jnilib
#	$(MAKE) -C min clean
#	$(MAKE) -C root clean
