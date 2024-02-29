package com.barry.color_imgage_kotlin.main

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barry.color_imgage_kotlin.GlideApp
import com.barry.color_imgage_kotlin.R
import com.barry.color_imgage_kotlin.databinding.ActivityMainBinding
import com.barry.color_imgage_kotlin.databinding.ItemColorImageBinding
import com.barry.color_imgage_kotlin.databinding.ItemPagingLoadStateBinding
import com.barry.color_imgage_kotlin.network.ColorImgModel
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _viewBind: ActivityMainBinding? = null
    private val viewBind: ActivityMainBinding get() = _viewBind!!

    private val DIFF_CALLBACK: DiffUtil.ItemCallback<ColorImgModel> =
        object : DiffUtil.ItemCallback<ColorImgModel>() {
            override fun areItemsTheSame(oldItem: ColorImgModel, newItem: ColorImgModel): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ColorImgModel,
                newItem: ColorImgModel
            ): Boolean {
                return oldItem == newItem
            }
        }

    private val colorImageAdapter: ColorImageAdapter = ColorImageAdapter(DIFF_CALLBACK)

    private val viewModel: MainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(this, application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBind.root)

        viewBind.activityMainColorImgRecyclerview.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 4)
            adapter = colorImageAdapter.withLoadStateHeaderAndFooter(
                header = LoaderAdapter(),
                footer = LoaderAdapter()
            )
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                // do your work here
                viewModel.colorImageDataFlow.collect {
                    colorImageAdapter.submitData(it)
                }
            }
        }
    }
}

class ColorImageAdapter(diffCallback: DiffUtil.ItemCallback<ColorImgModel>):
    PagingDataAdapter<ColorImgModel, RecyclerView.ViewHolder>(diffCallback) {

    inner class ColorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val viewBind: ItemColorImageBinding = ItemColorImageBinding.bind(itemView)

        fun bindData(data: ColorImgModel) {
            viewBind.itemColorIdTextView.text = data.id.toString()
            viewBind.itemColorTitleTextView.text = data.title.toString()
            GlideApp
                .with(itemView.context)
                .load(data.thumbnailUrl)
                .override(150, 150)
                .dontAnimate()
                .dontTransform()
                .skipMemoryCache(true)
                .priority(Priority.IMMEDIATE)
                .format(DecodeFormat.PREFER_RGB_565)
                .encodeFormat(Bitmap.CompressFormat.JPEG)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewBind.itemColorImageView)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ColorViewHolder) {
            getItem(position)?.let {
                holder.bindData(data = it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color_image, parent, false))
    }
}

class LoaderAdapter: LoadStateAdapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (holder is LoadingViewHolder) {
            holder.setState(loadState)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        return LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_paging_load_state, parent, false))
    }

    inner class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val viewBind: ItemPagingLoadStateBinding = ItemPagingLoadStateBinding.bind(itemView)
        fun setState(loadState: LoadState) {
            viewBind.loadingProgressBar.isVisible = loadState is LoadState.Loading
        }
    }

}

