package com.zzz.auvapp.ui.home.discovery

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentRefreshBinding
import com.zzz.common.ext.gone
import com.zzz.common.ext.loge
import com.zzz.common.mvvm.BaseVMFragment
import com.zzz.common.uitls.ToastUtil

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/05
 *     desc   : 首页-发现
 *     version: 1.0
 * </pre>
 */
class HomeDiscoveryFragment: BaseVMFragment<FragmentRefreshBinding>(R.layout.fragment_refresh) {
    private val viewModel by lazy { ViewModelProvider(this).get(DiscoveryHomeViewModel::class.java) }
    private lateinit var adapter: HomeDiscoveryAdapter

    override fun openObserve() {
        viewModel.requestUrlLiveData.observe(this) {
            val result = it.getOrNull()
            if (result != null) {
                if (result.itemList.isNullOrEmpty() && viewModel.dataList.isEmpty()) {
                    binding.smartRefresh.closeHeaderOrFooter()
                    return@observe
                }
                if (result.itemList.isNullOrEmpty() && viewModel.dataList.isNotEmpty()) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                    return@observe
                }
                if (result.nextPageUrl.isNullOrEmpty()) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                } else {
                    binding.smartRefresh.closeHeaderOrFooter()
                    viewModel.setNextUrl(result.nextPageUrl)
                }
                if (viewModel.isRefresh()) {
                    viewModel.dataList.clear()
                    viewModel.dataList.addAll(result.itemList)
                    adapter.notifyDataSetChanged()
                } else {
                    val size = viewModel.dataList.size
                    viewModel.dataList.addAll(result.itemList)
                    adapter.notifyItemRangeInserted(size, result.itemList.size)
                }
            } else {
                ToastUtil.show(it.exceptionOrNull().toString())
                binding.smartRefresh.closeHeaderOrFooter()
            }
            hideLoadingView()
        }
    }

    override fun initView() {
        binding.fab.gone()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)
        adapter = HomeDiscoveryAdapter(this, viewModel.dataList)
        binding.recyclerView.adapter = adapter

        binding.smartRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.smartRefresh.setOnLoadMoreListener {
            viewModel.loadMore()
        }
        showLoadingView()
        viewModel.refresh()
    }

    override fun initData() {

    }
}