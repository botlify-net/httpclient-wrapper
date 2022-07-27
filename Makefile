##
## HTTPCLIENT WRAPPER, 2022
## HttpClient Wrapper Makefile
## File description:
## Generic Makefile for HttpClient Wrapper
##

#=================================
#	Variables
#=================================

APP_NAME 	= httpclient-wrapper

APP_VERSION = 1.0

#=================================
#	Commands
#=================================

.PHONY:				all \
					install \
					test \
					finstall \
					clean

all:				install

# This command install the application
install:
					mvn install

# This command install the application without the tests
finstall:
					mvn install -DskipTests

# This command start all the tests
test:
					mvn test

# This command remove the target directory
clean:
					mvn clean
