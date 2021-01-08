package com.zzz.auvapp.ui.community.follow

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentRefreshBinding
import com.zzz.common.ext.gone
import com.zzz.common.mvvm.BaseVMFragment
import com.zzz.common.uitls.ToastUtil

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CommunityFollowFragment : BaseVMFragment<FragmentRefreshBinding>(R.layout.fragment_refresh) {
    private val viewModel by lazy { ViewModelProvider(this).get(FollowViewModel::class.java) }
    private lateinit var adapter: FollowAdapter

    override fun openObserve() {
        viewModel.requestLiveData.observe(this) {
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
                // 是否刷新  是否加载更多
                if (viewModel.isRefresh()) {
                    viewModel.dataList.clear()
                    viewModel.dataList.addAll(result.itemList)
                    adapter.notifyDataSetChanged()
                } else {
                    val itemCount = viewModel.dataList.size
                    viewModel.dataList.addAll(result.itemList)
                    adapter.notifyItemRangeInserted(itemCount+1, result.itemList.size)
                }
                viewModel.setNextUrl(result.nextPageUrl)

                if (result.nextPageUrl?.isEmpty() == true) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                } else {
                    binding.smartRefresh.closeHeaderOrFooter()
                }
            } else {
                binding.smartRefresh.closeHeaderOrFooter()
                it.exceptionOrNull()?.printStackTrace()
                ToastUtil.show(it.exceptionOrNull()?.message?: "未知错误")
            }
            hideLoadingView()
        }
    }

    override fun initView() {
        binding.fab.gone()
        binding.smartRefresh.setOnLoadMoreListener {
            viewModel.loadMore()
        }
        binding.smartRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)

        adapter = FollowAdapter(this, viewModel.dataList)
        binding.recyclerView.adapter = adapter

        showLoadingView()
        viewModel.refresh()

    }

    override fun initData() {

    }
}