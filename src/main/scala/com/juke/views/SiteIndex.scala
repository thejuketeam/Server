package com.juke.views

import xitrum.annotation.GET
import xitrum.FutureAction

@GET("")
class SiteIndex extends FutureAction{
  def execute() {
		respondView()
	}
}
