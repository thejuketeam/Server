package com.juke.models

import akka.actor.{Actor, ActorLogging ,ActorRef, ActorSystem, Props}
//Import titan graph
import com.thinkaurelius.titan.core.{TitanGraph, TitanVertex, TitanEdge, TitanFactory}
import org.apache.commons.configuration.BaseConfiguration
import org.apache.commons.configuration.Configuration

//This class forces certain methods to be implemented in anything that tries to access a database.
abstract class DatabaseActor extends Actor with akka.actor.ActorLogging {
	import context.system

	protected var graph : TitanGraph = null

	override def preStart() : Unit = {
		val conf : Configuration = new BaseConfiguration()
		conf.setProperty("storage.backend", "cassandra")
		conf.setProperty("storage.hostname", "127.0.0.1")

		graph = TitanFactory.open(conf)
	}
}
