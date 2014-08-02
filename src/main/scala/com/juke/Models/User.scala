/*Define what a user is. Basically, we have a class that contains
	a generic user definition that all users (premium, basic) should have.
 */
package com.juke.models

import AccountType._
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

case class User(name : String, user_id : String, id : Long, account_type : String, like_count : Int)

class UserActor extends DatabaseActor with akka.actor.ActorLogging {
	import context.system
	
	def lookup_possible_selves_to_json(user_name : String) : String = {
		val possible_users : Option[Array[User]] = lookup_possible_selves(user_name)
		implicit val formats = Serialization.formats(NoTypeHints)
		possible_users match{
			case Some(array_users) =>
				//Send back JSON array indicating match
				val json = ("search_type" -> "user", "search_query" -> user_name,
					"result_list" -> array_users)
				write(json)
			case None =>
				//Send back Json indicating no match...
				val json = ("search_type" -> "user", "search_query" -> user_name, "result_list" -> "empty")
				write(json)
		}
	}

	def lookup_possible_selves(user_name : String) : Option[Array[User]] = {
		val all_users : Iterable[Vertex] = graph.getVertices("type", "user").asScala
		if (all_users.size > 0){
			val users : Iterable[Vertex] = all_users.filter(
					user =>
						user.getProperty("name") == user_name
				)
			val final_user_list : Array[User] = users.map(
				user =>
					lookup_self(user.getProperty("user_id").asInstanceOf[String]).get
				).toArray[User]
			Some(final_user_list)
		}
		else{
			log.debug("No users with name: " ++ user_name ++ " found")
			None
		}
	}
	
	def lookup_self_to_json(user_id : String) : String = {
		val possible_user : Option[User] = lookup_self(user_id)
		implicit val formats = Serialization.formats(NoTypeHints)
		possible_user match {
			case Some(user) =>
				val json = ("search_type" -> "user", "search_query" -> user_id, "result" -> user)
				write(json)
			case None =>
				val json = ("search_type" -> "user", "search_query" -> user_id, "result" -> "None")
				write(json)
		}
	}

	def lookup_self(user_id : String) : Option[User] = {
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

	def receive = {
		case ("get_info", "user_id", user_id : String) =>
			sender ! lookup_self(user_id)
		case ("get_info", "user_name", user_name : String) =>
			sender ! lookup_possible_selves(user_name)
	}


}
