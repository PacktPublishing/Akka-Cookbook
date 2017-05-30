# Akka Cookbook
This is the code repository for [Akka Cookbook](https://www.packtpub.com/application-development/akka-cookbook?utm_source=github&utm_medium=repository&utm_content=9781785288180), published by Packt. It contains all the supporting project files necessary to work through the book from start to finish.

## About the book
Akka is an open source toolkit that simplifies the construction of distributed and concurrent applications on the JVM. This book will teach you how to develop reactive applications in Scala using the Akka framework.

This book will show you how to build concurrent, scalable, and reactive applications in Akka. You will see how to create high performance applications, extend applications, build microservices with Lagom, and more.

We will explore Akka's actor model and show you how to incorporate concurrency into your applications. The book puts a special emphasis on performance improvement and how to make an application available for users. We also make a special mention of message routing and construction.

By the end of this book, you will be able to create a high-performing Scala application using the Akka framework.

## Instructions and Navigation
All of the code is organized into folders. Each folder starts with a number followed by the application name. For example, Chapter 06.

The code will look like the following:

        package com.packt.chapter6
         sealed trait UserAction
         case object Add extends UserAction
         case object Remove extends UserAction
         case class UserUpdate(userId: String, action: UserAction)
         sealed trait Event
         case class AddUserEvent(userId: String) extends Event
         case class RemoveUserEvent(userId: String) extends Event
         
### Software requirements:
| __Chapter number__ | **Software required (With version)** | **Free/Proprietary** | **If proprietary, can code testing be performed using a trial version** | **If proprietary, then cost of the software** | **Download links to the software** | __Hardware specifications__ | **OS required** |
|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
| All | SBT 0.13.13 | Free | No |  | http://www.scala-sbt.org/ | No hardware specifications. | Windows,Linux,Mac OS |
 
#### Detailed installation steps (software-wise)
The steps should be listed in a way that it prepares the system environment to be able to test the codes of the book.

SBT:
        a. Download latest package from http://www.scala-sbt.org/download.html
        b. Install package
        c. Run sbt “sbt sbtVersion” in the command line to test the software was installed correctly.
    
### Note:
The Code folder contains the code samples used in this book.

Code bundle for Chapter 1 to 5 is present in the "Hello-akka" folder.

Software_Hardware_list file contains the list of required software and hardware for this book.

Happy coding! :)

## Related Products:
* [Apache Kafka Cookbook](https://www.packtpub.com/big-data-and-business-intelligence/apache-kafka-cookbook?utm_source=github&utm_medium=repository&utm_content=9781785882449)

* [Building Microservice with AKKA HTTP [Video]](https://www.packtpub.com/application-development/building-microservice-akka-http-video?utm_source=github&utm_medium=repository&utm_content=9781788298582)

* [Mastering Akka](https://www.packtpub.com/application-development/mastering-akka?utm_source=github&utm_medium=repository&utm_content=9781786465023)

### Suggestions and Feedback
[Click here](https://docs.google.com/forms/d/e/1FAIpQLSe5qwunkGf6PUvzPirPDtuy1Du5Rlzew23UBp2S-P3wB-GcwQ/viewform) if you have any feedback or suggestions.
