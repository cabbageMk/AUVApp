package com.zzz.auvapp.ui.home.recommend

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentRefreshBinding
import com.zzz.common.mvvm.BaseVMFragment
import com.zzz.common.uitls.ToastUtil

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendHomeFragment: BaseVMFragment<FragmentRefreshBinding>(R.layout.fragment_refresh) {
    private val viewModel by lazy { ViewModelProvider(this).get(RecommendHomeViewModel::class.java) }
    private lateinit var adapter: HomeRecommendAdapter

    override fun openObserve() {
        viewModel.dataListLiveData.observe(this) {
            val result = it.getOrNull()
            hideLoadingView()
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
                viewModel.nextUrl = result.nextPageUrl

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
        }
    }

    override fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)
        adapter = HomeRecommendAdapter(context!!, viewModel.dataList)
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