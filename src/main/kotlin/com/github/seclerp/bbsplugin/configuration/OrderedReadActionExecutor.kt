package com.github.seclerp.bbsplugin.configuration

import com.intellij.openapi.application.readAction
import fleet.util.logging.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class OrderedReadActionExecutor(private val scope: CoroutineScope) {
    private val updateChannel = Channel<() -> Unit>(Channel.Factory.UNLIMITED)

    init {
        scope.coroutineContext.job.invokeOnCompletion {
            updateChannel.close()
        }
        scope.launch(Dispatchers.Default) {
            updateChannel.receiveAsFlow().collect { task ->
                try {
                    readAction {
                        task()
                    }
                } catch (e: Exception) {
                    logger<BbsFileWatcherActivity>().error(e)
                }
            }
        }
    }

    fun enqueue(task: () -> Unit) =
        updateChannel.trySend(task)
}