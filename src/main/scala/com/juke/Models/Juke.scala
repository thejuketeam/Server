package com.juke.models

import akka.actor.{Actor, ActorLogging ,ActorRef, ActorSystem, Props}
import com.github.nscala_time.time.Imports._
import com.tinkerpop.blueprints.{Edge, Vertex, Graph, Direction}
import scala.collection.JavaConverters._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{ read, write, writePretty }
import org.json4s.native.Serialization


case class Location(lat : Double, long : Double)

case class Juke(songs : Array[Song], title : String, id : Long, location : Location, start_time : DateTime, end_time : DateTime)

class JukeActor extends DatabaseActor with akka.actor.ActorLogging {
	import context.system

	def lookup_self_to_json(juke_id : Long) : String = {
		val juke : Option[Juke] = lookup_self(juke_id)
		implicit val formats = Serialization.formats(NoTypeHints)
		juke match {
			case Some(juke) =>
				val json = ("search_type" -> "juke", "search_query" -> juke_id, "result" -> juke)
				write(json)
			case None =>
				val json = ("search_type" -> "juke", "search_query" -> juke_id, "result" -> "None")
				write(json)
		}
	}

	def lookup_self_to_json(juke_name : String) : String = {
		val jukes : Option[Array[Juke]] = lookup_self(juke_name)
		implicit val formats = Serialization.formats(NoTypeHints)
		jukes match {
			case Some (juke_list) =>
				val json = ("search_type" -> "juke", "search_query" -> juke_name, "result" -> juke_list)
				write(json)
			case None =>
				val json = ("search_type" -> "juke", "search_query" -> juke_name, "result" -> "None")
				write(json)
		}
	}

	def lookup_self(juke_name : String) : Option[Array[Juke]] = {
		val all_jukes : Iterable[Vertex] = graph.getVertices("type", "juke").asScala
		if (all_jukes.size > 0) {
			val jukes : Iterable[Vertex] = all_jukes.filter(
				juke =>
					juke.getProperty("name") == juke_name
				)
			
			val final_jukes_list : Array[Juke] = jukes.map(juke =>
					lookup_self(juke.getProperty("id").asInstanceOf[Long]).get
					).toArray[Juke]
			Some(final_jukes_list)
		}
		else{
			log.debug("Couldn't find juke by name: " ++ juke_name)
			None
		}
	}


	def lookup_self(juke_id : Long) : Option[Juke] = {
		val juke : Vertex = graph.getVertex(juke_id)
		if (juke != null) {
			val location : Location = Location(juke.getProperty("lat").asInstanceOf[Double], juke.getProperty("long").asInstanceOf[Double])
			val title : String = juke.getProperty("title").asInstanceOf[String]
			val song_vertices : Iterable[Vertex] = juke.getEdges(Direction.OUT, "contains").asScala.map(
					edge =>
						edge.getVertex(Direction.OUT)
				)
			val songs : Array[Song] = song_vertices.map(
					song =>
						Song(song.getProperty("title").asInstanceOf[String], song.getProperty("id").asInstanceOf[Long], song.getProperty("origin").asInstanceOf[String])
				).toArray[Song]
			//Date is stored as a long (compatibility across languages). Convert to datetime
			val start_date_time : DateTime = new DateTime(juke.getProperty("start_time").asInstanceOf[Long])
			val end_date_time : DateTime = new DateTime(juke.getProperty("end_time").asInstanceOf[Long])
			Some(Juke(songs, title, juke_id, location, start_date_time, end_date_time))
		}
		else{
			log.debug("Couldn't find juke")
			return None
		}
	}
	
	def receive = {
		case ("get_info", juke_id : Long) =>
			sender ! lookup_self(juke_id)
		case ("get_info", juke_name : String) =>
			sender ! lookup_self(juke_name)
	}
}
