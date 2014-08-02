package com.juke.views

import xitrum.annotation.{Error404, Error500}
import xitrum.FutureAction

@Error404
class NotFoundError extends FutureAction {
  def execute() {
    respondView()
  }
}

@Error500
class ServerError extends FutureAction {
  def execute() {
    respondView()
  }
}
