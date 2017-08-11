package com.nexflare.kloh.Model

/**
 * Created by nexflare on 12/08/17.
 */
data class EventResponse(val response:Results)

data class Results(val results:ArrayList<Result>)

data class Result(val title:String,val activityId:String,val imageUrl:String,
                  val ownerProfileImageUrl:String,val location:EventLocation,
                  val activityTime: ActivityTime,val description: String)

data class EventLocation(val name:String)

data class ActivityTime(val timeTuples: ArrayList<String>)
