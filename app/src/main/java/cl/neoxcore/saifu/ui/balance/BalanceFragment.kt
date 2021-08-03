package cl.neoxcore.saifu.ui.balance

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cl.neoxcore.saifu.R.string
import cl.neoxcore.saifu.databinding.FragmentBalanceBinding
import cl.neoxcore.saifu.presentation.BalanceViewModel
import cl.neoxcore.saifu.presentation.balance.BalanceUIntent
import cl.neoxcore.saifu.presentation.balance.BalanceUIntent.LoadBalanceUIntent
import cl.neoxcore.saifu.presentation.balance.BalanceUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.DisplayBalanceUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.ErrorWithCacheUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.model.UiBalance
import cl.neoxcore.saifu.presentation.mvi.MviUi
import cl.neoxcore.saifu.ui.address.AddressFragment
import cl.neoxcore.saifu.ui.navigator.Navigator
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
@Suppress("TooManyFunctions")
internal class BalanceFragment : Fragment(), MviUi<BalanceUIntent, BalanceUiState> {
    private var binding: FragmentBalanceBinding? = null

    @Inject
    lateinit var navigator: Navigator

    private val viewModel by viewModels<BalanceViewModel>()

    private val userIntents: MutableSharedFlow<BalanceUIntent> = MutableSharedFlow()

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
            binding = FragmentBalanceBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            transactionButton.setOnClickListener {
                navigator.goToTransactions(root)
            }
        }
    }

    private fun subscribeToProcessIntentsAndObserveStates() {
        viewModel.run {
            viewModel.processUserIntentsAndObserveUiStates(userIntents())
                .onEach { renderUiStates(it) }.launchIn(lifecycleScope)
        }
    }

    override fun userIntents(): Flow<BalanceUIntent> = merge(
        initialUserIntent(),
        userIntents.asSharedFlow()
    )

    private fun initialUserIntent(): Flow<BalanceUIntent> = flow {
        emit(LoadBalanceUIntent)
    }

    override fun renderUiStates(uiState: BalanceUiState) {
        when (uiState) {
            LoadingUiState -> showLoading()
            is ErrorUiState -> showError(uiState.error)
            is DisplayBalanceUiState -> showBalance(uiState.balance)
            is ErrorWithCacheUiState -> showErrorWithBalance(uiState.balance)
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
        binding?.apply {
            Snackbar.make(
                contentView,
                "Error Critico: ${error.localizedMessage}",
                Snackbar.LENGTH_INDEFINITE
            ).setAction(string.retry) {
                viewLifecycleOwner.lifecycleScope.launch {
                    userIntents.emit(LoadBalanceUIntent)
                }
            }.show()
        }
        hideLoading()
    }

    private fun showBalance(balance: UiBalance) {
        binding?.apply {
            balanceText.text = balance.balance.toString()
            finalBalanceText.text = balance.finalBalance.toString()
            unconfirmedBalanceText.text = balance.unconfirmedBalance.toString()
            addressText.text = balance.address
            addressImage.setImageBitmap(generateQr(balance.address))
        }
        hideLoading()
    }

    private fun showErrorWithBalance(balance: UiBalance) {
        binding?.apply {
            balanceText.text = balance.balance.toString()
            finalBalanceText.text = balance.finalBalance.toString()
            unconfirmedBalanceText.text = balance.unconfirmedBalance.toString()
            addressText.text = balance.address
            addressImage.setImageBitmap(generateQr(balance.address))
            Snackbar.make(
                contentView,
                getString(string.error_update),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(string.retry) {
                viewLifecycleOwner.lifecycleScope.launch {
                    userIntents.emit(LoadBalanceUIntent)
                }
            }.show()
        }
        hideLoading()
    }

    private fun generateQr(address: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(
            address, BarcodeFormat.QR_CODE,
            AddressFragment.QR_SIZE,
            AddressFragment.QR_SIZE
        )
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    private fun hideLoading() {
        binding?.loadingView?.isGone = true
        binding?.contentView?.isGone = false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
