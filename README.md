# Feign_Demo_Project

## Goal

* We can use the **feign client**.

* And you can easily use this project

  because this is the basic structure is all in place.

## Feature

* Set **Default Profile** when an inappropriate profile value is entered

  * ref : *YamlEnvironmentPostProcessor.class*
  
* We can use *custom* *.*yml property*

  * ref : *YamlEnvironmentPostProcessor.class*
    
* We can use **ErrorDecoder** for Feign

  * ref : *DemoFeignErrorDecoder.class*, *DemoFeignClient.class*
  
* We can use **Custom TimeOut** Property while using feign client

  * ref : *application-{profile}.yml*, *DemoFeignClient.class*

## Version

* Java : 11

* Spring Boot : 2.4.2

* Feign : 2020.0.1

* Build Tool : Gradle

## Dependency

> build.gradle

```
ext {
    /**
     * Spring Boot and springCloudVersion must be compatible.
     * 2.4.x == 2020.0.x
     * ref : https://spring.io/projects/spring-cloud
     */
    // Feign
    set('springCloudVersion', '2020.0.1')
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}

dependencies {
    // Feign
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    ...
}
```

## Comment

* If you want to test profile environment

  use this option

```
-Dspring.profiles.active={profile}
```

* I recommend you. 

  See how the properties of `config/custom/url.yml` are used and worked
  
  
