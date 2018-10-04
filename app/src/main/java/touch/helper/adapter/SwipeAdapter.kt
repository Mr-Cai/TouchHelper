package touch.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import touch.helper.R
import touch.helper.modle.ImageBean

/**
 * Created by nieyh on 17-5-18.
 */
class SwipeAdapter(data: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //数据 可能为空 此处Any 就是java中的object
    private var mData: ArrayList<Any> = data
    //拖拽回调接口
    private val mItemDragHelperCallback = ItemDragHelperCallback()
    //触摸辅助类 可以用于主动调用删除
    private val mItemTouchHelper = ItemTouchHelper(mItemDragHelperCallback)

    //生明常量
    companion object {
        const val TYPE_SUB_IMG: Int = 2
    }

    /**
     * 将recyclerView依附到触摸辅助类
     * */
    fun attachToRecyclerView(recyclerView: RecyclerView) {
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = mData[position]
        //将视图绑定数据
        holder.let {
            when (holder) {
                //如果是图片部分
                is ItemViewHolder -> {
                    if (data is ImageBean) {
                        holder.mImageView.setImageResource(data.mImageRes)
                    }
                }
            }
        }
    }

    /**
     * 返回列表的视图个数
     * */
    override fun getItemCount(): Int = mData.size

    override fun getItemViewType(position: Int): Int = TYPE_SUB_IMG


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_SUB_IMG -> {
                parent.let {
                    val layoutLayouter = LayoutInflater.from(parent.context)
                    val root = layoutLayouter.inflate(R.layout.recycler_sub_item, parent, false)
                    return ItemViewHolder(root)
                }
            }
        }
        //由于不能使用返回空 所以必须自行返回一个非空值
        return ItemViewHolder(parent as View)
    }

    /**
     * 图片显示视图,此处写法与上面是两种不同的构造函数写法,请自行理解
     * */
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView = itemView.findViewById(R.id.recycler_sub_item_img) as ImageView
        //用于实现遮罩效果
        var mShadow: View = itemView.findViewById(R.id.recycler_sub_item_shadow)
    }

    //kotlin 如果内部类需要访问外部类数据 则要声明为inner
    inner class ItemDragHelperCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            //此处返回可以拖动的方向值
            var swipe = 0
            var move = 0
            //此处为 假设recyclerview 不为空
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
            //此处不让标题来进行拖动效果 也就是拖动时 标题不会动
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
            if (viewHolder.itemViewType == TYPE_SUB_IMG) {
                //用于执行滑动删除
                val pos = viewHolder.adapterPosition
                mData.removeAt(pos)
                notifyItemRemoved(pos)
            }
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //显示遮罩
                viewHolder.let {
                    if (viewHolder is ItemViewHolder) {
                        viewHolder.mShadow.visibility = View.VISIBLE
                    }
                }
            }
        }

        /**
         * 此处当拖拽完成
         * */
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