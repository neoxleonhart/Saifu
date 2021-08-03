package cl.neoxcore.saifu.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cl.neoxcore.saifu.databinding.FragmentTransactionBinding
import cl.neoxcore.saifu.presentation.TransactionViewModel
import cl.neoxcore.saifu.presentation.model.UiTransaction
import cl.neoxcore.saifu.presentation.mvi.MviUi
import cl.neoxcore.saifu.presentation.transaction.TransactionUIntent
import cl.neoxcore.saifu.presentation.transaction.TransactionUIntent.LoadTransactionUIntent
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.DisplayTransactionUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.ErrorWithCacheUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.LoadingUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
@Suppress("TooManyFunctions")
internal class TransactionFragment : Fragment(), MviUi<TransactionUIntent, TransactionUiState> {

    private var binding: FragmentTransactionBinding? = null

    private val viewModel by viewModels<TransactionViewModel>()

    private val userIntents: MutableSharedFlow<TransactionUIntent> = MutableSharedFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToProcessIntentsAndObserveStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (binding == null) {
            binding = FragmentTransactionBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    private fun subscribeToProcessIntentsAndObserveStates() {
        viewModel.run {
            viewModel.processUserIntentsAndObserveUiStates(userIntents())
                .onEach { renderUiStates(it) }.launchIn(lifecycleScope)
        }
    }

    override fun userIntents(): Flow<TransactionUIntent> = merge(
        initialUserIntent(),
        userIntents.asSharedFlow()
    )

    private fun initialUserIntent(): Flow<TransactionUIntent> = flow {
        emit(LoadTransactionUIntent)
    }

    override fun renderUiStates(uiState: TransactionUiState) {
        when (uiState) {
            LoadingUiState -> showLoading()
            is ErrorUiState -> showError(uiState.error)
            is DisplayTransactionUiState -> showTransactions(uiState.transactions)
            is ErrorWithCacheUiState -> showErrorWithTransactions(
                uiState.error,
                uiState.transactions
            )
            else -> {
            }
        }
    }

    private fun showLoading() {
        binding?.apply {
            loadingView.isVisible = true
        }
    }

    private fun showError(error: Throwable) {
        hideLoading()
    }

    private fun showTransactions(transactions: List<UiTransaction>) {
        hideLoading()
    }

    private fun showErrorWithTransactions(error: Throwable, transactions: List<UiTransaction>) {
        hideLoading()
    }

    private fun hideLoading() {
        binding?.loadingView?.isGone = true
        binding?.recyclerView?.isVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
