package com.juke.views.rest

import xitrum.annotation.GET
import xitrum.ActorAction
import akka.actor.{Props, Actor, ActorRef}
import com.juke.models.UserActor

@GET("user/search/:user_name")
class UserSearch extends ActorAction{
	import context.system
	def execute(){
		val user_name : String = param[String]("user_name")
		val user_search_actor = system.actorOf(Props[UserActor])
	//	user_search_actor ? ("get_info", "user_name", user_name)
	}
}

@GET("user/:user_id")
class UserView extends ActorAction{
	import context.system
	def execute(){
		//Get id out of RESTapi
		val user_id : String = param[String]("user_id")
		//Look it up in server database
		val user_actor = system.actorOf(Props[UserActor])
	//	user_actor ? ("get_info", user_id)
	}
}
