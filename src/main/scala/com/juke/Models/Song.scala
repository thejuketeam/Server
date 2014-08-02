package com.juke.models

import akka.actor.{Actor, ActorLogging ,ActorRef, ActorSystem, Props}
import com.github.nscala_time.time.Imports._
import com.tinkerpop.blueprints.{Edge, Vertex, Graph, Direction}
import scala.collection.JavaConverters._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{ read, write, writePretty }
import org.json4s.native.Serialization


case class Song(title : String, id : Long, origin : String)

class SongActor extends DatabaseActor with akka.actor.ActorLogging {
	import context.system

	def lookup_self_to_json(song_id : Long) : String = {
		val song : Option[Song] = lookup_self(song_id)
		implicit val formats = Serialization.formats(NoTypeHints)
		song match {
			case Some(song) =>
				val json = ("search_type" -> "song", "search_query" -> song_id, "result" -> song)
				write(json)
			case None =>
				val json = ("search_type" -> "song", "search_query" -> song_id, "result" -> "None")
				write(json)
		}
	}

	def lookup_self_to_json(song_name : String) : String = {
		val songs : Option[Array[Song]] = lookup_self(song_name)
		implicit val formats = Serialization.formats(NoTypeHints)
		songs match {
			case Some (song_list) =>
				val json = ("search_type" -> "song", "search_query" -> song_name, "result" -> song_list)
				write(json)
			case None =>
				val json = ("search_type" -> "song", "search_query" -> song_name, "result" -> "None")
				write(json)
		}
	}

	def lookup_self(song_name : String) : Option[Array[Song]] = {
		val all_songs : Iterable[Vertex] = graph.getVertices("type", "song").asScala
		if (all_songs.size > 0) {
			val songs : Iterable[Vertex] = all_songs.filter(
				song =>
					song.getProperty("title") == song_name
				)
			
			val final_songs_list : Array[Song] = songs.map(song =>
					lookup_self(song.getProperty("id").asInstanceOf[Long]).get
					).toArray[Song]
			Some(final_songs_list)
		}
		else{
			log.debug("Couldn't find song by name: " ++ song_name)
			None
		}
	}


	def lookup_self(song_id : Long) : Option[Song] = {
		val song : Vertex = graph.getVertex(song_id)
		if (song != null) {
			val title : String = song.getProperty("title").asInstanceOf[String]
			val song_id : Long = song.getProperty("id").asInstanceOf[Long]
			val origin : String = song.getProperty("origin").asInstanceOf[String]
			//Date is stored as a long (compatibility across languages). Convert to datetime
			Some(Song(title, song_id, origin))
		}
		else{
			log.debug("Couldn't find the song")
			return None
		}
	}
	
	def receive = {
		case ("get_info", song_id : Long) =>
			sender ! lookup_self(song_id)
		case ("get_info", song_name : String) =>
			sender ! lookup_self(song_name)
	}
}
