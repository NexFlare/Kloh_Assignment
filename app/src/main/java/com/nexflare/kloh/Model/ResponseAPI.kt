package com.nexflare.kloh.Model

/**
 * Created by nexflare on 12/08/17.
 */
data class ResponseAPI(val response:Results)

data class Results(val results:ArrayList<Result>)

data class Result(var shareUrl:String)