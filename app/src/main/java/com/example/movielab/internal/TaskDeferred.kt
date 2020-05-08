package com.example.movielab.internal

import kotlinx.coroutines.Deferred
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred

fun <T> Task<T>.asDeferred():Deferred<T>{
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener { result ->
        deferred.complete(result)
    }

    this.addOnFailureListener{ exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred
}