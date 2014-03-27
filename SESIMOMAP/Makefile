all: linux solaris10

linux: 
	javac -Xlint -classpath ./src:./libs/log4j-1.2.8.jar -sourcepath ./src -d ./bin_linux ./src/**/*.java

solaris10: 
	javac -Xlint -classpath ./src:./libs/log4j-1.2.8.jar -sourcepath ./src -d ./bin_solaris10 ./src/**/*.java
