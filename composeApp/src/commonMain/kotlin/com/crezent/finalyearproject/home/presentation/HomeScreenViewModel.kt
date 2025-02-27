package com.crezent.finalyearproject.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    private val baseAppRepo: BaseAppRepo,
    private val preference: SharedPreference
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())

    private val _homeScreenEvent = Channel<HomeScreenEvent>()
    val homeScreenEvent = _homeScreenEvent.receiveAsFlow()

    val homeScreenState = _homeScreenState
        .onStart {
            getAuthenticatedUser()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            _homeScreenState.value

        )

    private fun getAuthenticatedUser() {
        _homeScreenState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {

            val request = baseAppRepo.getAndCacheAuthenticatedUser()
            withContext(Dispatchers.Main) {
                println("RESULT IS $request")
                if (request is Result.Error) {
                    _homeScreenState.update {
                        it.copy(lastBalance = preference.balance?.toDouble())
                    }
                    return@withContext
                }

                val user = (request as Result.Success).data
//
                _homeScreenState.value = homeScreenState.value.copy(
                    user = user
                )


            }


        }
    }
}