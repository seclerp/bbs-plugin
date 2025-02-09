package com.github.seclerp.bbsplugin.configuration

import fleet.util.logging.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class OrderedTaskExecutor(private val scope: CoroutineScope) {
    private val updateChannel = Channel<() -> Unit>(Channel.Factory.UNLIMITED)

    init {
        scope.coroutineContext.job.invokeOnCompletion {
            updateChannel.close()
        }
        scope.launch(Dispatchers.Default) {
            updateChannel.receiveAsFlow().collect { task ->
                try {
                    task()
                } catch (e: Exception) {
                    logger<BbsFileWatcherActivity>().error(e)
                }
            }
        }
    }

    fun enqueue(task: () -> Unit) = scope.launch(Dispatchers.Default) {
        updateChannel.trySend(task)
    }
}