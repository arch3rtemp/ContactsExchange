package dev.arch3rtemp.contactexchange.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.arch3rtemp.contactexchange.R
import dev.arch3rtemp.contactexchange.databinding.FragmentHomeBinding
import dev.arch3rtemp.contactexchange.domain.model.Contact
import dev.arch3rtemp.contactexchange.presentation.home.adapter.ContactsListAdapter
import dev.arch3rtemp.contactexchange.presentation.home.adapter.drawer.CardItemDrawer
import dev.arch3rtemp.contactexchange.presentation.home.adapter.drawer.ContactItemDrawer
import dev.arch3rtemp.core_ui.base.BaseFragment
import dev.arch3rtemp.core_ui.view.AppSearchView
import dev.arch3rtemp.core_ui.view.DebounceQueryTextListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<HomeEvent, HomeEffect, HomeState, FragmentHomeBinding, HomeViewModel>(),
    ContactsListAdapter.ClickListener {

    override val viewModel: HomeViewModel by viewModel()
    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate
    private val cardsAdapter: ContactsListAdapter by lazy { ContactsListAdapter(this@HomeFragment) }
    private val contactsAdapter: ContactsListAdapter by lazy { ContactsListAdapter(this@HomeFragment) }

    override fun prepareView(savedInstanceState: Bundle?) {
        setupToolbar()
        setupRecyclerView()
        initObservers()
        setListeners()
    }

    private fun setupToolbar() = with(binding) {

        val searchView = toolbarContact.menu.findItem(R.id.action_search).actionView as AppSearchView
        searchView.setOnQueryTextListener(
            DebounceQueryTextListener(
                lifecycle = viewLifecycleOwner.lifecycle,
                onDebounceQueryTextChange = { query ->
                    searchQuery(query)
                })
        )
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvCards.adapter = cardsAdapter
            rvContacts.adapter = contactsAdapter
        }
    }

    private fun initObservers() {
        observeRecyclerListener()
    }

    private fun setListeners() {
        with(binding) {
            fabAdd.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToCreateCardFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun observeRecyclerListener() {
        with(binding) {
            rvCards.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // If scrolling towards the left (dx < 0), hide the FAB
                    if (dx > 0 && fabAdd.isVisible) {
                        fabAdd.hide()
                    }

                    // If scrolling towards the right (dx > 0), show the FAB
                    if (dx < 0 && !fabAdd.isVisible) {
                        fabAdd.show()
                    }
                }
            })
        }
    }

    private fun searchQuery(query: String) {
        viewModel.setEvent(HomeEvent.OnSearchTyped(query))
    }

    override fun renderState(state: HomeState) = with(binding) {
        renderCardsState(state.cardsState)
        renderContactsState(state.contactsState)
        contactsAdapter.filterContacts(state.query)
    }

    private fun renderCardsState(state: ViewState) {
        when (state) {
            is ViewState.Success -> {
                showCardsSuccess()
                val cards = state.data.map { card ->
                    CardItemDrawer(card)
                }
                cardsAdapter.modifyList(cards)
            }
            ViewState.Loading -> {
                showCardsLoading()
            }
            ViewState.Error -> {
                showCardsError()
            }
            ViewState.Empty -> {
                showCardsEmpty()
            }
        }
    }

    private fun renderContactsState(state: ViewState) {
        when (state) {
            is ViewState.Success -> {
                showContactsSuccess()
                val contacts = state.data.map { contact ->
                    ContactItemDrawer(contact)
                }
                contactsAdapter.modifyList(contacts)
            }
            ViewState.Loading -> {
                showContactsLoading()
            }
            ViewState.Error -> {
                showContactsError()
            }
            ViewState.Empty -> {
                showContactsEmpty()
            }
        }
    }

    override fun renderEffect(effect: HomeEffect) {
        when (effect) {
            is HomeEffect.Error -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }
            is HomeEffect.Deleted -> {
                Snackbar
                    .make(requireView(), getString(R.string.msg_contact_deleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.msg_undo)) {
                        viewModel.setEvent(HomeEvent.OnContactSaved(effect.contact))
                    }
                    .show()
            }
        }
    }

    override fun onContactClicked(id: Int) {
        goToCardFragment(id)
    }

    override fun onCardClicked(id: Int) {
        goToCardFragment(id)
    }

    override fun onDeleteClicked(contact: Contact) {
        viewModel.setEvent(HomeEvent.OnContactDelete(contact))
    }

    private fun goToCardFragment(id: Int) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCardFragment(id))
    }

    private fun showCardsEmpty() = with(binding) {
        ivCardsEmpty.visibility = View.VISIBLE
        progressCircularCards.visibility = View.INVISIBLE
        ivCardsError.visibility = View.INVISIBLE
        rvCards.visibility = View.INVISIBLE
    }

    private fun showContactsEmpty() = with(binding) {
        ivContactsEmpty.visibility = View.VISIBLE
        progressCircularContacts.visibility = View.GONE
        ivContactsError.visibility = View.GONE
        rvContacts.visibility = View.GONE
    }

    private fun showCardsLoading() = with(binding) {
        progressCircularCards.visibility = View.VISIBLE
        ivCardsEmpty.visibility = View.INVISIBLE
        ivCardsError.visibility = View.INVISIBLE
        rvCards.visibility = View.INVISIBLE
    }

    private fun showContactsLoading() = with(binding) {
        progressCircularContacts.visibility = View.VISIBLE
        ivContactsEmpty.visibility = View.GONE
        ivContactsError.visibility = View.GONE
        rvContacts.visibility = View.GONE
    }

    private fun showCardsSuccess() = with(binding) {
        rvCards.visibility = View.VISIBLE
        progressCircularCards.visibility = View.INVISIBLE
        ivCardsEmpty.visibility = View.INVISIBLE
        ivCardsError.visibility = View.INVISIBLE
    }

    private fun showContactsSuccess() = with(binding) {
        rvContacts.visibility = View.VISIBLE
        progressCircularContacts.visibility = View.GONE
        ivContactsEmpty.visibility = View.GONE
        ivContactsError.visibility = View.GONE
    }

    private fun showCardsError() = with(binding) {
        ivCardsError.visibility = View.VISIBLE
        rvCards.visibility = View.INVISIBLE
        progressCircularCards.visibility = View.INVISIBLE
        ivCardsEmpty.visibility = View.INVISIBLE
    }

    private fun showContactsError() = with(binding) {
        ivContactsError.visibility = View.VISIBLE
        rvContacts.visibility = View.GONE
        progressCircularContacts.visibility = View.GONE
        ivContactsEmpty.visibility = View.GONE
    }
}
