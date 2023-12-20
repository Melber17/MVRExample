package com.melber17.project999.subscription

import com.melber17.project999.core.ClearRepresentative
import com.melber17.project999.core.HandleDeath
import com.melber17.project999.core.Representative
import com.melber17.project999.core.RunAsync
import com.melber17.project999.core.UiObserver
import com.melber17.project999.dashboard.DashboardScreen
import com.melber17.project999.main.Navigation
import com.melber17.project999.main.Screen
import com.melber17.project999.subscription.domain.SubscriptionInteractor
import com.melber17.project999.subscription.presentation.EmptySubscriptionObserver
import com.melber17.project999.subscription.presentation.SaveAndRestoreSubscriptionUiState
import com.melber17.project999.subscription.presentation.SubscriptionObservable
import com.melber17.project999.subscription.presentation.SubscriptionObserver
import com.melber17.project999.subscription.presentation.SubscriptionRepresentative
import com.melber17.project999.subscription.presentation.SubscriptionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative
    private lateinit var observable: FakeObservable
    private lateinit var clear: FakeClear
    private lateinit var interactor: FakeInteractor
    private lateinit var navigation: FakeNavigation
    private lateinit var handleDeath: FakeHandleDeath
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun setup() {
        observable = FakeObservable.Base()
        clear = FakeClear.Base()
        interactor = FakeInteractor.Base()
        navigation = FakeNavigation.Base()
        handleDeath = FakeHandleDeath.Base()
        runAsync = FakeRunAsync.Base()

        representative = SubscriptionRepresentative.Base(
            handleDeath,
            observable,
            clear,
            interactor,
            navigation,
            runAsync,
        )

    }

    @Test
    fun main_scenario() {
        val restoreState = FakeSaveAndRestore.Base()
        representative.init(restoreState)
        handleDeath.checkFirstOpening(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)

        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)

        representative.observed()
        observable.checkClearCalled()

        representative.finish()
        clear.checkClearCalledWith(SubscriptionRepresentative::class.java)
        navigation.checkUpdated(DashboardScreen)

        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
    }

    @Test
    fun test_save_and_restore() {
        val restoreState = FakeSaveAndRestore.Base()
        representative.init(restoreState)
        handleDeath.checkFirstOpening(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateCalledCount(1)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(restoreState)

        representative.init(restoreState)
        handleDeath.checkFirstOpening(1)
        observable.checkUpdateCalledCount(2)
    }

    @Test
    fun test_death_after_loading() {
        val restoreState = FakeSaveAndRestore.Base()
        representative.init(restoreState)
        handleDeath.checkFirstOpening(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(restoreState)


        // death happening here
        setup()

        representative.init(restoreState)
        handleDeath.checkFirstOpening(0)
        observable.checkUiState(SubscriptionUiState.Empty)
        observable.checkUpdateCalledCount(0)
        interactor.checkSubscribeCalledTimes(1)
    }

    @Test
    fun test_death_after_success() {
        val restoreState = FakeSaveAndRestore.Base()
        representative.init(restoreState)
        handleDeath.checkFirstOpening(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(restoreState)


        setup()

        representative.init(restoreState)
        handleDeath.checkFirstOpening(0)
        observable.checkUiState(SubscriptionUiState.Success)
        observable.checkUpdateCalledCount(1)
        interactor.checkSubscribeCalledTimes(0)
    }

    @Test
    fun test_death_after_success_observed() {
        val restoreState = FakeSaveAndRestore.Base()
        representative.init(restoreState)
        handleDeath.checkFirstOpening(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)
        representative.observed()
        observable.checkClearCalled()
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(restoreState)


        // death happening here
        setup()
        representative.init(restoreState)
        handleDeath.checkFirstOpening(0)
        observable.checkUiState(SubscriptionUiState.Empty)
        observable.checkUpdateCalledCount(0)
        interactor.checkSubscribeCalledTimes(0)
    }

}

private interface FakeSaveAndRestore : SaveAndRestoreSubscriptionUiState.Mutable {
    class Base() : FakeSaveAndRestore {
        private var state: SubscriptionUiState? = null

        override fun save(data: SubscriptionUiState) {
            state = data
        }

        override fun restore(): SubscriptionUiState {
            return state!!
        }

        override fun isEmpty(): Boolean = state == null
    }
}

private interface FakeNavigation : Navigation.Update {
    fun checkUpdated(screen: Screen)

    class Base() : FakeNavigation {
        private var updateCalledWithScreen: Screen = Screen.Empty
        override fun checkUpdated(screen: Screen) {
            assertEquals(screen, updateCalledWithScreen)
        }

        override fun update(data: Screen) {
            updateCalledWithScreen = data
        }
    }
}


private interface FakeInteractor : SubscriptionInteractor {
    fun checkSubscribeCalledTimes(times: Int)


    class Base() : FakeInteractor {
        private var subscribeCalledCount = 0
        override fun checkSubscribeCalledTimes(times: Int) {
            assertEquals(times, subscribeCalledCount)
        }

        override suspend fun subscribe() {
            subscribeCalledCount++
        }

    }
}

private interface FakeRunAsync : RunAsync {
    fun pingResult()
    class Base : FakeRunAsync {
        private var cachedBlock: (Any) -> Unit = {}
        private var cached: Any = Unit
        override fun pingResult() {
            cachedBlock.invoke(cached)
        }

        override fun <T : Any> runAsync(
            scope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) = runBlocking {
            cached = backgroundBlock.invoke()
            cachedBlock = uiBlock as (Any) -> Unit
        }

        override fun clear() = Unit

    }
}

private interface FakeClear : ClearRepresentative {
    fun checkClearCalledWith(clasz: Class<out Representative<*>>)
    class Base : FakeClear {
        private var clearCalledClass: Class<out Representative<*>>? = null

        override fun checkClearCalledWith(clasz: Class<out Representative<*>>) {
            assertEquals(clasz, clearCalledClass)
        }

        override fun clear(clasz: Class<out Representative<*>>) {
            clearCalledClass = clasz
        }

    }
}

private interface FakeObservable : SubscriptionObservable {
    fun checkClearCalled()
    fun checkUpdateCalledCount(times: Int)
    fun checkUiState(uiState: SubscriptionUiState)
    fun checkUpdateObserverCalled(observer: SubscriptionObserver)


    class Base : FakeObservable {
        private var clearCalled = false
        private var cache: SubscriptionUiState = SubscriptionUiState.Empty
        private var updateCalledCount = 0

        override fun checkClearCalled() {
            assertEquals(true, clearCalled)
            clearCalled = false
        }

        override fun checkUpdateCalledCount(times: Int) {
            assertEquals(times, updateCalledCount)
        }


        override fun clear() {
            cache = SubscriptionUiState.Empty
            clearCalled = true
        }


        override fun update(data: SubscriptionUiState) {
            cache = data
            updateCalledCount++;
        }

        override fun checkUiState(uiState: SubscriptionUiState) {
            assertEquals(uiState, cache)
        }

        override fun checkUpdateObserverCalled(observer: SubscriptionObserver) {
            assertEquals(observerCached, observer)
        }

        private var observerCached: UiObserver<SubscriptionUiState> =
            object : SubscriptionObserver {
                override fun update(data: SubscriptionUiState) = Unit
            }

        override fun updateObserver(uiObserver: UiObserver<SubscriptionUiState>) {
            observerCached = uiObserver
        }

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }

    }
}

private interface FakeHandleDeath : HandleDeath {
    fun checkFirstOpening(times: Int)

    class Base() : FakeHandleDeath {
        private var deathHappened = true
        private var firstOpeningCalledTimes = 0

        override fun checkFirstOpening(times: Int) {
            assertEquals(times, firstOpeningCalledTimes)
        }

        override fun firstOpening() {
            firstOpeningCalledTimes++
        }

        override fun isDeathHappened(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }
    }

}