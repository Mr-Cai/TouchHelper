package touch.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import touch.helper.R
import touch.helper.modle.ImageBean

class DragAdapter(data: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mData: ArrayList<Any> = data
    //拖拽回调接口
    private val mItemDragHelperCallback = ItemDragHelperCallback()
    //触摸辅助类 可以用于主动调用删除
    private val mItemTouchHelper = ItemTouchHelper(mItemDragHelperCallback)

    //生明常量
    companion object {
        const val TYPE_TITLE: Int = 1
        const val TYPE_SUB_IMG: Int = 2
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) { //将recyclerView依附到触摸辅助类

        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = mData[position]
        //将视图绑定数据
        holder.let {
            when (holder) {
                //如果是组标题部分
                is GroupViewHolder -> {
                    if (data is String) {
                        holder.mTitle.text = data
                    }
                }
                //如果是图片部分
                is ItemViewHolder -> {
                    if (data is ImageBean) {
                        holder.mImageView.setImageResource(data.mImageRes)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun getItemViewType(position: Int): Int {
        mData.let {
            return when (mData[position]) {
                is String -> {//当时字符串数据，也就是标题
                    TYPE_TITLE
                }
                else -> { //当都不是的时候就是图片了
                    TYPE_SUB_IMG
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_SUB_IMG -> {
                parent.let {
                    val root = LayoutInflater.from(parent.context).inflate(R.layout.recycler_sub_item, parent, false)
                    return ItemViewHolder(root)
                }
            }
            else -> {
                parent.let {
                    val root = LayoutInflater.from(parent.context).inflate(R.layout.recycler_group_item, parent, false)
                    return GroupViewHolder(root)
                }
            }
        }
    }

    /**
     * 标题视图
     * */
    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //标题
        var mTitle: TextView = itemView.findViewById(R.id.recycler_group_item_title) as TextView
    }

    /**
     * 图片显示视图,此处写法与上面是两种不同的构造函数写法,请自行理解
     * */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView = itemView.findViewById(R.id.recycler_sub_item_img) as ImageView
        //用于实现遮罩效果
        var mShadow: View = itemView.findViewById(R.id.recycler_sub_item_shadow)
    }

    inner class ItemDragHelperCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            var swipe = 0
            var move = 0
            recyclerView.let {
                if (recyclerView.layoutManager is GridLayoutManager) {
                    //如果是网格型 则可以左右上下都可以拖动
                    move = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                } else if (recyclerView.layoutManager is LinearLayoutManager) {
                    //支持上下拖动
                    move = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    //左右滑动删除
                    swipe = ItemTouchHelper.START or ItemTouchHelper.END
                }
            }
            return ItemTouchHelper.Callback.makeMovementFlags(move, swipe)

        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            //此处的返回值 决定是否可以拖动
            //此处不让标题来进行拖动效果 也就是拖动时 标题不会动
            if (viewHolder.itemViewType == TYPE_TITLE || target.itemViewType == TYPE_TITLE) {
                return false
            }

            val fromPos = viewHolder.adapterPosition
            val toPos = target.adapterPosition
            //此处为mData不为空时
            mData.let {
                val from = mData[fromPos]
                mData.removeAt(fromPos)
                mData.add(toPos, from)
                //执行交换动画
                notifyItemMoved(fromPos, toPos)
                return true
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        //不重写默认是返回true的 如果返回false的话 需要使用ItemTouchHelper的实例方法
        //使用 startDrag 来执行拖拽
        //使用 startSwipe 来执行滑动删除
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        //是否支持滑动功能
        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        /**
         * 此处用于状态变化时 更换图片状态
         * */
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //显示遮罩
                viewHolder?.let {
                    if (viewHolder is ItemViewHolder) {
                        viewHolder.mShadow.visibility = View.VISIBLE
                    }
                }
            }
        }

        // 当拖拽完成清理
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            //隐藏遮罩
            viewHolder.let {
                if (viewHolder is ItemViewHolder) {
                    viewHolder.mShadow.visibility = View.GONE
                }
            }
        }
    }
}