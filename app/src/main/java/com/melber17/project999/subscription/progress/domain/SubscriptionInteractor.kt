package com.melber17.project999.subscription.progress.domain

interface SubscriptionInteractor {
    suspend fun subscribe(): SubscriptionResult

    suspend fun subscribeInternal(): SubscriptionResult
    class Base(
        private val repository: SubscriptionRepository
    ) : SubscriptionInteractor {

        override suspend fun subscribe(): SubscriptionResult {
            if (repository.isPremiumUser()) {
                return SubscriptionResult.Success
            } else {
                repository.subscribe()
                return SubscriptionResult.NoDataYet
            }
        }

        override suspend fun subscribeInternal(): SubscriptionResult {
            repository.subscribeInternal()
            return SubscriptionResult.Success
        }
    }
}

interface SubscriptionResult {
    interface Mapper {
        fun mapSuccess()
    }
    fun map(mapper: Mapper)
    object Success : SubscriptionResult {
        override fun map(mapper: Mapper) = mapper.mapSuccess()
    }

    object NoDataYet : SubscriptionResult {
        override fun map(mapper: Mapper) = Unit
    }

}