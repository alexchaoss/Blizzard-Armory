package com.BlizzardArmory.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.databinding.NewsListFragmentBinding
import com.BlizzardArmory.ui.main.MainActivity
import com.BlizzardArmory.ui.navigation.GamesActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsListFragment : Fragment() {


    private val viewModel: NewsListViewModel by viewModels()
    private lateinit var binding: NewsListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addOnBackPressCallback(activity as GamesActivity)
        binding = NewsListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        setSwipeGestureToRefreshData()
        viewModel.getDownloaded().value = false
        setBackToTopButton()
    }

    private fun setObservers() {
        viewModel.getDownloaded().observe(viewLifecycleOwner, {
            if (it) {
                viewModel.filterList()
                viewModel.setupRecycler()
                binding.newsRecycler.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = NewsAdapter(viewModel.getNewsList().value!!, context)
                }
            } else {
                viewModel.downloadNews()
                binding.swipe.isRefreshing = false
            }
        })
        viewModel.getShowMore().observe(viewLifecycleOwner, {
            if (it) {
                viewModel.filterList()
                (binding.newsRecycler.adapter as NewsAdapter).addItems(viewModel.getNewsList().value!!)
            }
        })
    }

    private fun setBackToTopButton() {
        binding.newsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() == 0
                        || (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == recyclerView.adapter?.itemCount!! - 1) {
                    binding.backToTop.visibility = View.GONE
                } else {
                    binding.backToTop.visibility = View.VISIBLE
                }
            }
        })

        binding.backToTop.setOnClickListener {
            val job = viewModel.coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    binding.newsRecycler.layoutManager?.smoothScrollToPosition(binding.newsRecycler, RecyclerView.State(), 0)
                    if ((binding.newsRecycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() > 30) {
                        delay(1500)
                    } else {
                        delay((binding.newsRecycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() * 50L)
                    }
                    binding.newsRecycler.scrollToPosition(0)
                }
            }
            viewModel.jobs.add(job)
            binding.backToTop.visibility = View.GONE
        }
    }

    private fun setSwipeGestureToRefreshData() {
        binding.swipe.setOnRefreshListener {
            viewModel.getDownloaded().value = false
        }
    }

    companion object{
        fun addOnBackPressCallback(activity: GamesActivity){
            activity.onBackPressedDispatcher.addCallback {
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activity.startActivity(intent)
            }
        }
    }
}