package cl.neoxcore.saifu.ui.address

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cl.neoxcore.saifu.R
import cl.neoxcore.saifu.databinding.FragmentAddressBinding
import cl.neoxcore.saifu.presentation.AddressViewModel
import cl.neoxcore.saifu.presentation.address.AddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.GenerateNewAddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.SaveAddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DisplayAddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorGenerateUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorSaveUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.SaveUiState
import cl.neoxcore.saifu.presentation.mvi.MviUi
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
@Suppress("TooManyFunctions")
class AddressFragment : Fragment(), MviUi<AddressUIntent, AddressUiState> {
    private var binding: FragmentAddressBinding? = null

    private val viewModel by viewModels<AddressViewModel>()

    private val userIntents: MutableSharedFlow<AddressUIntent> = MutableSharedFlow()

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
            binding = FragmentAddressBinding.inflate(inflater, container, false)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            generateAddressButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    userIntents.emit(GenerateNewAddressUIntent)
                }
            }
            saveAddressButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    userIntents.emit(SaveAddressUIntent(addressText.text.toString()))
                }
            }
        }
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
            is ErrorGenerateUiState -> showGenerateError(uiState.error)
            is ErrorSaveUiState -> showSaveError(uiState.error)
            LoadingUiState -> showLoading()
            SaveUiState -> goToNextScreen()
        }
    }

    private fun goToNextScreen() {
        Toast.makeText(context, "nueva pantalla", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding?.apply {
            loadingView.visibility = View.VISIBLE
        }
    }

    private fun showGenerateError(error: Throwable) {
        binding?.apply {
            saveAddressButton.isEnabled = addressText.text.toString().isNotEmpty()
            loadingView.visibility = View.GONE
            Snackbar.make(
                contentView,
                "Error: ${error.localizedMessage}",
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.retry) {
                viewLifecycleOwner.lifecycleScope.launch {
                        userIntents.emit(GenerateNewAddressUIntent)
                }
            }.show()
        }
    }

    private fun showSaveError(error: Throwable) {
        binding?.apply {
            loadingView.visibility = View.GONE
            Snackbar.make(
                contentView,
                "Error: ${error.localizedMessage}",
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.retry) {
                viewLifecycleOwner.lifecycleScope.launch {
                        userIntents.emit(SaveAddressUIntent(addressText.text.toString()))
                }
            }.show()
        }
    }

    private fun showAddress(address: String) {
        binding?.apply {
            loadingView.visibility = View.GONE
            addressText.text = address
            addressImage.setImageBitmap(generateQr(address))
            saveAddressButton.isEnabled = true
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
