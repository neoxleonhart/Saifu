package cl.neoxcore.saifu.ui.address

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cl.neoxcore.saifu.R.string
import cl.neoxcore.saifu.databinding.FragmentAddressBinding
import cl.neoxcore.saifu.presentation.AddressViewModel
import cl.neoxcore.saifu.presentation.address.AddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.GenerateNewAddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.GetAddressSavedUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.SaveAddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DisplayAddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorGenerateUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorSaveUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.InitializedUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.SaveUiState
import cl.neoxcore.saifu.presentation.mvi.MviUi
import cl.neoxcore.saifu.ui.navigator.Navigator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
class AddressFragment : Fragment(), MviUi<AddressUIntent, AddressUiState> {
    private var binding: FragmentAddressBinding? = null

    @Inject
    lateinit var navigator: Navigator

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
                showConfirmDialog()
            }
        }
    }

    private fun subscribeToProcessIntentsAndObserveStates() {
        viewModel.run {
            viewModel.processUserIntentsAndObserveUiStates(userIntents())
                .onEach { renderUiStates(it) }.launchIn(lifecycleScope)
        }
    }

    override fun userIntents(): Flow<AddressUIntent> = merge(
        initialUserIntent(),
        userIntents.asSharedFlow()
    )

    private fun initialUserIntent(): Flow<AddressUIntent> = flow {
        emit(GetAddressSavedUIntent)
    }

    override fun renderUiStates(uiState: AddressUiState) {
        when (uiState) {
            DefaultUiState -> showLoading()
            InitializedUiState -> showNoAddress()
            is DisplayAddressUiState -> showAddress(uiState.address)
            is ErrorGenerateUiState -> showGenerateError(uiState.error)
            is ErrorSaveUiState -> showSaveError(uiState.error)
            LoadingUiState -> showLoading()
            SaveUiState -> goToNextScreen()
        }
    }

    private fun goToNextScreen() {
        binding?.let {
            navigator.goToAddress(it.root)
        }
    }

    private fun showLoading() {
        binding?.apply {
            loadingView.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding?.loadingView?.isGone = true
        binding?.contentView?.isGone = false
    }

    private fun showGenerateError(error: Throwable) {
        error.printStackTrace()
        binding?.apply {
            saveAddressButton.isEnabled = addressText.text.toString().isNotEmpty()
            hideLoading()
            Snackbar.make(
                contentView,
                getString(string.server_error),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(string.retry) {
                viewLifecycleOwner.lifecycleScope.launch {
                    userIntents.emit(GenerateNewAddressUIntent)
                }
            }.show()
        }
    }

    private fun showSaveError(error: Throwable) {
        binding?.apply {
            hideLoading()
            Snackbar.make(
                contentView,
                "Error: ${error.localizedMessage}",
                Snackbar.LENGTH_INDEFINITE
            ).setAction(string.retry) {
                viewLifecycleOwner.lifecycleScope.launch {
                    userIntents.emit(SaveAddressUIntent(addressText.text.toString()))
                }
            }.show()
        }
    }

    private fun showAddress(address: String) {
        binding?.apply {
            addressText.text = address
            addressImage.setImageBitmap(generateQr(address))
            saveAddressButton.isEnabled = true
        }
        hideLoading()
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
        hideLoading()
    }

    private fun showConfirmDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(resources.getString(string.confirm_title))
                .setMessage(resources.getString(string.confirm_text))
                .setNegativeButton(resources.getString(string.cancel)) { _, _ ->
                }
                .setPositiveButton(resources.getString(string.accept)) { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        userIntents.emit(SaveAddressUIntent(binding?.addressText?.text.toString()))
                    }
                }.show()
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
