package io.kubikl.omniscient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class OmniscientApplication

fun main(args: Array<String>) {
	runApplication<OmniscientApplication>(*args)
}
