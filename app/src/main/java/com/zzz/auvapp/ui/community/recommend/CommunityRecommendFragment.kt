package com.zzz.auvapp.ui.community.recommend

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentRefreshBinding
import com.zzz.common.ext.gone
import com.zzz.common.mvvm.BaseVMFragment
import com.zzz.common.uitls.ToastUtil

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CommunityRecommendFragment :
    BaseVMFragment<FragmentRefreshBinding>(R.layout.fragment_refresh) {
    private val viewModel by lazy { ViewModelProvider(this).get(CommunityRecommendViewModel::class.java) }
    private lateinit var adapter: CommunityRecommendAdapter

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
                    adapter.notifyItemRangeInserted(itemCount, result.itemList.size)
                }
                viewModel.setNextUrl(result.nextPageUrl)

                if (result.nextPageUrl.isEmpty()) {
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
        binding.smartRefresh.setOnRefreshListener { viewModel.refresh() }
        binding.smartRefresh.setOnLoadMoreListener { viewModel.loadMore() }

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
//                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }

        binding.recyclerView.addItemDecoration(ItemDecoration())
        binding.recyclerView.setHasFixedSize(true)
        adapter = CommunityRecommendAdapter(this, viewModel.dataList)
        binding.recyclerView.adapter = adapter

        showLoadingView()
        viewModel.refresh()
    }

    override fun initData() {

    }
}