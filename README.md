# Pong
[![Build Status](https://travis-ci.org/suckowbiz/pong.svg)](https://travis-ci.org/suckowbiz/pong)  [![Coverage Status](https://coveralls.io/repos/suckowbiz/pong/badge.svg)](https://coveralls.io/r/suckowbiz/pong)

# What is it?
A Java EE 7 enabled Java 8 web application to store infrastructure details of distributed host(s).

# Set up
In order to provide administrative access little configuration is required. For this purpose the path to a configuration file is expected to be available as system property named "PONG_CONFIG_PATH". The configuration file must contain the following properties:

```
supertoken=4711
```

# Use it
*POST* information that *duke* is located at *127.0.0.1* accessible via *ssh* on port *22*. A header parameter *X-PONG-TOKEN* is expected. It is initiated on first time.
```
http://localhost:8080/pong-0.0.1-SNAPSHOT/resources/hosts/duke/127.0.0.1/22/ssh
```

*GET* all hosts using *X-PONG-TOKEN* with super token value
```
http://localhost:8080/pong-0.0.1-SNAPSHOT/resources/hosts/all
```
