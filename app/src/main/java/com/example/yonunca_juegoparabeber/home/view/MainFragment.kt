package com.example.yonunca_juegoparabeber.home.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.BaseApplication
import com.example.yonunca_juegoparabeber.base.BaseApplication.Companion.getApplicationContext
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.databinding.FragmentMainBinding
import com.example.yonunca_juegoparabeber.home.viewmodel.MainViewModel
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainFragment : BaseFragment() {
    private var _binding: FragmentMainBinding? = null
    private val viewModel: MainViewModel by viewModels()

    private val binding get() = _binding!!
    private var contextMenu: ContextMenu? = null

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainFragment"


    override fun setListeners() {
        binding.run {
            naughty.setOnClickListener {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                    goToNaughty()
                }
            }
            hot.setOnClickListener {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                    goToHot()
                }
            }
            community.setOnClickListener {


                goToCommunity()
            }
            online.setOnClickListener {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                    goToOnline()
                }
            }
            createGame.createGameButton.setOnClickListener {
                goToCreateGame()
            }
            createGame.searchGame.setOnClickListener {
                goToSearchGame()
            }
            createGame.close.setOnClickListener {
                closeCreateGame()
            }
            popUp.googleButton.setOnClickListener {
                viewModel.tryToLogin(signInLauncher)
            }
            popUp.close.setOnClickListener {
                closeOnlinePopUp()
            }
            settings.setOnClickListener {
                showDialog()
            }
            popUp.signUp.setOnClickListener {
                findNavController().navigate(R.id.mainFragment_to_signUpFragment)
            }
            popUp.signIn.setOnClickListener {
                findNavController().navigate(R.id.mainFragment_to_signUpFragment)
            }
        }
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner) {
            if (it.messageToShow != null) {
                showLoginError(it.messageToShow)
            }
            if (it.isOnline) {
                hideLoginButton()
                updateContextMenuTitle(it.userName)
            } else {
                hideCreateGameButton()
            }
            updateContextMenu(it.isOnline)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
//        registerForContextMenu(binding.settings)
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.main_settings, menu)
        contextMenu = menu
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.login -> {
                showLoginButton()
                true
            }
            R.id.sign_out -> {
                signOut()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        viewModel.onSignInResult(res)
    }

    private fun goToNaughty() {
        findNavController().navigate(R.id.mainFragment_to_offlineScreenFragment)
    }

    private fun goToHot() {
        findNavController().navigate(R.id.mainFragment_to_offlineHotScreenFragment)
    }

    private fun goToCommunity() {
        findNavController().navigate(R.id.mainFragment_to_communityFragment)
    }

    private fun goToSearchGame() {
        findNavController().navigate(R.id.mainFragment_to_playerSelectionFragment)
    }

    private fun goToCreateGame() {
        findNavController().navigate(R.id.mainFragment_to_createGameFragment)
    }

    private fun updateContextMenuTitle(name: String) {
        contextMenu?.setHeaderTitle(name)
    }

    private fun updateContextMenu(isOnline: Boolean) {
        contextMenu?.getItem(0)?.isEnabled = !isOnline
        contextMenu?.getItem(1)?.isEnabled = isOnline
        contextMenu?.getItem(2)?.isEnabled = isOnline

    }

    private fun showDialog() {
        var array = arrayOf(getString(R.string.signin))
        if (isOnline()) {
            array = arrayOf(getString(R.string.sign_out), getString(R.string.delete_account))
        }
        val builder = AlertDialog.Builder(requireContext())
        val displayName = GoogleSignIn.getLastSignedInAccount(requireActivity())?.displayName
        builder.setTitle(displayName)
            .setItems(array) { _, which ->
                when (array[which]) {
                    getString(R.string.signin) -> showLoginButton()
                    getString(R.string.sign_out) -> signOut()
                    getString(R.string.delete_account) -> signOut()
                }
            }
        builder.create().show()
    }

    private fun isOnline(): Boolean {
        return viewModel.getUIState().value!!.isOnline
    }

    private fun goToOnline() {
        if (isOnline()) {
            showCreateGameButton()
        } else {
            showLoginButton()
        }
    }

    private fun signOut() {
        viewModel.signOut()
        Toast.makeText(requireContext(), "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show()
    }

    private fun closeOnlinePopUp() {
        binding.popUp.popUp.visibility = View.GONE
    }

    private fun closeCreateGame() {
        binding.createGame.createGame.visibility = View.GONE
    }

    private fun showCreateGameButton() {
        binding.createGame.createGame.visibility = View.VISIBLE
    }

    private fun hideCreateGameButton() {
        binding.createGame.createGame.visibility = View.GONE
    }

    private fun showLoginButton() {
        binding.popUp.popUp.visibility = View.VISIBLE
    }

    private fun hideLoginButton() {
        binding.popUp.popUp.visibility = View.GONE
    }

    private fun showLoginError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}