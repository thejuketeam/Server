/*Define what a user is. Basically, we have a class that contains
	a generic user definition that all users (premium, basic) should have.
 */
package com.juke.models

import akka.actor.{Actor, ActorLogging ,ActorRef, ActorSystem, Props}
import com.thinkaurelius.titan.core.{TitanGraph, TitanVertex, TitanEdge, TitanFactory};
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import com.tinkerpop.blueprints.{Edge, Vertex, Graph, Direction};
import com.tinkerpop.frames.{FramedTransactionalGraph, FramedGraphFactory}
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule
import com.tinkerpop.blueprints.util.wrappers.event.EventGraph
import scala.collection.JavaConverters._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{ read, write, writePretty }
import org.json4s.native.Serialization
import com.github.nscala_time.time.Imports._

trait GraphActor[T]{
	def add(obj : T)
	def update(obj : T)
	def delete(obj : T)
	def search(obj_id : Long) : Option[T]
	def search(obj_name : String) : Option[Array[T]]
}

class UserGraphActor extends DatabaseActor with akka.actor.ActorLogging with GraphActor[User]{
	import context.system
	//Actor stuff
	def receive = {
		case ("search", song_id : Long) =>
			sender ! search(song_id)
		case ("search", song_name : String) =>
			sender ! search(song_name)
		case ("add", user : User) =>
			add(user)
		case ("update", user : User) =>
			update(user)
		case("delete", user : User) =>
			delete(user)
	}
	
	def add(user : User) = {
		val new_user_vertex : Vertex = graph.addVertex(null)
		new_user_vertex.setProperty("name", user.name)
		new_user_vertex.setProperty("user_id", user.user_id)
		new_user_vertex.setProperty("id", user.id)
		new_user_vertex.setProperty("account_type", user.account_type)
		new_user_vertex.setProperty("like_count", user.like_count)
		graph.commit
	}
	def update(user : User) = {
		val user_vertex : Vertex = graph.getVertex(user.id)
		user_vertex.setProperty("name", user.name)
		user_vertex.setProperty("user_id", user.user_id)
		user_vertex.setProperty("id", user.id)
		user_vertex.setProperty("account_type", user.account_type)
		user_vertex.setProperty("like_count", user.like_count)
		graph.commit
	}
	def delete(user : User) = {
		val user_vertex : Vertex = graph.getVertex(user.id)
		graph.removeVertex(user_vertex)
		graph.commit
	}
	def search(user_name : String) : Option[Array[User]] = {
		val all_users : Iterable[Vertex] = graph.getVertices("type", "user").asScala
		if (all_users.size > 0){
			val users : Iterable[Vertex] = all_users.filter(
					user =>
						user.getProperty("name") == user_name
				)
			val final_user_list : Array[User] = users.map(
				user =>
					search_by_id(user.getProperty("user_id").asInstanceOf[String]).get
				).toArray[User]
			Some(final_user_list)
		}
		else{
			log.debug("No users with name: " ++ user_name ++ " found")
			None
		}
	}
	//Implement this
	def search(id : Long) : Option[User] = {
		val person : Vertex = graph.getVertex(id)
		if (person != null){
			//Get person from database, return in case class
			val name : String = person.getProperty("name").asInstanceOf[String]
			val id : Long = person.getProperty("id").asInstanceOf[Long]
			val like_count : Int = person.getProperty("like_count").asInstanceOf[Int]
			//get account type
			val account_type : String = person.getProperty("account_type").asInstanceOf[String]
			val user_id : String = person.getProperty("user_id").asInstanceOf[String]
			Some(User(name, user_id, id, account_type, like_count))
		}
		else{
			log.debug(s"User with id: $id not found")
			return None
		}
	}

	def search_by_id(user_id : String) : Option[User] = {
		val person : Vertex = graph.getVertices("user_id", user_id).iterator.next
		if (person != null){
			//Get person from database, return in case class
			val name : String = person.getProperty("name").asInstanceOf[String]
			val id : Long = person.getProperty("id").asInstanceOf[Long]
			val like_count : Int = person.getProperty("like_count").asInstanceOf[Int]
			//get account type
			val account_type : String = person.getProperty("account_type").asInstanceOf[String]
			Some(User(name, user_id, id, account_type, like_count))
		}
		else{
			log.debug("User with user id: " ++ user_id ++ " not found")
			return None
		}
	}

}

class SongGraphActor extends DatabaseActor with akka.actor.ActorLogging with GraphActor[Song]{
	import context.system

	//Actor stuff
	def receive = {
		case ("search", song_id : Long) =>
			sender ! search(song_id)
		case ("search", song_name : String) =>
			sender ! search(song_name)
		case ("add", song : Song) =>
			add(song)
		case ("update", song : Song) =>
			update(song)
		case("delete", song : Song) =>
			delete(song)
	}
	
	def add(song : Song) = {
		val new_song_vertex : Vertex = graph.addVertex(null)
		new_song_vertex.setProperty("title", song.title)
		new_song_vertex.setProperty("id", song.id)
		new_song_vertex.setProperty("origin", song.origin)
		graph.commit
	}
	def update(song : Song) = {
		val song_vertex : Vertex = graph.getVertex(song.id)
		song_vertex.setProperty("title", song.title)
		song_vertex.setProperty("id", song.id)
		song_vertex.setProperty("origin", song.origin)
		graph.commit
	}
	def delete(song : Song) = {
		val song_vertex : Vertex = graph.getVertex(song.id)
		graph.removeVertex(song_vertex)
		graph.commit
	}

	def search(song_name : String) : Option[Array[Song]] = {
		val all_songs : Iterable[Vertex] = graph.getVertices("type", "song").asScala
		if (all_songs.size > 0) {
			val songs : Iterable[Vertex] = all_songs.filter(
				song =>
					song.getProperty("title") == song_name
				)
			
			val final_songs_list : Array[Song] = songs.map(song =>
					search(song.getProperty("id").asInstanceOf[Long]).get
					).toArray[Song]
			Some(final_songs_list)
		}
		else{
			log.debug("Couldn't find song by name: " ++ song_name)
			None
		}
	}

	def search(song_id : Long) : Option[Song] = {
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
}


class JukeGraphActor extends DatabaseActor with akka.actor.ActorLogging with GraphActor[Juke]{
	import context.system
	
	//Actor stuff
	def receive = {
		case ("search", juke_id : Long) =>
			sender ! search(juke_id)
		case ("search", juke_name : String) =>
			sender ! search(juke_name)
		case ("add", juke : Juke) =>
			add(juke)
		case ("update", juke : Juke) =>
			update(juke)
		case("delete", juke : Juke) =>
			delete(juke)
	}

	def add(juke : Juke) = {
		val new_juke_vertex : Vertex = graph.addVertex(null)
		new_juke_vertex.setProperty("title", juke.title)
		new_juke_vertex.setProperty("id", juke.id)
		new_juke_vertex.setProperty("long", juke.location.long)
		new_juke_vertex.setProperty("lat", juke.location.lat)
		new_juke_vertex.setProperty("start_time", juke.start_time.getMillis)
		new_juke_vertex.setProperty("end_time", juke.end_time.getMillis)
		graph.commit
	}
	def update(juke : Juke) = {
		val juke_vertex : Vertex = graph.getVertex(juke.id)
		juke_vertex.setProperty("title", juke.title)
		juke_vertex.setProperty("long", juke.location.long)
		juke_vertex.setProperty("lat", juke.location.lat)
		juke_vertex.setProperty("start_time", juke.start_time.getMillis)
		juke_vertex.setProperty("end_time", juke.end_time.getMillis)
		graph.commit
	}
	def delete(juke : Juke) = {
		val juke_vertex : Vertex = graph.getVertex(juke.id)
		graph.removeVertex(juke_vertex)
		graph.commit
	}

	def search(juke_name : String) : Option[Array[Juke]] = {
		val all_jukes : Iterable[Vertex] = graph.getVertices("type", "juke").asScala
		if (all_jukes.size > 0) {
			val jukes : Iterable[Vertex] = all_jukes.filter(
				juke =>
					juke.getProperty("name") == juke_name
				)
			
			val final_jukes_list : Array[Juke] = jukes.map(juke =>
					search(juke.getProperty("id").asInstanceOf[Long]).get
					).toArray[Juke]
			Some(final_jukes_list)
		}
		else{
			log.debug("Couldn't find juke by name: " ++ juke_name)
			None
		}
	}


	def search(juke_id : Long) : Option[Juke] = {
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
}
