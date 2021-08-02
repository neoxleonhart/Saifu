package cl.neoxcore.saifu.ui.address

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cl.neoxcore.saifu.databinding.FragmentAddressBinding
import cl.neoxcore.saifu.presentation.AddressViewModel
import cl.neoxcore.saifu.presentation.address.AddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DisplayAddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.SaveUiState
import cl.neoxcore.saifu.presentation.mvi.MviUi
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class AddressFragment : Fragment(), MviUi<AddressUIntent, AddressUiState> {
    private var binding: FragmentAddressBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddressViewModel by viewModels { viewModelFactory }
    private val userIntents: MutableSharedFlow<AddressUIntent> = MutableSharedFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
        subscribeToProcessIntentsAndObserveStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (binding == null) {
            binding = FragmentAddressBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    private fun setupInjection() {
        TODO()
    }

    private fun subscribeToProcessIntentsAndObserveStates() {
        viewModel.run {
            viewModel.processUserIntentsAndObserveUiStates(userIntents())
                .onEach { renderUiStates(it) }.launchIn(lifecycleScope)
        }
    }

    override fun userIntents(): Flow<AddressUIntent> = userIntents.asSharedFlow()

    override fun renderUiStates(uiState: AddressUiState) {
        when (uiState) {
            DefaultUiState -> showNoAddress()
            is DisplayAddressUiState -> showAddress(uiState.address)
            is ErrorUiState -> showError(uiState.error)
            LoadingUiState -> showLoading()
            SaveUiState -> goToNextScreen()
        }
    }

    private fun goToNextScreen() {
        TODO("Not yet implemented")
    }

    private fun showLoading() {
        binding?.apply {
            loadingView.visibility = View.VISIBLE
        }
    }

    private fun showError(error: Throwable) {
        binding?.apply {
            loadingView.visibility = View.GONE
        }
    }

    private fun showAddress(address: String) {
        binding?.apply {
            loadingView.visibility = View.GONE
            addressText.text = address
            addressImage.setImageBitmap(generateQr(address))
        }
    }

    private fun generateQr(address: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(address, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE)
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

    private fun showNoAddress() {
        binding?.apply {
            saveAddressButton.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val QR_SIZE = 512
    }
}
