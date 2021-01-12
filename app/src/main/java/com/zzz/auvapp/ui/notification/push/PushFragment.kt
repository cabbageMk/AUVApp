package com.zzz.auvapp.ui.notification.push

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentRefreshBinding
import com.zzz.common.ext.gone
import com.zzz.common.ext.toast
import com.zzz.common.mvvm.BaseVMFragment

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PushFragment: BaseVMFragment<FragmentRefreshBinding>(R.layout.fragment_refresh) {
    private val viewModel by lazy { ViewModelProvider(this).get(PushViewModel::class.java) }
    private lateinit var adapter: PushAdapter

    override fun openObserve() {
        viewModel.requestLiveData.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (result != null) {
                if (result.messageList.isNullOrEmpty() && viewModel.dataList.isEmpty()) {
                    binding.smartRefresh.closeHeaderOrFooter()
                    return@observe
                }
                if (result.messageList.isNullOrEmpty() && viewModel.dataList.isNotEmpty()) {
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
                    viewModel.dataList.addAll(result.messageList)
                    adapter.notifyDataSetChanged()
                } else {
                    val size = viewModel.dataList.size
                    viewModel.dataList.addAll(result.messageList)
                    adapter.notifyItemRangeInserted(size, result.messageList.size)
                }
            } else {
                toast(it.exceptionOrNull().toString())
            }
            hideLoadingView()
        }
    }

    override fun initView() {
        binding.fab.gone()
        binding.smartRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.smartRefresh.setOnLoadMoreListener { viewModel.loadMore() }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)
        adapter = PushAdapter(this, viewModel.dataList)
        binding.recyclerView.adapter = adapter

        showLoadingView()
        viewModel.refresh()
    }

    override fun initData() {

    }
}